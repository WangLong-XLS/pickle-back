package com.pickle.agent.rag.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class RAGConfiguration {

    @Bean
    public ChatClient ragChatClient(ChatClient.Builder builder, VectorStore vectorStore) {
        // 配置 RAG Advisor
        var qaAdvisor = QuestionAnswerAdvisor.builder(vectorStore)
                .searchRequest(SearchRequest.builder()
                        .topK(4)                    // 返回最相关的 4 个文档块
                        .similarityThreshold(0.7)   // 相似度阈值，低于 0.7 的会被过滤
                        .build())
                .build();

        return builder
                .defaultAdvisors(qaAdvisor)
                .build();
    }

    /**
     * 纯粹的大模型 ChatClient（不检索知识库）
     */
    @Bean
    public ChatClient llmOnlyChatClient(ChatClient.Builder builder) {
        return builder
                .defaultSystem("你是一个智能助手，请用中文回答用户的问题。")
                .build();
    }
}