package com.example.SmartCommunity.enums;

import lombok.Getter;

@Getter
public enum ContentType {
    TEXT((byte) 1),
    IMAGE((byte) 2),
    VIDEO((byte) 3);

    private final byte code;

    ContentType(byte code) {
        this.code = code;
    }

}

