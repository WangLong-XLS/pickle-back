package com.pickle.sys.rag.config;

import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatMemoryConfig {

    @Bean
    public ChatMemory chatMemory() {
        return MessageWindowChatMemory.builder()
                // 1. 指定存储实现：这里用内存存储
                .chatMemoryRepository(new InMemoryChatMemoryRepository())
                // 2. 设置保留最近多少条消息（默认20条，这里设10条）
                .maxMessages(10)
                .build();
    }
}