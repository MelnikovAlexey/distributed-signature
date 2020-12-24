package ru.otus.projectwork.localsignservice.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.security.PrivateKey;
import java.security.cert.Certificate;

@AllArgsConstructor
@Getter
@Setter
public class StoreInfo {
    private PrivateKey privateKey;
    private Certificate certificate;
}
