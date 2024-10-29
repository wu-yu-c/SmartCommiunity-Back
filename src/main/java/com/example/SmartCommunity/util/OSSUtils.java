package com.example.SmartCommunity.util;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.common.auth.CredentialsProviderFactory;
import com.aliyun.oss.common.auth.EnvironmentVariableCredentialsProvider;
import com.aliyun.oss.model.PutObjectRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public class OSSUtils {

    // Endpoint以华东2（上海）为例，其它Region请按实际情况填写。
    private static final String ENDPOINT = "https://oss-cn-shanghai.aliyuncs.com";
    // Bucket名称
    private static final String BUCKET_NAME = "first-tekcub";

    public static String uploadFileToOSS(MultipartFile file, String objectName) throws com.aliyuncs.exceptions.ClientException {
        // 从环境变量中获取访问凭证。
        EnvironmentVariableCredentialsProvider credentialsProvider = CredentialsProviderFactory.newEnvironmentVariableCredentialsProvider();
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(ENDPOINT, credentialsProvider);

        try {
            // 使用文件输入流上传
            ossClient.putObject(new PutObjectRequest(BUCKET_NAME, objectName, file.getInputStream()));
            System.out.println("图片上传成功！");
            return "success";
        } catch (OSSException | IOException oe) {
            System.err.println("上传失败，错误信息: " + oe.getMessage());
            return "failure";
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }
}