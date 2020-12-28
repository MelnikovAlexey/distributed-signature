package ru.otus.projectwork.signservice.config.security;


import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.otus.projectwork.signservice.entity.User;
import ru.otus.projectwork.signservice.repository.UserRepository;

import java.util.Objects;

@Service("innerUserDetailService")
public class UserDetailServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    public UserDetailServiceImpl(UserRepository userRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    @Override
    public UserDetails loadUserByUsername(String s)  {
        User user = userRepository.findByLogin(s,true);
        if (Objects.isNull(user)){
            throw new UsernameNotFoundException(s);
        }
        if (Objects.isNull(user.getPassword())){
            user.setPassword("");
        }
        return new UserDetailsImpl(user, encoder);
    }
}
