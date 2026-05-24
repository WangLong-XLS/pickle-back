package com.pickle.agent.service.impl;

import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

//@Service
public class ImageVectorService {

    @Autowired
    private VectorStore vectorStore;  // 你现有的向量存储

    /**
     * 存储图片：生成描述文本并向量化
     */
    public void storeImage(String imagePath, String imageDescription) {
        // 1. 构建包含图片路径和描述的 Document
        String content = String.format("图片路径: %s\n图片内容: %s",
                imagePath, imageDescription);

        Document doc = new Document(
                content,
                Map.of(
                        "type", "image",
                        "image_path", imagePath,
                        "description", imageDescription,
                        "timestamp", LocalDateTime.now().toString()
                )
        );

        // 2. 复用现有的 vectorStore.add() 方法
        //    自动调用你的 EmbeddingModel 生成文本向量并存储
        vectorStore.add(List.of(doc));
    }

    /**
     * 用文字描述搜索图片
     */
    public List<Document> searchImagesByText(String queryText, int topK) {
        return vectorStore.similaritySearch(
                SearchRequest.builder()
                        .query(queryText)
                        .topK(topK)
                        .filterExpression("type == 'image'")  // 只搜索图片
                        .build()
        );
    }
}