package com.spring.ecommerce.mongodb.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class WebhookController {
    @PostMapping("/webhook-endpoint")
    public void handleWebhook(@RequestBody String payload) {
        System.out.println("Received webhook: " + payload);

        // Gọi script deploy.sh với quyền quản trị viên
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("sudo", "/home/workspace/deploy.sh");
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();
            // Xử lý đầu ra nếu cần
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
