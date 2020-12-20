package ru.otus.projectwork.signservice.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.projectwork.signservice.service.LocalSignInteractionService;

@RequiredArgsConstructor
@RestController
@Slf4j
public class LocalSignInteractionController {
    private final LocalSignInteractionService service;

    @PostMapping("/connect")
    public void connect(@RequestBody String tokenInfo, @RequestHeader("serviceName") String serviceName) {
        service.serviceConnected(tokenInfo, serviceName);
        log.info("Service {} connected with token {}", serviceName, tokenInfo);
    }

    @PostMapping("/disconnect")
    public void disconnect(@RequestHeader("serviceName") String serviceName) {
        service.serviceDisconnected(serviceName);
        log.info("Service {} disconnected", serviceName);
    }
}
