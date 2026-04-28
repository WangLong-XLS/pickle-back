package com.pickle.sys.controller;

import com.pickle.sys.service.IDocumentVectorService;
import com.pickle.utils.exception.BizException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/vector")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "向量存储", description = "文档向量化存储相关接口")
public class DocumentVectorController {

    private final IDocumentVectorService documentVectorService;

    /**
     * 上传文档并向量化存储
     */

    @RequestMapping("/uploadDocumentSave")
    @Operation(summary = "上传文档并向量化存储")
    public void uploadDocumentSave(@RequestParam("file") MultipartFile file) {

        // 验证文件
        if (file == null || file.isEmpty()) {
            throw new BizException("文件不能为空");
        }

        // 验证文件大小（限制 50MB）
        if (file.getSize() > 50 * 1024 * 1024) {
            throw new BizException("文件大小不能超过 50MB");
        }

        try {
            log.info("开始处理文件上传: fileName={}, size={}", file.getOriginalFilename(), file.getSize());

            int chunkCount = documentVectorService.vectorDocumentSave(file);

            log.info("文件处理完成: {}, 分块数: {}", file.getOriginalFilename(), chunkCount);

        } catch (Exception e) {
            log.error("文档处理失败", e);
            throw new BizException("文档处理失败: " + e.getMessage());
        }
    }
}