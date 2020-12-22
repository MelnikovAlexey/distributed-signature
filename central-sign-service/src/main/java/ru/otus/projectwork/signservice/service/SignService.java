package ru.otus.projectwork.signservice.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface SignService {
    Optional<String> doSign(MultipartFile data, long userId, String pwd) throws Exception;

}
