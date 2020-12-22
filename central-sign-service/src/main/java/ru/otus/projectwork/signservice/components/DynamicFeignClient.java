package ru.otus.projectwork.signservice.components;

import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface DynamicFeignClient {

    /**
     * Передача локальному сервису ЭЦП документа на подпись
     * @param serviceName - имя сервиса
     * @param data - документ на подпись
     * @return массив байт ЭЦП
     */
    Optional<String> doSign(String serviceName, MultipartFile data);

    /**
     * Запрос информации о токенах на запущенных локальных сервисах ЭЦП
     * @param serviceName имя сервсиа
     * @return информация о токене.
     */
    String requestToken(String serviceName);
}
