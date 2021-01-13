package ru.otus.projectwork.signservice.config.security;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.otus.projectwork.signservice.entity.User;

import java.util.Collection;
import java.util.Set;

public class UserDetailsImpl implements UserDetails {

    private final PasswordEncoder encoder;
    private final User user;
    private final Set<? extends GrantedAuthority> grantedAuthorities;

    public UserDetailsImpl(User user, PasswordEncoder encoder) {
        this.encoder = encoder;
        this.grantedAuthorities = GrantedAuthorityImpl.generateAuthorities(user.getUserRole());
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return encoder.encode(user.getPassword());
    }

    @Override
    public String getUsername() {
        return user.getLogin();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return user.isEnabled();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.isEnabled();
    }

    @JsonProperty
    public User details() {
        return user;
    }
}
