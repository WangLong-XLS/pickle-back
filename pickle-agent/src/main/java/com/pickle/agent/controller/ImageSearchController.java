package com.pickle.agent.controller;

import com.pickle.agent.service.impl.ImageVectorService;
import com.pickle.utils.exception.BizException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

//@RestController
@Slf4j
@RequiredArgsConstructor
public class ImageSearchController {
    

    private final ImageVectorService imageVectorService;


    /**
     * 上传图片并存储到向量库
     * 流程：保存文件 -> 生成描述 -> 存储向量
     */
    @PostMapping("/upload")
    public Map<String, Object> uploadImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "description", required = false) String userDescription) {

        try {
            // 1. 验证文件类型
            if (!isImageFile(file)) {
                throw new BizException("只支持图片文件（jpg、png、jpeg、gif）");
            }

            // 2. 保存文件到本地
//            String savedPath = saveFile(file);
            String savedPath = file.getOriginalFilename();

            // 3. 生成图片描述（如果用户没有提供）
            String description = (userDescription != null && !userDescription.isEmpty())
                    ? userDescription
                    : generateDefaultDescription(file.getOriginalFilename());

            // 4. 存储到向量数据库
            imageVectorService.storeImage(savedPath, description);

            // 5. 返回结果
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "图片上传并存储成功");
            response.put("imagePath", savedPath);
            response.put("description", description);
            response.put("fileSize", file.getSize());

            return response;

        } catch (Exception e) {
            log.error("上传图片失败", e);
            throw new BizException(e.getMessage());
        }
    }

    private boolean isImageFile(MultipartFile file) {
        String contentType = file.getContentType();
        if (contentType == null) return false;

        return contentType.equals("image/jpeg") ||
                contentType.equals("image/jpg") ||
                contentType.equals("image/png") ||
                contentType.equals("image/gif") ||
                contentType.equals("image/bmp");
    }

    private String generateDefaultDescription(String filename) {
        // 根据文件名生成简单描述（可以接入图像识别API生成更准确的描述）
        String nameWithoutExt = filename.contains(".")
                ? filename.substring(0, filename.lastIndexOf("."))
                : filename;

        // 简单处理：移除下划线、连字符
        String description = nameWithoutExt
                .replaceAll("_", " ")
                .replaceAll("-", " ")
                .replaceAll("\\d+", "");  // 移除数字

        return description.isEmpty() ? "未命名图片" : description;
    }
}