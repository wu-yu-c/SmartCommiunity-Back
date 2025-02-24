package com.example.SmartCommunity.util.impl;

import com.example.SmartCommunity.dto.ChatMessageDTO;
import com.example.SmartCommunity.util.AiResponseGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@Component
public class Qwen2Generator implements AiResponseGenerator {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public ChatMessageDTO generateResponse(ChatMessageDTO message) throws Exception {
        // 创建请求的URL
        URL url = new URL("http://localhost:5001/v1/chat/completions");
        // 建立HTTP连接
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        // 构建请求体
        String requestBody = buildRequestBody(message);
        //假如没有image_url,将其设置为空白图片地址
        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = requestBody.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        // 发送请求并获取响应
        int statusCode = connection.getResponseCode();
        if (statusCode != 200) {
            InputStream errorStream = connection.getErrorStream();
            if (errorStream != null) {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(errorStream))) {
                    StringBuilder errorResponse = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        errorResponse.append(line);
                    }
                    System.err.println("API Error Response: " + errorResponse);
                }
            }
            throw new RuntimeException("HTTP request failed with status code: " + statusCode);
        }

        // 读取响应
        try (InputStreamReader in = new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8);
             BufferedReader br = new BufferedReader(in)) {
            StringBuilder responseBuilder = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                responseBuilder.append(line);
            }

            // 解析JSON响应
            String responseJson = responseBuilder.toString();
            return parseResponse(responseJson);
        }
    }

    private String buildRequestBody(ChatMessageDTO message) throws Exception {
        // 创建 JSON 对象
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode root = mapper.createObjectNode();
        root.put("model", "Qwen2-VL-2B-Instruct");

        ArrayNode messages = mapper.createArrayNode();

        // 添加系统角色
        ObjectNode systemMessage = mapper.createObjectNode();
        systemMessage.put("role", "system");
        if(message.getImage_url().length()==0){
            //System.out.println("上传图片为空白");
            message.setImage_url("https://first-textbucket.oss-cn-shanghai.aliyuncs.com/prompt.png");
            systemMessage.put("content", "你是一个社区工作人员，请你为用户提供热心的服务,忽视图片输入图片中的内容");
        }
        else systemMessage.put("content", "你是一个社区工作人员，请你为用户提供热心的服务。");
        messages.add(systemMessage);

        // 添加用户角色
        ObjectNode userMessage = mapper.createObjectNode();
        userMessage.put("role", "user");

        ArrayNode userContent = mapper.createArrayNode();
        if (message.getImage_url() != null) {
            ObjectNode imageContent = mapper.createObjectNode();
            imageContent.put("type", "image_url");

            ObjectNode imageUrl = mapper.createObjectNode();
            imageUrl.put("url", message.getImage_url());

            imageContent.set("image_url", imageUrl);
            userContent.add(imageContent);
        }

        if (message.getText() != null) {
            ObjectNode textContent = mapper.createObjectNode();
            textContent.put("type", "text");
            textContent.put("text", message.getText());
            userContent.add(textContent);
        }

        userMessage.set("content", userContent);
        messages.add(userMessage);

        root.set("messages", messages);

        return mapper.writeValueAsString(root);
    }

    private ChatMessageDTO parseResponse(String responseJson) throws Exception {
        JsonNode responseNode = objectMapper.readTree(responseJson);

        // 提取Assistant的消息内容
        String assistantMessage = responseNode.path("choices").get(0)
                .path("message").path("content").asText();

        // 构造并返回ChatMessageDTO
        return new ChatMessageDTO("assistant", null, assistantMessage);
    }

}
