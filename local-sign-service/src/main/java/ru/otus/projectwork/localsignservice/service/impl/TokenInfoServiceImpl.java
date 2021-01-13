package ru.otus.projectwork.localsignservice.service.impl;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.otus.projectwork.localsignservice.components.PKCS11Wrapper;
import ru.otus.projectwork.localsignservice.components.TokenInfo;
import ru.otus.projectwork.localsignservice.service.PasswordManagerService;
import ru.otus.projectwork.localsignservice.service.SignServiceGateway;
import ru.otus.projectwork.localsignservice.service.TokenInfoService;
import sun.security.pkcs11.wrapper.CK_TOKEN_INFO;
import sun.security.pkcs11.wrapper.PKCS11;

import java.util.Objects;
import java.util.concurrent.CountDownLatch;

@Service
@Slf4j
public class TokenInfoServiceImpl implements TokenInfoService {

    private final PKCS11Wrapper pkcs11wrapper;
    private final SignServiceGateway serviceGateway;
    private final TokenInfo tokenInfo;
    private final PasswordManagerService passwordManagerService;

    public TokenInfoServiceImpl(PKCS11Wrapper pkcs11wrapper,
                                SignServiceGateway serviceGateway,
                                TokenInfo tokenInfo,
                                PasswordManagerService passwordManagerService) {
        this.pkcs11wrapper = pkcs11wrapper;
        this.serviceGateway = serviceGateway;
        this.tokenInfo = tokenInfo;
        this.passwordManagerService = passwordManagerService;
    }

    @SneakyThrows
    @Scheduled(cron = "${cron.expression}")
    @Override
    public synchronized void getTokenList() {
        long[] slotList = getPkcs().C_GetSlotList(true);
        if (slotList.length != 1) {
            if (slotList.length == 0 && Objects.nonNull(tokenInfo.getToken())) {
                disconnect();
            }
            return;
        }

        CK_TOKEN_INFO info = getPkcs().C_GetTokenInfo(slotList[0]);
        String serialNumber = String.valueOf(info.serialNumber).trim();

        if (!serialNumber.equals(tokenInfo.getToken())) {
            connect(serialNumber);
        }
    }

    private PKCS11 getPkcs() {
        return pkcs11wrapper.getPkcs11();
    }

    private void disconnect() {
        log.info("token disconnected");
        tokenInfo.clearToken();
        serviceGateway.disconnect();
        passwordManagerService.clean();
        showMessage("Токен отключен");
    }

    private void connect(String tokenId) {
        log.info("Token connected {}", tokenId);
        tokenInfo.setTokenInfo(tokenId);
        serviceGateway.connect(tokenId);
        showMessage(String.format("Токен %s подключен", Integer.parseInt(tokenId, 16)));
    }


    private void showMessage(String message) {
        final CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            new Alert(Alert.AlertType.INFORMATION,message).showAndWait();
            latch.countDown();
        });
        try {
            latch.await();

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}
