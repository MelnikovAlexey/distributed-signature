package ru.otus.projectwork.signservice.config;

import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.openfeign.FeignClientBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.unit.DataSize;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import ru.otus.projectwork.signservice.components.DynamicFeignClient;
import ru.otus.projectwork.signservice.components.impl.DynamicFeignClientImpl;
import ru.otus.projectwork.signservice.service.LocalSignInteractionService;

import javax.annotation.PostConstruct;
import javax.servlet.MultipartConfigElement;

import static java.util.Objects.nonNull;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class AppConfig {
    @Value("${spring.application.name}")
    private String serviceName;
    private final ApplicationContext applicationContext;
    private final DiscoveryClient discoveryClient;
    private final LocalSignInteractionService localSignInteractionService;

    @Bean
    public FeignClientBuilder clientBuilder() {
        return new FeignClientBuilder(applicationContext);
    }

    @Bean
    public DynamicFeignClient dynamicFeignClient() {
        return new DynamicFeignClientImpl(clientBuilder());
    }

    @Bean
    public MultipartResolver multipartResolver() {
        return new StandardServletMultipartResolver();
    }

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize(DataSize.ofMegabytes(20));
        factory.setMaxRequestSize(DataSize.ofMegabytes(20));
        return factory.createMultipartConfig();
    }

    @Bean
    public Encoder feignFormEncoder() {
        return new SpringFormEncoder();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @PostConstruct
    private void initialize() {
        // при запуске проверяем на наличие запущенных сервисов и пытаемся запросить у них текущие токены
        discoveryClient.getServices()
                .stream()
                .filter(service -> !serviceName.equalsIgnoreCase(service))
                .forEach(service -> registerService(service, dynamicFeignClient()));
    }

    private void registerService(String service, DynamicFeignClient feignClient) {
        try {
            final String token = feignClient.requestToken(service);
            if (nonNull(token) && !token.isEmpty())
                localSignInteractionService.serviceConnected(token, service);
        } catch (Exception e) {
            log.error("", e);
        }
    }
}
