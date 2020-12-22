package ru.otus.projectwork.signservice.components.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FeignClientBuilder;
import org.springframework.web.multipart.MultipartFile;
import ru.otus.projectwork.signservice.components.DynamicFeignClient;
import ru.otus.projectwork.signservice.service.LocalSignServiceGateway;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Компонент для динамического создания feign-клиентов по имени сервиса
 */
@Slf4j
public class DynamicFeignClientImpl implements DynamicFeignClient {

    private final FeignClientBuilder clientBuilder;
    private final Map<String, LocalSignServiceGateway> clientMapping = new ConcurrentHashMap<>();

    public DynamicFeignClientImpl(FeignClientBuilder clientBuilder) {
        this.clientBuilder = clientBuilder;
    }

    @Override
    public Optional<String> doSign(String serviceName, MultipartFile data) {
        log.info("Service name:{}", serviceName);
        return getGateway(serviceName).doSign(data);
    }

    @Override
    public String requestToken(String serviceName) {
        return getGateway(serviceName).requestToken();
    }


    private LocalSignServiceGateway getGateway(String serviceName) {
        return clientMapping.computeIfAbsent(serviceName, name -> clientBuilder.forType(LocalSignServiceGateway.class, name).build());
    }
}
