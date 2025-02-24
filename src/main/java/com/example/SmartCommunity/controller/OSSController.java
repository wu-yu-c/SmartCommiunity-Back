package com.example.SmartCommunity.controller;

import com.aliyuncs.exceptions.ClientException;
import com.example.SmartCommunity.util.OSSUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpStatus;

import java.util.*;

@Tag(name = "OSS操作相关接口")
@RestController
@RequestMapping("/OSS")
public class OSSController {

    @Operation(summary = "上传文件到OSS", description = "上传本地文件并保存到OSS，返回上传结果")
    @PostMapping(value = "api/upload", consumes = "multipart/form-data")
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
            String fileUrl = "https://first-textbucket.oss-cn-shanghai.aliyuncs.com/"+ uniqueFileName;
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

    @Operation(summary = "从OSS下载文件", description = "输入文件的名称，从OSS下载文件")
    @GetMapping("api/download")
    public ResponseEntity<Map<String,Object>> downloadFile(
            @RequestParam String objectName, // 必需参数：要下载的文件名
            @RequestParam(defaultValue = ".") String localPath) throws ClientException { // 可选参数：保存路径，默认为当前文件夹
        Map<String, Object> response = new HashMap<>();
        // 拼接文件保存路径
        String localFilePath = localPath + "/" + objectName; // 使用 '/' 作为路径分隔符

        String result = OSSUtils.downloadFileFromOSS(objectName, localFilePath);

        if ("success".equals(result)) {
            response.put("code", 200);
            response.put("message", "File downloaded successfully to " + localFilePath);
            return ResponseEntity.ok(response);
        } else if ("null".equals(result)) {
            response.put("code", 404);
            response.put("message", "未找到该文件");
            return ResponseEntity.status(404).body(response);
        } else {
            response.put("code", 500);
            response.put("message", "Failed to download file.");
            return ResponseEntity.status(500).body(response);
        }
    }

    @Operation(summary = "列举OSS中的文件")
    @GetMapping("/api/listFiles")
    public ResponseEntity<Map<String,Object>> getFiles() {
        Map<String ,Object> response=new HashMap<>();
        try {
            List<String> files = OSSUtils.listFile(); // 调用列举文件的函数
            if (files != null) {
                response.put("code",200);
                response.put("message","success");
                response.put("files",files);
                return ResponseEntity.ok(response); // 返回成功的文件列表
            } else {
                response.put("code", 404);
                response.put("message", "bucket中无文件");
                return ResponseEntity.status(404).body(response); // 返回失败信息
            }
        } catch (Exception e) {
            response.put("code", 500);
            response.put("message", "Error: " + e.getMessage());
            return ResponseEntity.status(500).body(response);  // 返回异常信息
        }
    }

    @Operation(summary = "通过文件名查找文件")
    @GetMapping("/api/searchFile")
    public ResponseEntity<Map<String, Object>> getFileUrl(@RequestParam String fileName) {
        Map<String, Object> response = new HashMap<>();
        try {
            String fileUrl = OSSUtils.getFileUrlFromOSS(fileName); // 调用获取文件 URL 的函数
            if (fileUrl != null) {
                response.put("code", 200);
                response.put("message", "Success");
                response.put("fileUrl", fileUrl);
                return ResponseEntity.ok(response); // 返回成功响应
            } else {
                response.put("code", 404);
                response.put("message", "未找到该文件.");
                return ResponseEntity.status(404).body(response); // 文件未找到
            }
        } catch (Exception e) {
            response.put("code", 500);
            response.put("message", "Error: " + e.getMessage());
            return ResponseEntity.status(500).body(response); // 返回异常信息
        }
    }

    @Operation(summary = "通过文件的名称删除文件")
    @PostMapping("api/deleteFile")
    public ResponseEntity<Map<String, Object>> deleteFile(@RequestParam String fileName) {
        Map<String, Object> response = new HashMap<>();
        try {
            String fileUrl = OSSUtils.getFileUrlFromOSS(fileName);
            if (fileUrl != null) {
                OSSUtils.deleteFile(fileName);
                response.put("code", 200);
                response.put("message", "删除成功.");
                return ResponseEntity.ok(response);
            } else {
                response.put("code", 404);
                response.put("message", "未找到对应文件.");
                return ResponseEntity.status(404).body(response);
            }
        } catch (Exception e) {
            response.put("code", 500);
            response.put("message", "Error: " + e.getMessage());
            return ResponseEntity.status(500).body(response); // 返回异常信息
        }
    }
}

