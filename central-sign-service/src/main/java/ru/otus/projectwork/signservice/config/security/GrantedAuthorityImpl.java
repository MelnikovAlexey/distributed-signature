package ru.otus.projectwork.signservice.config.security;



import org.springframework.security.core.GrantedAuthority;
import ru.otus.projectwork.signservice.entity.UserRole;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class GrantedAuthorityImpl implements GrantedAuthority {
    public static final long ID_RIGHT_ADMIN = 1;

    public static final String AUTHORITY_ADMIN = "ROLE_ADMIN";

    public static final String AUTHORITY_USER = "ROLE_USER";

    private final String authority;

    public GrantedAuthorityImpl(String authority) {
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return authority;
    }

    public static Set<GrantedAuthorityImpl> generateAuthorities(UserRole userRole){
        Set<GrantedAuthorityImpl> authorities = new HashSet<>();
        if (Objects.nonNull(userRole)&&userRole.getId()>0){
            authorities.add(new GrantedAuthorityImpl(AUTHORITY_USER));
            if(isAdmin(userRole)){
                authorities.add(new GrantedAuthorityImpl(AUTHORITY_ADMIN));
            }
        }
        return authorities;
    }

    private static boolean isAdmin(UserRole userRole){
        return userRole.getId() == ID_RIGHT_ADMIN;
    }
}
