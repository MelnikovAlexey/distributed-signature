package ru.otus.projectwork.localsignservice.domain;

public class StoreKeyPass {

    private final String password;
    private final PasswordKeyType title;

    public StoreKeyPass(String password, PasswordKeyType title) {
        this.password = password;
        this.title = title;
    }

    public String getPassword() {
        return password;
    }

    public PasswordKeyType getTitle() {
        return title;
    }
}
