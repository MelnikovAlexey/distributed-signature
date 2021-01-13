package ru.otus.projectwork.signservice.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.otus.projectwork.signservice.config.security.JsonBasicAuthenticationEntryPoint;

@Configuration
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
    private final UserDetailsService userDetailService;

    private final JsonBasicAuthenticationEntryPoint entryPoint;

    private final PasswordEncoder passwordEncoder;


    public WebSecurityConfiguration(@Qualifier("innerUserDetailService")UserDetailsService userDetailService,
                                    JsonBasicAuthenticationEntryPoint entryPoint,
                                    PasswordEncoder passwordEncoder) {
        this.userDetailService = userDetailService;
        this.entryPoint = entryPoint;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(userDetailService);
        return provider;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().disable().csrf().disable();
        http.authorizeRequests()
                .antMatchers(HttpMethod.POST, "/sign").hasRole("USER")
                .antMatchers(HttpMethod.POST, "/connect").permitAll()
                .antMatchers(HttpMethod.POST, "/disconnect").permitAll()
                .and()
                .httpBasic().authenticationEntryPoint(entryPoint);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authenticationProvider());
    }
}
