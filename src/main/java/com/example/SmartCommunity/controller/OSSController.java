package com.example.SmartCommunity.controller;

import com.aliyuncs.exceptions.ClientException;
import com.example.SmartCommunity.util.OSSUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "OSS操作相关接口")
@RestController
@RequestMapping("/OSS")
public class OSSController {

    @ApiOperation(value = "上传文件到OSS",notes = "填写文件路径和文件保存在OSS中的名称，返回上传结果")
    @PostMapping("/upload")
    public String uploadFileToOSS(
            @RequestParam String localFilePath,
            @RequestParam String objectName) throws ClientException {
        return OSSUtils.uploadFileToOSS(localFilePath, objectName);
    }
}
