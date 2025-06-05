package com.example.SmartCommunity.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.alibaba.dashscope.exception.UploadFileException;
import com.example.SmartCommunity.common.response.ApiResponse;
import com.example.SmartCommunity.service.ChatSessionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.SmartCommunity.service.AiAssistantService;
import org.springframework.web.multipart.MultipartFile;

@Tag(name="AI助手接口")
@RestController
@RequestMapping("/api/ai-assistant")
public class AiAssistantController {

    private final AiAssistantService aiAssistantService;
    private final ChatSessionService chatSessionService;
    @Autowired
    public AiAssistantController(AiAssistantService aiAssistantService, ChatSessionService chatSessionService) {
        this.aiAssistantService = aiAssistantService;
        this.chatSessionService = chatSessionService;
    }

    @Operation(summary = "AI对话接口",description = "上传的图像大小不得超过10MB，上传的视频时长不得超过40s。" +
            "使用swagger测试时，不传入sessionId默认会创建一个新的session，所以想使用多轮对话必须在第二次对话时传入sessionId。" +
            "此外，由于免费额度有限，多轮对话只会读取该session下的最近10条消息记录，太久远的消息会被忽略。")
    @SaCheckLogin
    @PostMapping(value = "/chat", consumes = "multipart/form-data")
    public ResponseEntity<ApiResponse<?>> chat(@RequestParam(value = "sessionId",required = false) Long sessionId,
            @RequestParam("text") String text,
            @RequestParam(value = "file", required = false) MultipartFile file)
            throws NoApiKeyException, UploadFileException, ApiException {
        Long userId = StpUtil.getLoginIdAsLong();
        Object response;
        try {
            response = aiAssistantService.processMultiModalInput(sessionId, userId, text, file);
        } catch (com.alibaba.dashscope.exception.InputRequiredException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(HttpStatus.OK, "获取AI助手响应成功", response));
    }

    @Operation(summary = "通过会话Id删除会话",description = "删除会话后，该会话下的所有message和在OSS中存储的图片/视频都会被同步删除")
    @SaCheckLogin
    @DeleteMapping("/session_id")
    public ResponseEntity<ApiResponse<Void>> deleteSession(
            @RequestParam(value = "sessionId",required = false) Long sessionId) {
        chatSessionService.deleteSessionById(sessionId);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(HttpStatus.OK,"删除会话成功",null));
    }
}
