package com.example.SmartCommunity.controller;

import com.example.SmartCommunity.service.VideoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Tag(name = "视频接口")
@RestController
@RequestMapping("/Video")
public class VideoController {

    @Autowired
    private VideoService videoService;
    @Autowired
    private RestTemplate restTemplate;

    /**
     * 搜索相似视频
     *
     * @param searchRequest 包含 `text` 和 `k` 的请求体
     * @return 搜索结果
     */
    @Operation(summary = "搜索相似视频")
    @PostMapping("/search")
    public ResponseEntity<?> searchVideos(@RequestBody SearchRequest searchRequest) {
        try {
            List<Map<String, Object>> results = videoService.searchVideos(searchRequest.getText(), searchRequest.getK());

            // 构建基础 URL
            String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();

            // 为每个结果添加视频播放的 URL
            for (Map<String, Object> result : results) {
                String filename = (String) result.get("video");
                String videoUrl = baseUrl + "/Video/videos/" + filename;
                result.put("videoUrl", videoUrl);
            }

            return ResponseEntity.ok(Map.of("results", results));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * 上传视频并获取特征
     *
     * @param videoFile 视频文件
     * @return 视频特征向量
     */
    @Operation(summary = "上传视频并获取特征")
    @PostMapping("/embed/video")
    public ResponseEntity<?> embedVideo(@RequestParam("video") MultipartFile videoFile) {
        try {
            List<Float> embedding = videoService.embedVideo(videoFile);
            return ResponseEntity.ok(Map.of("embedding", embedding));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * 输入文本并获取特征
     *
     * @param textRequest 包含 `text` 的请求体
     * @return 文本特征向量
     */
    @Operation(summary = "输入文本并获取特征")
    @PostMapping("/embed/text")
    public ResponseEntity<?> embedText(@RequestBody TextRequest textRequest) {
        try {
            List<Float> embedding = videoService.embedText(textRequest.getText());
            return ResponseEntity.ok(Map.of("embedding", embedding));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * 获取视频文件的二进制数据
     *
     * @param filename 视频文件名
     * @return 视频文件的字节数组
     */
    @Operation(summary = "获取视频文件")
    @GetMapping("/videos/{filename:.+}")
    public ResponseEntity<byte[]> getVideo(@PathVariable String filename) {
        try {
            byte[] videoData = videoService.getVideoData(filename);

            // 根据文件扩展名确定 Content-Type
            String contentType = "application/octet-stream";
            String extension = FilenameUtils.getExtension(filename).toLowerCase();

            switch (extension) {
                case "mp4":
                    contentType = "video/mp4";
                    break;
                case "avi":
                    contentType = "video/x-msvideo";
                    break;
                case "mov":
                    contentType = "video/quicktime";
                    break;
                case "mkv":
                    contentType = "video/x-matroska";
                    break;
                case "flv":
                    contentType = "video/x-flv";
                    break;
                case "wmv":
                    contentType = "video/x-ms-wmv";
                    break;
                default:
                    // 保持默认 Content-Type
                    break;
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(contentType));
            headers.setContentLength(videoData.length);
            headers.setContentDisposition(ContentDisposition.builder("inline").filename(filename).build());

            return new ResponseEntity<>(videoData, headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * 定义用于搜索请求的内部类
     */
    public static class SearchRequest {
        private String text;
        private int k = 1; // 默认值为1

        // Getters and Setters

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public int getK() {
            return k;
        }

        public void setK(int k) {
            this.k = k;
        }
    }

    /**
     * 定义用于文本特征请求的内部类
     */
    public static class TextRequest {
        private String text;

        // Getters and Setters

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }

    /**
     * 上传视频并将其转发到 Flask 进行处理
     *
     * @param videoFile 上传的视频文件
     * @return 上传状态
     */
    @Operation(summary = "上传视频并转发到 Flask")
    @PostMapping("/upload/video")
    public ResponseEntity<?> uploadVideo(@RequestParam("video") MultipartFile videoFile) {
        try {
            // 将 MultipartFile 转为字节数组
            byte[] videoBytes = videoFile.getBytes();

            // 将视频文件字节数据传递给 Flask 服务进行处理
            String flaskResponse = null;
            try {
                flaskResponse = videoService.sendVideoToFlask(videoBytes, videoFile.getOriginalFilename());
            } catch (Exception e) {
                e.printStackTrace();
            }

            // 返回结果
            return ResponseEntity.ok(Map.of("status", "success", "message", flaskResponse));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }
}
