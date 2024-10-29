package com.example.SmartCommunity.util;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.common.auth.CredentialsProviderFactory;
import com.aliyun.oss.common.auth.EnvironmentVariableCredentialsProvider;
import java.io.File;

public class OSSUtils {

    // Endpoint以华东2（上海）为例，其它Region请按实际情况填写。
    private static final String ENDPOINT = "https://oss-cn-shanghai.aliyuncs.com";
    // Bucket名称
    private static final String BUCKET_NAME = "first-tekcub";

    /**
     * 上传文件到OSS
     *
     * @param localFilePath 本地文件路径
     * @param objectName    OSS中的Object路径，Object路径中不能包含bucket名称
     * @return 上传成功返回 "success"，失败返回 "failure"
     */
    public static String uploadFileToOSS(String localFilePath, String objectName) throws com.aliyuncs.exceptions.ClientException {
        // 从环境变量中获取访问凭证。
        EnvironmentVariableCredentialsProvider credentialsProvider = CredentialsProviderFactory.newEnvironmentVariableCredentialsProvider();
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(ENDPOINT, credentialsProvider);

        try {
            // 上传文件
            ossClient.putObject(BUCKET_NAME, objectName, new File(localFilePath));
            System.out.println("图片上传成功！");
            return "success";
        } catch (OSSException oe) {
            System.err.println("OSSException: 请求被OSS拒绝");
            System.err.println("Error Message: " + oe.getErrorMessage());
            System.err.println("Error Code: " + oe.getErrorCode());
            System.err.println("Request ID: " + oe.getRequestId());
            System.err.println("Host ID: " + oe.getHostId());
            return "failure";
        } catch (ClientException ce) {
            System.err.println("ClientException: 客户端遇到严重问题");
            System.err.println("Error Message: " + ce.getMessage());
            return "failure";
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }

}