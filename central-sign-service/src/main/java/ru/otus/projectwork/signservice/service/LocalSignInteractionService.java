package ru.otus.projectwork.signservice.service;

public interface LocalSignInteractionService {
    void serviceConnected(String tokenInfo, String serviceName);

    void serviceDisconnected(String serviceName);
}
