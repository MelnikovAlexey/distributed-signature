package ru.otus.projectwork.localsignservice.service;

import java.util.Optional;

public interface SignService {
    Optional<String> doSign(byte[] data);
}
