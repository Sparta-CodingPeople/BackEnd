package com.server.delivery.domain.ai.service;


import com.server.delivery.model.user.entity.User;

public interface AiService {

    String generateContent(String content, User user);
}
