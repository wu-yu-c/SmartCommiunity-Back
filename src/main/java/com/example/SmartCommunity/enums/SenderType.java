package com.example.SmartCommunity.enums;

import lombok.Getter;

@Getter
public enum SenderType {
    USER((byte) 1),
    AI((byte) 2);

    private final byte code;

    SenderType(byte code) {
        this.code = code;
    }

}
