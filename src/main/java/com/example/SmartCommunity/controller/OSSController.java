package com.example.SmartCommunity.controller;

import com.aliyuncs.exceptions.ClientException;
import com.example.SmartCommunity.util.OSSUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpStatus;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Tag(name = "OSS操作相关接口")
@RestController
@RequestMapping("/OSS")
public class OSSController {

    @Operation(summary = "上传文件到OSS", description = "上传本地文件并保存到OSS，返回上传结果")
    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public ResponseEntity<Map<String, Object>> uploadFileToOSS(@RequestParam("file")MultipartFile file) throws ClientException {
        // 检查文件是否为空
        if (file.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("code", HttpStatus.BAD_REQUEST.value());
            response.put("message", "文件不能为空");
            return ResponseEntity.badRequest().body(response);
        }

        // 使用 UUID 生成唯一文件名
        String originalFileName = file.getOriginalFilename();
        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String uniqueFileName = UUID.randomUUID().toString() + fileExtension;

        // 上传逻辑调用
        String result = OSSUtils.uploadFileToOSS(file, uniqueFileName);
        Map<String, Object> response = new HashMap<>();
        if ("success".equals(result)) {
            String fileUrl = "https://first-tekcub.oss-cn-shanghai.aliyuncs.com/"+ uniqueFileName;
            response.put("code", HttpStatus.OK.value());
            response.put("message", "文件上传成功");
            response.put("fileUrl", fileUrl);
            return ResponseEntity.ok(response);
        } else {
            response.put("code", HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.put("message", "文件上传失败");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}

