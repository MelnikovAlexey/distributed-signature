package ru.otus.projectwork.localsignservice.service.impl;


import org.springframework.stereotype.Service;
import ru.otus.projectwork.localsignservice.domain.PasswordKeyType;
import ru.otus.projectwork.localsignservice.domain.StoreKeyPass;
import ru.otus.projectwork.localsignservice.service.PasswordManagerService;

import java.util.concurrent.ConcurrentHashMap;

import static ru.otus.projectwork.localsignservice.domain.PasswordKeyType.PRIVATE_PASS_KEY;


@Service
public class PasswordManagerServiceImpl implements PasswordManagerService {

    private static final ConcurrentHashMap<PasswordKeyType,StoreKeyPass> storeKeyPassword = new ConcurrentHashMap<>();

    @Override
    public String getPrivateKeyPassword() {
        if (checkPrivateKeyPassword()){
            return storeKeyPassword.get(PRIVATE_PASS_KEY).getPassword();
        }
        return "";
    }

    @Override
    public boolean checkPrivateKeyPassword() {
        return storeKeyPassword.containsKey(PRIVATE_PASS_KEY);
    }

    @Override
    public void clean() {
        storeKeyPassword.clear();
    }

    @Override
    public void save(StoreKeyPass keyPass) {
        storeKeyPassword.put(keyPass.getTitle(), keyPass);
    }
}
