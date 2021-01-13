package ru.otus.projectwork.localsignservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.otus.projectwork.localsignservice.service.SignService;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Slf4j
public class SignController {
    private final SignService signService;

    @PostMapping(value = "/sign", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> doSign(@RequestParam("file") MultipartFile data) {
        try {
            final Optional<String> sign = signService.doSign(data.getBytes());
            return sign.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
        } catch (IOException e) {
            log.error("", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }
}
