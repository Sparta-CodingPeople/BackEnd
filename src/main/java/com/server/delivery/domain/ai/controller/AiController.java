package com.server.delivery.domain.ai.controller;

import com.server.delivery.domain.ai.service.AiService;
import com.server.delivery.model.user.entity.User;
import com.server.delivery.util.helper.UserHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ai")
public class AiController {

    private final AiService aiService;
    private final UserHelper userHelper;

    @PostMapping("/generate")
    public String generateContent(@RequestBody Map<String, Object> requestData,
                                  @AuthenticationPrincipal UserDetails userDetails) {

          User user = userHelper.getUser(userDetails.getUsername());
         //User user = userHelper.getUserById(5L);

        if (user == null) {
            throw new IllegalArgumentException("유효한 사용자 정보가 없습니다.");
        }

        String content = (String) requestData.get("content");

        return aiService.generateContent(content, user);
    }

}
