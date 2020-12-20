package ru.otus.projectwork.signservice.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.projectwork.signservice.domain.ServiceToken;
import ru.otus.projectwork.signservice.repository.ServiceTokenRepository;
import ru.otus.projectwork.signservice.service.LocalSignInteractionService;

import java.util.Optional;

@Service
@Slf4j
public class LocalSignInteractionServiceImpl implements LocalSignInteractionService {
    private final ServiceTokenRepository serviceRepository;

    public LocalSignInteractionServiceImpl(ServiceTokenRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    @Override
    public void serviceConnected(String tokenInfo, String serviceName) {
        Optional<ServiceToken> serviceToken = serviceRepository.findByServiceName(serviceName);
        serviceToken.ifPresent(serviceRepository::delete);
        serviceRepository.add(new ServiceToken(serviceName, tokenInfo));
        log.info("Service {} registered with token {}", serviceName, tokenInfo);
    }

    @Override
    public void serviceDisconnected(String serviceName) {
        Optional<ServiceToken> serviceToken = serviceRepository.findByServiceName(serviceName);
        serviceToken.ifPresent(serviceRepository::delete);
        log.info("Service {} unregistered", serviceName);
    }
}
