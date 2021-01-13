package ru.otus.projectwork.localsignservice.service;


import ru.otus.projectwork.localsignservice.domain.StoreKeyPass;

public interface PasswordManagerService {

    String getPrivateKeyPassword();

    boolean checkPrivateKeyPassword();

    void clean();

    void save(StoreKeyPass keyPass);

}
