package com.pickle.agent.service.impl;

import com.pickle.agent.service.IMemoryService;
import com.pickle.agent.service.IRAGAnswerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

//@Service
@Slf4j
public class RAGAnswerService implements IRAGAnswerService {

    private final ChatClient ragChatClient;
    private final VectorStore vectorStore;
    private final ChatClient llmOnlyChatClient;
    private final double similarityThreshold;
    private final boolean fallbackToLLM;
    private final IMemoryService memoryService;

    // 手动编写构造函数，使用 @Qualifier
    public RAGAnswerService(
            ChatClient ragChatClient,
            VectorStore vectorStore,
            @Qualifier("llmOnlyChatClient") ChatClient llmOnlyChatClient,
            @Value("${knowledge.similarity.threshold:0.65}") double similarityThreshold,
            @Value("${knowledge.fallback.llm.enabled:true}") boolean fallbackToLLM,
            IMemoryService memoryService
    ) {
        this.ragChatClient = ragChatClient;
        this.vectorStore = vectorStore;
        this.llmOnlyChatClient = llmOnlyChatClient;
        this.similarityThreshold = similarityThreshold;
        this.fallbackToLLM = fallbackToLLM;
        this.memoryService = memoryService;
    }


    /**
     * 基于知识库回答用户问题
     *
     * @param userQuestion 用户问题
     * @return AI 生成的答案
     */
    @Override
    public String askQuestion(String userQuestion) {
        log.info("用户问题: {}", userQuestion);

        // 1. 检索历史记忆
        String memoryContext = memoryService.retrieveMemories("123456789", userQuestion);
        log.info("记忆上下文: {}", memoryContext);

        // 2. 增强用户问题
        String enhancedQuestion = userQuestion;
        if (!memoryContext.isEmpty()) {
            enhancedQuestion = "【历史对话】\n" + memoryContext + "\n【当前问题】\n" + userQuestion;
            log.info("使用增强后的问题");
        }

        // 3. 调用原有 RAG/LLM 逻辑
        String answer = originalAskLogic(enhancedQuestion);

        // 4. 存储本次对话记忆
        memoryService.storeMemory("123456789", userQuestion, answer);
        return answer;
    }

    private String originalAskLogic(String userQuestion) {
        // 1. 先检索向量库
        SearchRequest request = SearchRequest.builder()
                .query(userQuestion)
                .topK(1)  // 只需要最高相似度做判断
                .similarityThreshold(similarityThreshold - 0.1) // 先放宽一点，后面再判断
                .build();

        List<Document> results = vectorStore.similaritySearch(request);

        // 2. 判断最高相似度
        double maxScore = results.isEmpty() ? 0 : results.getFirst().getScore();
        log.info("最高相似度: {}", maxScore);

        String answer;
        if (maxScore >= similarityThreshold) {
            // 知识库中有相关内容，使用 RAG 回答
            log.info("使用知识库回答 (相似度: {})", maxScore);
            answer = ragChatClient.prompt()
                    .user(userQuestion)
                    .call()
                    .content();
        } else {
            // 知识库中无相关内容，降级到大模型
            if (fallbackToLLM) {
                log.warn("知识库无相关内容 (最高相似度: {})，降级到大模型回答", maxScore);
                answer = llmFallbackAnswer(userQuestion);
            } else {
                log.warn("知识库无相关内容且降级功能未开启");
                answer = noKnowledgeFallback(userQuestion);
            }
        }
        log.info("问题: {}\n答案: {}", userQuestion, answer);
        return answer;
    }

    /**
     * 降级到大模型回答
     */
    private String llmFallbackAnswer(String userQuestion) {
        String systemPrompt = """
                你是智能助手，当前问题不在现有知识库范围内。
                请用中文自然回答用户问题，但需在回答末尾添加提示：
                "（注意：当前回答基于通用知识，非本系统知识库内容）"
                """;

        return llmOnlyChatClient.prompt()
                .system(systemPrompt)
                .user(userQuestion)
                .call()
                .content();
    }

    /**
     * 无降级时的友好提示
     */
    private String noKnowledgeFallback(String userQuestion) {
        return String.format("""
                抱歉，我目前的知识库中没有关于「%s」的相关信息。
                
                建议：
                1. 换个方式描述你的问题
                2. 上传相关文档后重试
                3. 联系管理员补充知识库内容
                """, userQuestion);
    }


    /**
     * 带元数据过滤的问答（只搜索"情感类"文档）
     * 利用你添加的 category 元数据
     */
    @Override
    public String askWithFilter(String userQuestion) {
        String answer = ragChatClient.prompt()
                .user(userQuestion)
                .advisors(advisor -> advisor.param(
                        QuestionAnswerAdvisor.FILTER_EXPRESSION,
                        "category == '情感类'"
                ))
                .call()
                .content();
        return answer;
    }

    /**
     * 只搜索指定文档
     */
    public String askInDocument(String userQuestion, String docName) {
        String answer = ragChatClient.prompt()
                .user(userQuestion)
                .advisors(advisor -> advisor.param(
                        QuestionAnswerAdvisor.FILTER_EXPRESSION,
                        String.format("docName == '%s'", docName)
                ))
                .call()
                .content();
        return answer;
    }
}