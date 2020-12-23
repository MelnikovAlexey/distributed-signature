package ru.otus.projectwork.signservice.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.otus.projectwork.signservice.service.SignService;

import java.util.Optional;

@RequiredArgsConstructor
@RestController
@Slf4j
public class SignServiceController {
    private final SignService signService;

    @PostMapping(value = "/sign/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> doSign(@RequestParam("file") MultipartFile data,
                                    @PathVariable("id") long id,
                                    @RequestParam(name = "pwd", required = false, defaultValue = "") String pwd) {
        log.info("Sign request received. {}", data);
        try {
            final Optional<String> sign = signService.doSign(data, id, pwd);
            return sign.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
