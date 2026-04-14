package com.pickle.sys.rag.config;

import com.pickle.sys.rag.DocumentLoaderRag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * 恋爱大师向量数据库配置（初始化基于内存的向量数据库 Bean）
 */
@Slf4j
@Configuration
public class VectorStoreConfig {

    @Resource
    private DocumentLoaderRag documentLoaderRag;

    @Bean
    VectorStore vectorStore(EmbeddingModel embeddingModel) {
        SimpleVectorStore simpleVectorStore = SimpleVectorStore.builder(embeddingModel).build();
        List<Document> documents = documentLoaderRag.loadMarkdowns();
        log.info("加载了 {} 个文档到向量存储", documents.size());
        simpleVectorStore.add(documents);
        return simpleVectorStore;
    }
}
