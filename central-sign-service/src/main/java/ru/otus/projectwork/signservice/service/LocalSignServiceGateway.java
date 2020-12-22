package ru.otus.projectwork.signservice.service;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface LocalSignServiceGateway {
    @PostMapping(value = "/sign", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    Optional<String> doSign(MultipartFile file);

    @GetMapping("/info")
    String requestToken();
}
