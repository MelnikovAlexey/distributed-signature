package ru.otus.projectwork.localsignservice.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "sign-service")
public interface SignServiceGateway {
    @PostMapping(value = "/connect", headers = "serviceName=${spring.application.name}")
    void connect(@RequestBody String tokenId);

    @PostMapping(value = "/disconnect", headers = "serviceName=${spring.application.name}")
    void disconnect();
}
