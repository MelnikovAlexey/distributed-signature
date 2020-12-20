package ru.otus.projectwork.signservice.repository;

import ru.otus.projectwork.signservice.domain.ServiceToken;

import java.util.Optional;

public interface ServiceTokenRepository {

    ServiceToken add(ServiceToken serviceToken);

    Optional<ServiceToken> findByToken(String tokenId);

    Optional<ServiceToken> findByServiceName(String serviceName);

    void delete(ServiceToken serviceToken);
}
