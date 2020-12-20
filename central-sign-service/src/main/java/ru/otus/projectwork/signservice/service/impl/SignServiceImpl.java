package ru.otus.projectwork.signservice.service.impl;

import org.springframework.web.multipart.MultipartFile;
import ru.otus.projectwork.signservice.service.SignService;

import java.util.Optional;

public class SignServiceImpl implements SignService {
    @Override
    public Optional<byte[]> doSign(MultipartFile data, long userId, String pwd) throws Exception {
        return Optional.empty();
    }
}
