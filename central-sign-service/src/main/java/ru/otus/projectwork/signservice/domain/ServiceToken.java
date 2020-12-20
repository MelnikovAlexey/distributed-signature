package ru.otus.projectwork.signservice.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ServiceToken {
    private final String serviceName;
    private final String tokenId;
}
