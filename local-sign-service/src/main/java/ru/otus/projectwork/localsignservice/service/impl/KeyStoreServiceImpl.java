package ru.otus.projectwork.localsignservice.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.projectwork.localsignservice.domain.StoreInfo;
import ru.otus.projectwork.localsignservice.service.KeyStoreService;
import ru.otus.projectwork.localsignservice.service.exception.AuthException;
import ru.otus.projectwork.localsignservice.service.exception.KeyAccessException;

import java.io.IOException;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.Enumeration;
import java.util.Optional;

@Service
@Slf4j
public class KeyStoreServiceImpl implements KeyStoreService {
    private static final String STORE_TYPE = "RutokenStore";

    private KeyStore getKeyStore() throws KeyStoreException, NoSuchProviderException, IOException, NoSuchAlgorithmException, CertificateException {
        KeyStore keyStore = KeyStore.getInstance(STORE_TYPE, "JCP");
        keyStore.load(null, null);
        return keyStore;
    }

    @Override
    public Optional<StoreInfo> getCertAndKey(String keyPass) {
        try {
            KeyStore keyStore = getKeyStore();
            return getStoreInfo(keyPass, keyStore);
        } catch (KeyStoreException | NoSuchProviderException e) {
            throw new KeyAccessException(e.getMessage(), e.getCause());
        } catch (CertificateException | NoSuchAlgorithmException | IOException e) {
            throw new AuthException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public boolean checkPrivKeyPass(String keyPass) {
        boolean result = false;
        try {
            KeyStore store = getKeyStore();
            Enumeration<String> aliases = store.aliases();

            while (aliases.hasMoreElements()) {
                try {
                    String alias = aliases.nextElement();
                    PrivateKey key = (PrivateKey) store.getKey(alias, keyPass.toCharArray());
                    result = true;
                    break;
                } catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException e) {
                    log.error(e.getMessage());
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return result;
    }

    private Optional<StoreInfo> getStoreInfo(String keyPass, KeyStore keyStore) throws KeyStoreException {
        Enumeration<String> aliases = keyStore.aliases();
        while (aliases.hasMoreElements()) {
            String alias = aliases.nextElement();
            try {
                PrivateKey key = (PrivateKey) keyStore.getKey(alias, keyPass.toCharArray());
                Certificate cert = keyStore.getCertificate(alias);
                return Optional.of(new StoreInfo(key, cert));
            } catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException e) {
                log.error("Алиас и пароль к ключу не совпадают: {}", e.getMessage());
            }
        }
        return Optional.empty();
    }
}
