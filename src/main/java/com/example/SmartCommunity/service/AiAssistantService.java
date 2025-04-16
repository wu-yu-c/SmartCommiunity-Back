package com.example.SmartCommunity.service;

import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.alibaba.dashscope.exception.UploadFileException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

public interface AiAssistantService {
    @Transactional
    Object processMultiModalInput(Long SessionId, Long userId, String text, MultipartFile file)
            throws NoApiKeyException, UploadFileException, ApiException, InputRequiredException;
}
