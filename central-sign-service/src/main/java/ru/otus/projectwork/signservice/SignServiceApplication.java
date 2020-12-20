package ru.otus.projectwork.signservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
@Slf4j
public class SignServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SignServiceApplication.class, args);
    }

}
