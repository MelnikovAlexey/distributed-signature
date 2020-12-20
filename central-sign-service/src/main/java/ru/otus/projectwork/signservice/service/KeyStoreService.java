package ru.otus.projectwork.signservice.service;

import java.security.PrivateKey;
import java.security.cert.Certificate;

public interface KeyStoreService {

    void init();

    PrivateKey getPrivateKey(String alias, String password);

    Certificate getCertificate(String alias);

}
