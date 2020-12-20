package ru.otus.projectwork.signservice.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.CryptoPro.JCP.JCP;
import ru.CryptoPro.JCP.Random.BioRandomConsole;
import ru.otus.projectwork.signservice.service.KeyStoreService;
import ru.otus.projectwork.signservice.service.exception.AuthException;
import ru.otus.projectwork.signservice.service.exception.KeyAccessException;
import ru.otus.projectwork.signservice.service.exception.KeyStoreInitialException;

import javax.annotation.PostConstruct;
import java.io.*;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;

@Service
@Slf4j
public class KeyStoreServiceImpl implements KeyStoreService {
    /**
     * имя ключевого носителя для инициализации хранилища
     */
    private static final String STORE_TYPE = JCP.HD_STORE_NAME;

    @Value("${keystore.file.path}")
    private String path;

    @Value("${keystore.file.name}")
    private String name;

    @Value("${keystore.password}")
    private String password;

    private String storePath;
    /**
     * устанавливаемый пароль на хранилище сертификатов, переопределяется в методе init
     */
    private char[] storePass = new char[]{'p', 'a', 's', 's', 'w', 'o', 'r', 'd'};

    /**
     * Инициируем хранилище ключей, предварительно проверяем наличие хранилища.
     */
    @Override
    @PostConstruct
    public void init() {
        checkStoreVariable();
        BioRandomConsole.main(null);
        try {
            initStore();
        } catch (KeyStoreInitialException e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public PrivateKey getPrivateKey(String alias, String password) {
        PrivateKey key;
        try {
            final KeyStore keyStore = getKeyStore();
            key = (PrivateKey) keyStore.getKey(alias, password.toCharArray());
        } catch (KeyStoreException e) {
            throw new KeyAccessException(e.getMessage(), e.getCause());
        } catch (CertificateException | NoSuchAlgorithmException | IOException | UnrecoverableKeyException e) {
            throw new AuthException(e.getMessage(), e.getCause());
        }
        return key;
    }

    @Override
    public Certificate getCertificate(String alias) {
        Certificate certificate;
        try {
            final KeyStore keyStore = getKeyStore();
            certificate = keyStore.getCertificate(alias);
        } catch (KeyStoreException e) {
            throw new KeyAccessException(e.getMessage(), e.getCause());
        } catch (CertificateException | NoSuchAlgorithmException | IOException e) {
            throw new AuthException(e.getMessage(), e.getCause());
        }
        return certificate;
    }




    private KeyStore getKeyStore() throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException {
        final KeyStore keyStore = KeyStore.getInstance(STORE_TYPE);
        final File file = new File(storePath);
        if (!file.exists()) {
            throw new FileNotFoundException("File " + STORE_TYPE + " not found while retrieving private key");
        }
        keyStore.load(new FileInputStream(file), storePass);
        return keyStore;
    }

    private void initStore() {
        final File file = new File(storePath);
        if (file.exists()) {
            log.info("Storage {} exists", storePath);
            return;
        } else {
            file.getParentFile().mkdirs();
        }
        try {
            final KeyStore keyStore = KeyStore.getInstance(STORE_TYPE);
            keyStore.load(null, null);
            keyStore.store(new FileOutputStream(file), storePass);
        } catch (KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException e) {
            throw new KeyStoreInitialException(e.getMessage(), e.getCause());
        }
    }

    private void checkStoreVariable() {
        storePass = password.toCharArray();
        if (path.isEmpty() || name.isEmpty()) {
            log.error("Path to keystore or name keystore is empty");
            throw new KeyStoreInitialException("Path to keystore or name keystore is empty");
        }
        storePath = path + (path.endsWith(File.separator) ? "" : File.separator) + name;
    }
}
