package ru.otus.projectwork.localsignservice.components;

import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicReference;

@Component
public class TokenInfo {
    private final AtomicReference<String> info = new AtomicReference<>(null);

    public void setTokenInfo(String token){
        info.set(token);
    }

    public void clearToken(){
        info.set(null);
    }

    public String getToken(){
        return info.get();
    }

}
