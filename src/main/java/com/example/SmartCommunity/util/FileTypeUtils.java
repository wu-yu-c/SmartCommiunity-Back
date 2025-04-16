package com.example.SmartCommunity.util;

public class FileTypeUtils {
    public static boolean isImage(String contentType) {
        return contentType != null && contentType.startsWith("image/");
    }

    public static boolean isVideo(String contentType) {
        return contentType != null && contentType.startsWith("video/");
    }
}

