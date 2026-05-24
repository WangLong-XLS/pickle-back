package com.pickle.agent.service.impl;

import com.pickle.agent.service.IMemoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//@Service
@Slf4j
public class MemoryService implements IMemoryService {
    
    private final VectorStore vectorStore;
    private final ChatClient ragChatClient;  // 用于LLM判断重复

    // 可选：添加内存缓存，避免频繁调用LLM
    private final Set<String> recentMemoryCache = new HashSet<>();

    public MemoryService(VectorStore vectorStore, ChatClient ragChatClient) {
        this.vectorStore = vectorStore;
        this.ragChatClient = ragChatClient;
    }

    // 存储对话记忆（带去重）
    @Override
    public void storeMemory(String userId, String keyInfo, String assistantMessage) {
        // 1. 提取关键信息（而不是存储完整对话）
//        String keyInfo = extractKeyInfo(userMessage, assistantMessage);

        // 如果没有提取到关键信息，就不存储
        if (keyInfo == null) {
            log.info("未提取到关键信息，跳过存储");
            return;
        }

        log.info("提取的关键信息: {}", keyInfo);

        // 2. 内存缓存快速去重（避免频繁调用LLM）
        String cacheKey = userId + ":" + keyInfo.hashCode();
        if (recentMemoryCache.contains(cacheKey)) {
            log.info("内存缓存命中，跳过重复存储");
            return;
        }

        // 3. 检索相似的已有记忆
        SearchRequest checkRequest = SearchRequest.builder()
                .query(keyInfo)  // 用提取的关键信息检索
                .topK(3)
                .similarityThreshold(0.75)  // 高阈值初步筛选
                .build();

        List<Document> similarMemories = vectorStore.similaritySearch(checkRequest);

        // 4. 如果有相似的记忆，用LLM判断是否重复
        if (!similarMemories.isEmpty()) {
            for (Document existing : similarMemories) {
                // 只检查同一用户的记忆
                if (!userId.equals(existing.getMetadata().get("userId"))) {
                    continue;
                }

                boolean isDuplicate = judgeDuplicate(existing.getText(), keyInfo);

                if (isDuplicate) {
                    log.info("检测到重复记忆，跳过存储。已有: {}", existing.getText());
                    // 更新已有记忆的时间戳（表示最近又被提到）
                    updateMemoryTimestamp(existing);
                    return;
                }
            }
        }

        // 5. 不重复，存储新记忆
        Document memory = new Document(keyInfo +"\n" +assistantMessage);  // 只存储关键信息，不存完整对话
        memory.getMetadata().put("userId", userId);
        memory.getMetadata().put("timestamp", System.currentTimeMillis());
        memory.getMetadata().put("type", "对话记忆");

        vectorStore.add(List.of(memory));
        log.info("存储新记忆: {}", keyInfo);

        // 6. 加入内存缓存（1小时后过期）
        recentMemoryCache.add(cacheKey);
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.schedule(() -> recentMemoryCache.remove(cacheKey), 1, TimeUnit.HOURS);
    }


    /**
     * 提取关键信息（姓名、偏好、重要事实等）
     */
    private String extractKeyInfo(String userMessage, String assistantMessage) {
        // 提取姓名
        if (userMessage.contains("我叫") || userMessage.contains("我是")) {
            Pattern pattern = Pattern.compile("我叫([^，。！？\\s]+)|我是([^，。！？\\s]+)");
            Matcher matcher = pattern.matcher(userMessage);
            if (matcher.find()) {
                String name = matcher.group(1) != null ? matcher.group(1) : matcher.group(2);
                return "用户姓名：" + name.trim();
            }
        }

        // 提取年龄
        Pattern agePattern = Pattern.compile("(\\d+)[岁|years old]");
        Matcher ageMatcher = agePattern.matcher(userMessage);
        if (ageMatcher.find()) {
            return "用户年龄：" + ageMatcher.group(1) + "岁";
        }

        // 提取偏好（喜欢/不喜欢）
        if (userMessage.contains("喜欢")) {
            Pattern likePattern = Pattern.compile("喜欢([^，。！？\\s]+)");
            Matcher likeMatcher = likePattern.matcher(userMessage);
            if (likeMatcher.find()) {
                return "用户喜欢：" + likeMatcher.group(1);
            }
        }

        if (userMessage.contains("不喜欢") || userMessage.contains("讨厌")) {
            Pattern dislikePattern = Pattern.compile("不喜欢([^，。！？\\s]+)|讨厌([^，。！？\\s]+)");
            Matcher dislikeMatcher = dislikePattern.matcher(userMessage);
            if (dislikeMatcher.find()) {
                String dislike = dislikeMatcher.group(1) != null ? dislikeMatcher.group(1) : dislikeMatcher.group(2);
                return "用户不喜欢：" + dislike;
            }
        }

        // 如果用户问题较短（可能是追问），不提取（避免存储无意义内容）
        /*if (userMessage.length() < 10) {
            return null;
        }*/

        // 默认：如果不是明显的个人信息，返回null不存储
        return null;
    }

    /**
     * 用LLM判断两条信息是否重复
     */
    private boolean judgeDuplicate(String existingText, String newText) {
        String prompt = String.format("""
            判断以下两条信息是否本质相同或表达同一件事（只回答true或false）：
            
            已有信息：%s
            新信息：%s
            
            注意：
            - 如果新信息是对已有信息的补充或细化，回答false
            - 如果新信息只是换了个说法但含义相同，回答true
            - 如果两条信息完全不同，回答false
            
            只回答true或false，不要有其他内容：
            """, existingText, newText);

        try {
            String result = ragChatClient.prompt()
                    .user(prompt)
                    .call()
                    .content();

            boolean isDuplicate = false;
            if (result != null) {
                isDuplicate = "true".equalsIgnoreCase(result.trim());
            }
            log.info("LLM判断重复结果: {} - 已有: {}, 新: {}", isDuplicate, existingText, newText);
            return isDuplicate;

        } catch (Exception e) {
            log.error("LLM判断重复失败，默认不重复", e);
            return false;  // 失败时默认不重复，避免丢失信息
        }
    }

    /**
     * 更新已有记忆的时间戳
     */
    private void updateMemoryTimestamp(Document memory) {
        // 注意：PGVector 不支持直接更新，需要删除后重新插入
        String memoryId = memory.getId();
        String content = memory.getText();
        String userId = (String) memory.getMetadata().get("userId");

        // 删除旧记录
        vectorStore.delete(List.of(memoryId));

        // 创建新记录（更新时间戳）
        if (content != null) {
            Document updated = new Document(content);
            updated.getMetadata().put("userId", userId);
            updated.getMetadata().put("timestamp", System.currentTimeMillis());
            updated.getMetadata().put("type", "对话记忆");
            updated.getMetadata().put("updated", true);
            vectorStore.add(List.of(updated));
            log.info("更新记忆时间戳: {}", content);
        }
    }

    // 检索相关记忆（用于新对话）
    @Override
    public String retrieveMemories(String userId, String currentQuestion) {
        SearchRequest request = SearchRequest.builder()
                .query(currentQuestion)
                .filterExpression(String.format("userId == '%s' && type == '对话记忆'", userId))
                .topK(3)
                .similarityThreshold(0.4)
                .build();
        
        List<Document> memories = vectorStore.similaritySearch(request);
        if (memories.isEmpty()) {
            return "";
        }

        // 将记忆拼接成上下文
        StringBuilder context = new StringBuilder("历史对话记录：\n");
        for (Document doc : memories) {
            context.append(doc.getText()).append("\n---\n");
        }
        return context.toString();
    }
}