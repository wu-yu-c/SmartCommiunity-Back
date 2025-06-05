package com.example.SmartCommunity.util;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.common.auth.CredentialsProviderFactory;
import com.aliyun.oss.common.auth.EnvironmentVariableCredentialsProvider;
import com.aliyun.oss.model.PutObjectRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

public class OSSUtils {

    // Endpoint以华东2（上海）为例，其它Region请按实际情况填写。
    private static final String ENDPOINT = "https://oss-cn-shanghai.aliyuncs.com";
    // Bucket名称
    private static final String BUCKET_NAME = "1st-bucket";

    public static void uploadFileToOSS(MultipartFile file, String objectName) {
        try {
            // 从环境变量中获取访问凭证。
            EnvironmentVariableCredentialsProvider credentialsProvider = CredentialsProviderFactory.newEnvironmentVariableCredentialsProvider();
            // 创建OSSClient实例。
            OSS ossClient = new OSSClientBuilder().build(ENDPOINT, credentialsProvider);

            try {
                // 使用文件输入流上传
                ossClient.putObject(new PutObjectRequest(BUCKET_NAME, objectName, file.getInputStream()));
            } finally {
                if (ossClient != null)
                    ossClient.shutdown();
            }
        } catch (OSSException | IOException | com.aliyuncs.exceptions.ClientException e) {
            throw new RuntimeException("上传文件至 OSS 失败：", e);
        }
    }

    public static void deleteFile(String fileName) {
        try {
            EnvironmentVariableCredentialsProvider credentialsProvider =
                    CredentialsProviderFactory.newEnvironmentVariableCredentialsProvider();
            OSS ossClient = new OSSClientBuilder().build(ENDPOINT, credentialsProvider);
            try {
                ossClient.deleteObject(BUCKET_NAME, fileName);
            } finally {
                if(ossClient != null)
                    ossClient.shutdown();
            }
        } catch (ClientException | com.aliyuncs.exceptions.ClientException e) {
            throw new RuntimeException("从 OSS 删除文件失败：", e);
        }
    }
}