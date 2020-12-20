package ru.otus.projectwork.signservice.repository.impl;

import org.springframework.stereotype.Service;
import ru.otus.projectwork.signservice.domain.ServiceToken;
import ru.otus.projectwork.signservice.repository.ServiceTokenRepository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ServiceTokenRepositoryImpl implements ServiceTokenRepository {

    private static final ConcurrentHashMap<String, ServiceToken> storage = new ConcurrentHashMap<>();

    @Override
    public ServiceToken add(ServiceToken serviceToken) {
        return storage.putIfAbsent(serviceToken.getTokenId(), serviceToken);
    }

    @Override
    public Optional<ServiceToken> findByToken(String tokenId) {
        return Optional.ofNullable(storage.getOrDefault(tokenId, null));
    }

    @Override
    public Optional<ServiceToken> findByServiceName(String serviceName) {
        return storage.values().stream().filter(f -> f.getServiceName().equals(serviceName)).findFirst();
    }

    @Override
    public void delete(ServiceToken serviceToken) {
        storage.remove(serviceToken.getTokenId());
    }
}
