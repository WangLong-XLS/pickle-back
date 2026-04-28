package com.pickle.sys.rag;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.document.DocumentTransformer;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

@Component
@Slf4j
public class MarkdownHierarchicalSplitter implements DocumentTransformer {

    private final int maxChunkTokens;  // 注意：这里是 Token 数
    private final TokenTextSplitter fallbackSplitter;

    public MarkdownHierarchicalSplitter(@Value("${document.chunk.max-tokens:}") int maxChunkTokens) {
        this.maxChunkTokens = maxChunkTokens;
        // 创建后备分割器（按 Token 切）
        this.fallbackSplitter = new TokenTextSplitter(maxChunkTokens, maxChunkTokens / 10, 5, 10000, true);
    }

    @Override
    public List<Document> apply(List<Document> documents) {
        List<Document> chunks = new ArrayList<>();
        for (Document doc : documents) {
            String content = doc.getText();
            if (content == null || content.isEmpty()) continue;

            Pattern headingPattern = Pattern.compile("^(?=#{1,6}\\s)", Pattern.MULTILINE);
            String[] sections = headingPattern.split(content);
            log.info("文档按标题分割后，共得到 {} 个章节", sections.length);

            for (String section : sections) {
                if (section.trim().isEmpty()) continue;

                // 估算 Token 数（粗略估算：中文字符≈0.5-1 token，英文≈0.25 token）
                int estimatedTokens = estimateTokenCount(section);

                if (estimatedTokens > maxChunkTokens) {
                    // 章节太长，使用 TokenTextSplitter 进一步切分
                    Document tempDoc = new Document(section, new HashMap<>(doc.getMetadata()));
                    List<Document> subChunks = fallbackSplitter.apply(List.of(tempDoc));
                    chunks.addAll(subChunks);
                    log.debug("长章节 {} 字符被切分为 {} 个子块", section.length(), subChunks.size());
                } else {
                    Document chunk = new Document(section, new HashMap<>(doc.getMetadata()));
                    chunk.getMetadata().put("source_title", extractTitle(section));
                    chunk.getMetadata().put("chunk_type", "heading");
                    chunks.add(chunk);
                }
            }
        }
        return chunks;
    }

    private int estimateTokenCount(String text) {
        // 粗略估算：英文 1 字符 ≈ 0.25 token，中文 1 字符 ≈ 0.5 token
        int chineseCount = text.replaceAll("[^\u4e00-\u9fa5]", "").length();
        int otherCount = text.length() - chineseCount;
        return (int) (chineseCount * 0.5 + otherCount * 0.25);
    }

    private String extractTitle(String section) {
        String[] lines = section.split("\n");
        for (String line : lines) {
            if (line.trim().startsWith("#")) {
                // 移除 # 符号并去空格
                return line.replaceAll("^#+\\s*", "").trim();
            }
        }
        return "无标题";
    }
}