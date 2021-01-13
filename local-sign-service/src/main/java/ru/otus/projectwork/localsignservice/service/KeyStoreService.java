package ru.otus.projectwork.localsignservice.service;

import ru.otus.projectwork.localsignservice.domain.StoreInfo;

import java.util.Optional;

public interface KeyStoreService {

    Optional<StoreInfo> getCertAndKey(String keyPass);

    boolean checkPrivKeyPass(String keyPass);
}