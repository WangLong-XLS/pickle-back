package com.pickle.agent.service.impl;


import com.pickle.agent.rag.MarkdownHierarchicalSplitter;
import com.pickle.agent.service.IDocumentVectorService;
import com.pickle.utils.uuid.UUIDUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.InputStreamResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
//@Service
@RequiredArgsConstructor
@Slf4j
public class DocumentVectorService implements IDocumentVectorService {

    private final VectorStore vectorStore;
    private final MarkdownHierarchicalSplitter markdownHierarchicalSplitter;

    @Override
    public int vectorDocumentSave(MultipartFile file) {
        String originalFileName = file.getOriginalFilename();
        log.info("开始处理文件: {}", originalFileName);

        try {
            // 1. 使用 TikaDocumentReader 读取文件内容
            TikaDocumentReader reader = new TikaDocumentReader(new InputStreamResource(file.getInputStream()));
            List<Document> documents = reader.get();
            log.info("读取完成，共 {} 个原始文档", documents.size());

            if (documents.isEmpty()) {
                log.warn("文件内容为空，无法处理");
                return 0;
            }

            // 2. 添加自定义元数据（现在 metadata 是可变 Map，可以直接修改）
            String docId = UUIDUtil.newUUID();

            for (Document doc : documents) {
                Map<String, Object> metadata = doc.getMetadata();
                metadata.put("docId", docId);
                metadata.put("docName", originalFileName);
                metadata.put("category", "情感类");
                metadata.put("originalFileName", originalFileName);
                metadata.put("fileSize", file.getSize());
                metadata.put("uploadTime", System.currentTimeMillis());
                metadata.put("fileType", getFileExtension(originalFileName));
            }

            log.info("元数据添加完成，文档ID: {}", docId);

            // 3. 文本分块（使用 Builder 模式，更清晰）
            List<Document> chunkedDocs = markdownHierarchicalSplitter.apply(documents);
            log.info("分块完成，共 {} 个文档块", chunkedDocs.size());

            // 4. 存入向量数据库（自动调用 EmbeddingModel 生成向量并存储）
            vectorStore.add(chunkedDocs);
            log.info("向量存储完成，文档ID: {}, 文档块数量: {}", docId, chunkedDocs.size());

            return chunkedDocs.size();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取文件扩展名
     */
    private String getFileExtension(String fileName) {
        if (fileName == null || fileName.lastIndexOf(".") == -1) {
            return "unknown";
        }
        return fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
    }
}