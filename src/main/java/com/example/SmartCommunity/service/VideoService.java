package com.example.SmartCommunity.service;

import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Map;

public interface VideoService {

    /**
     * 搜索相似视频
     *
     * @param text 查询文本
     * @param k    返回视频个数
     * @return 包含视频名称和相似度的列表
     * @throws Exception 可能抛出的异常
     */
    List<Map<String, Object>> searchVideos(String text, int k) throws Exception;

    /**
     * 上传视频并获取特征
     *
     * @param videoFile 视频文件
     * @return 视频特征向量
     * @throws Exception 可能抛出的异常
     */
    List<Float> embedVideo(MultipartFile videoFile) throws Exception;

    /**
     * 输入文本并获取特征
     *
     * @param text 输入文本
     * @return 文本特征向量
     * @throws Exception 可能抛出的异常
     */
    List<Float> embedText(String text) throws Exception;

    /**
     * 获取视频文件的二进制数据
     *
     * @param filename 视频文件名
     * @return 视频文件的字节数组
     * @throws Exception 可能抛出的异常
     */
    byte[] getVideoData(String filename) throws Exception;

    /**
     * 将视频字节数组转发给 Flask 进行处理
     *
     * @param videoBytes 视频文件的字节数组
     * @param filename   视频文件名
     * @return Flask 服务的响应信息
     * @throws Exception
     */
    String sendVideoToFlask(byte[] videoBytes, String filename) throws Exception;
}
