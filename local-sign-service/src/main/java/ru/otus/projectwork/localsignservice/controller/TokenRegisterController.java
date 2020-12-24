package ru.otus.projectwork.localsignservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.projectwork.localsignservice.components.TokenInfo;
import ru.otus.projectwork.localsignservice.service.SignServiceGateway;


import static java.util.Objects.isNull;

@RestController
@RequiredArgsConstructor
@Slf4j
public class TokenRegisterController {
    private final SignServiceGateway serviceGateway;
    private final TokenInfo tokenInfo;

    /**
     * Метод - для подключения токена.
     */
    @PostMapping("/connect")
    public void connect(@RequestBody String tokenId) {
        log.info("Token connected {}", tokenId);
        tokenInfo.setTokenInfo(tokenId);
        serviceGateway.connect(tokenId);
    }

    /**
     * Метод - для отключения токена.
     */
    @PostMapping("/disconnect")
    public void disconnect() {
        tokenInfo.clearToken();
        serviceGateway.disconnect();
    }

    @GetMapping("/info")
    public String getToken() {
        final String token = tokenInfo.getToken();
        return isNull(token) ? "" : token;
    }
}
