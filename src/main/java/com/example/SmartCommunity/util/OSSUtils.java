package com.example.SmartCommunity.util;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.common.auth.CredentialsProviderFactory;
import com.aliyun.oss.common.auth.EnvironmentVariableCredentialsProvider;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.OSSObjectSummary;
import com.aliyun.oss.model.ObjectListing;
import com.aliyun.oss.model.PutObjectRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class OSSUtils {

    // Endpoint以华东2（上海）为例，其它Region请按实际情况填写。
    private static final String ENDPOINT = "https://oss-cn-shanghai.aliyuncs.com";
    // Bucket名称
    private static final String BUCKET_NAME = "first-textbucket";

    public static String uploadFileToOSS(MultipartFile file, String objectName) throws com.aliyuncs.exceptions.ClientException {
        // 从环境变量中获取访问凭证。
        EnvironmentVariableCredentialsProvider credentialsProvider = CredentialsProviderFactory.newEnvironmentVariableCredentialsProvider();
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(ENDPOINT, credentialsProvider);

        try {
            // 使用文件输入流上传
            ossClient.putObject(new PutObjectRequest(BUCKET_NAME, objectName, file.getInputStream()));
            System.out.println("文件上传成功！");
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

    public static String downloadFileFromOSS(String objectName, String localFilePath) throws com.aliyuncs.exceptions.ClientException {
        // 从环境变量中获取访问凭证。
        EnvironmentVariableCredentialsProvider credentialsProvider = CredentialsProviderFactory.newEnvironmentVariableCredentialsProvider();

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(ENDPOINT, credentialsProvider);

        try {
            // 调用ossClient.getObject返回一个OSSObject实例，该实例包含文件内容及文件元数据。
            OSSObject ossObject = ossClient.getObject(BUCKET_NAME, objectName);
            // 调用ossObject.getObjectContent获取文件输入流，可读取此输入流获取其内容。
            InputStream content = ossObject.getObjectContent();
            if (content != null) {
                try (OutputStream outputStream = new FileOutputStream(localFilePath)) {
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = content.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                }
                // 数据读取完成后，获取的流必须关闭，否则会造成连接泄漏，导致请求无连接可用，程序无法正常工作。
                content.close();
                return "success";
            } else
                return "null";
        } catch (OSSException oe) {
            System.out.println("OSS异常: " + oe.getErrorMessage());
            return "null"; // OSS错误，返回failure
        } catch (ClientException ce) {
            System.out.println("客户端异常: " + ce.getMessage());
            return "failure"; // 客户端错误，返回failure
        } catch (IOException e) {
            e.printStackTrace();
            return "failure"; // 读取文件时发生错误，返回failure
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }

    public static List<String> listFile() throws Exception {

        EnvironmentVariableCredentialsProvider credentialsProvider = CredentialsProviderFactory.newEnvironmentVariableCredentialsProvider();
        List<String> fileInfoList = new ArrayList<>(); // 用于保存文件信息
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(ENDPOINT, credentialsProvider);

        try {
            // ossClient.listObjects返回ObjectListing实例，包含此次listObject请求的返回结果。
            ObjectListing objectListing = ossClient.listObjects(BUCKET_NAME);
            // objectListing.getObjectSummaries获取所有文件的描述信息。
            for (OSSObjectSummary objectSummary : objectListing.getObjectSummaries()) {
                String fileUrl = getFileUrlFromOSS(objectSummary.getKey());
                String fileInfo = objectSummary.getKey() + " (size = " + objectSummary.getSize() + "KB), " +
                        "url = " + fileUrl;
                fileInfoList.add(fileInfo); // 将文件信息添加到列表中
            }
            return fileInfoList; // 返回文件信息列表
        } catch (OSSException oe) {
            System.out.println("OSS异常: " + oe.getErrorMessage());
            return null; // OSS错误，返回null
        } catch (ClientException ce) {
            System.out.println("客户端异常: " + ce.getMessage());
            return null; // 客户端错误，返回null
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }

    public static String getFileUrlFromOSS(String fileName) throws com.aliyuncs.exceptions.ClientException {
        EnvironmentVariableCredentialsProvider credentialsProvider = CredentialsProviderFactory.newEnvironmentVariableCredentialsProvider();

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(ENDPOINT, credentialsProvider);

        try {
            // 如果文件存在，构造并返回文件的 URL
            boolean fileExists = ossClient.doesObjectExist(BUCKET_NAME, fileName);
            if (fileExists) {
                // 生成文件的 URL，假设使用了 OSS 的公开读访问
                return "https://" + BUCKET_NAME + "." + "oss-cn-shanghai.aliyuncs.com/" + fileName;
            } else {
                return null; // 文件不存在
            }
        } catch (OSSException oe) {
            System.out.println("OSS异常: " + oe.getErrorMessage());
            return null; // 发生错误返回 null
        } catch (ClientException ce) {
            System.out.println("客户端异常: " + ce.getMessage());
            return null; // 发生错误返回 null
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }

    public static void deleteFile(String fileName) throws com.aliyuncs.exceptions.ClientException {
        EnvironmentVariableCredentialsProvider credentialsProvider = CredentialsProviderFactory.newEnvironmentVariableCredentialsProvider();

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(ENDPOINT, credentialsProvider);
        try {
            ossClient.deleteObject(BUCKET_NAME, fileName);
        } catch (OSSException oe) {
            System.out.println("OSS异常: " + oe.getErrorMessage());
        } catch (ClientException ce) {
            System.out.println("客户端异常: " + ce.getMessage());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }
}