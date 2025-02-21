package com.server.delivery.domain.ai.controller;

import com.server.delivery.domain.ai.service.AiService;
import com.server.delivery.domain.ai.service.AiServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ai")
public class AiController {

    private final AiService aiService;



    @GetMapping("/generate")
    public String generateContent(@RequestParam String content) {

        return aiService.generateContent(content);
    }
}
