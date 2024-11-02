package com.example.SmartCommunity.util.impl;

import com.example.SmartCommunity.util.AiResponseGenerator;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class RandomResponseGenerator implements AiResponseGenerator {
    private static final String[] GREETINGS = {
            "Hello! How can I help you today?",
            "Hi! What can I do for you?",
            "Hey! How can I assist you today?",
            "Hello! What can I help you with today?",
            "Hi! How can I assist you today?",
            "Hey! What can I do for you today?"
    };

    @Override
    public String generateResponse(String message) {
        return GREETINGS[new Random().nextInt(GREETINGS.length)];
    }
}