package com.example.SmartCommunity.service.impl;

import com.example.SmartCommunity.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

@Service
public class VideoServiceImpl implements VideoService {

    // Flask 后端的基础 URL，根据实际情况修改
    @Value("${flask.base.url}")
    private String flaskBaseUrl;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public List<Map<String, Object>> searchVideos(String text, int k) throws Exception {
        String url = flaskBaseUrl + "/search";

        // 构建请求体
        Map<String, Object> requestBody = Map.of(
                "text", text,
                "k", k
        );

        // 设置请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        // 发送 POST 请求
        ResponseEntity<Map> response = restTemplate.postForEntity(url, entity, Map.class);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            // 假设 Flask 返回的 JSON 结构是 { "results": [ {...}, {...} ] }
            List<Map<String, Object>> results = (List<Map<String, Object>>) response.getBody().get("results");
            return results;
        } else {
            throw new Exception("Failed to search videos: " + response.getStatusCode());
        }
    }

    @Override
    public List<Float> embedVideo(MultipartFile videoFile) throws Exception {
        String url = flaskBaseUrl + "/embed/video";

        // 构建 multipart 请求体
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("video", getMultipartFileResource(videoFile));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        // 发送 POST 请求
        ResponseEntity<Map> response = restTemplate.postForEntity(url, requestEntity, Map.class);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            // 假设 Flask 返回的 JSON 结构是 { "embedding": [ ... ] }
            List<Double> embeddingDoubles = (List<Double>) response.getBody().get("embedding");
            // 将 List<Double> 转换为 List<Float>
            List<Float> embeddingFloats = embeddingDoubles.stream()
                    .map(Double::floatValue)
                    .toList();
            return embeddingFloats;
        } else {
            throw new Exception("Failed to embed video: " + response.getStatusCode());
        }
    }

    @Override
    public List<Float> embedText(String text) throws Exception {
        String url = flaskBaseUrl + "/embed/text";

        // 构建请求体
        Map<String, Object> requestBody = Map.of(
                "text", text
        );

        // 设置请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        // 发送 POST 请求
        ResponseEntity<Map> response = restTemplate.postForEntity(url, entity, Map.class);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            // 假设 Flask 返回的 JSON 结构是 { "embedding": [ ... ] }
            Object embeddingObj = response.getBody().get("embedding");
            if (embeddingObj instanceof List<?>) {
                List<?> embeddingList = (List<?>) embeddingObj;
                // 检查是 List<Double> 还是 List<List<Double>>
                if (!embeddingList.isEmpty() && embeddingList.get(0) instanceof List<?>) {
                    // 多个文本输入
                    List<List<Double>> embeddingsDoubles = (List<List<Double>>) embeddingObj;
                    // 转换为 List<List<Float>>
                    List<List<Float>> embeddingsFloats = embeddingsDoubles.stream()
                            .map(list -> list.stream()
                                    .map(o -> ((Number) o).floatValue())
                                    .toList())
                            .toList();
                    // 这里只返回第一个文本的特征向量
                    if (!embeddingsFloats.isEmpty()) {
                        return embeddingsFloats.get(0);
                    }
                } else {
                    // 单个文本输入
                    List<Double> embeddingDoubles = (List<Double>) embeddingObj;
                    // 转换为 List<Float>
                    List<Float> embeddingFloats = embeddingDoubles.stream()
                            .map(Double::floatValue)
                            .toList();
                    return embeddingFloats;
                }
            }
        }

        throw new Exception("Failed to embed text: " + response.getStatusCode());
    }

    @Override
    public byte[] getVideoData(String filename) throws Exception {
        String url = flaskBaseUrl + "/videos/" + filename;

        // 设置接受的媒体类型
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.ALL));

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        // 发送 GET 请求
        ResponseEntity<byte[]> response = restTemplate.exchange(url, HttpMethod.GET, entity, byte[].class);

        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else {
            throw new Exception("Failed to retrieve video data: " + response.getStatusCode());
        }
    }

    /**
     * 将 MultipartFile 转换为 Spring 的 Resource
     *
     * @param file 上传的文件
     * @return Resource 对象
     * @throws Exception 可能抛出的异常
     */
    private org.springframework.core.io.Resource getMultipartFileResource(MultipartFile file) throws Exception {
        try {
            return new org.springframework.core.io.ByteArrayResource(file.getBytes()) {
                @Override
                public String getFilename() {
                    return file.getOriginalFilename();
                }
            };
        } catch (Exception e) {
            throw new Exception("Failed to read multipart file", e);
        }
    }

    @Override
    public String sendVideoToFlask(byte[] videoBytes, String filename) throws Exception {
        String flaskUrl = flaskBaseUrl + "/upload/video";

        // 创建一个 ByteArrayResource 包装视频文件字节数据
        ByteArrayResource videoResource = new ByteArrayResource(videoBytes) {
            @Override
            public String getFilename() {
                return filename;
            }
        };

        // 构建 multipart 请求体
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("video", videoResource);  // 添加视频文件

        // 设置请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        // 构建请求体
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        // 发送 POST 请求
        ResponseEntity<String> response = restTemplate.exchange(flaskUrl, HttpMethod.POST, requestEntity, String.class);

        // 根据响应结果进行处理
        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();  // 返回 Flask 的响应内容
        } else {
            throw new Exception("Flask 服务处理失败：" + response.getStatusCode());
        }
    }

}
