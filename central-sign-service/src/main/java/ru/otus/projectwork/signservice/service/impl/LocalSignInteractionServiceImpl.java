package ru.otus.projectwork.signservice.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.projectwork.signservice.service.LocalSignInteractionService;

@Service
@Slf4j
public class LocalSignInteractionServiceImpl implements LocalSignInteractionService {
    @Override
    public void serviceConnected(String tokenInfo, String serviceName) {

    }

    @Override
    public void serviceDisconnected(String serviceName) {

    }
}
