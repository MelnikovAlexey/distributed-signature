package ru.otus.projectwork.localsignservice.service.impl;

import com.objsys.asn1j.runtime.Asn1BerDecodeBuffer;
import com.objsys.asn1j.runtime.Asn1BerEncodeBuffer;
import com.objsys.asn1j.runtime.Asn1Null;
import com.objsys.asn1j.runtime.Asn1ObjectIdentifier;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.CryptoPro.JCP.ASN.CryptographicMessageSyntax.*;
import ru.CryptoPro.JCP.ASN.PKIX1Explicit88.CertificateSerialNumber;
import ru.CryptoPro.JCP.ASN.PKIX1Explicit88.Name;
import ru.CryptoPro.JCP.JCP;
import ru.CryptoPro.JCP.params.OID;
import ru.otus.projectwork.localsignservice.controller.ui.PrivateKeyPasswordController;
import ru.otus.projectwork.localsignservice.domain.PasswordKeyType;
import ru.otus.projectwork.localsignservice.domain.StoreKeyPass;
import ru.otus.projectwork.localsignservice.service.KeyStoreService;
import ru.otus.projectwork.localsignservice.service.PasswordManagerService;
import ru.otus.projectwork.localsignservice.service.SignService;
import ru.otus.projectwork.localsignservice.service.exception.AuthException;
import ru.otus.projectwork.localsignservice.service.exception.KeyAccessException;

import java.security.Signature;
import java.security.*;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;


@Service
@Slf4j
public class SignServiceImpl implements SignService {
    @Value("${spring.application.name}")
    private String serviceName;

    private final KeyStoreService keyStoreService;
    private final PasswordManagerService passwordManagerService;

    public SignServiceImpl(KeyStoreService keyStoreService, PasswordManagerService passwordManagerService) {
        this.keyStoreService = keyStoreService;
        this.passwordManagerService = passwordManagerService;
    }

    @Override
    public Optional<String> doSign(byte[] data) {
        final CountDownLatch latch = new CountDownLatch(1);
        final AtomicBoolean queryResult = new AtomicBoolean(false);
        final StringProperty privatePass = new SimpleStringProperty("");
        if (!passwordManagerService.checkPrivateKeyPassword()) {
            Platform.runLater(() -> {
                queryResult.set(showPasswordDialog(privatePass));
                latch.countDown();
            });
        } else {
            privatePass.set(passwordManagerService.getPrivateKeyPassword());
            Platform.runLater(() -> {
                queryResult.set(isConfirm());
                latch.countDown();
            });
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        if (queryResult.get()) {
            return getSign(data, privatePass.get());
        }
        return Optional.empty();
    }

    private boolean isConfirm() {
        return new Alert(Alert.AlertType.CONFIRMATION, "Подтвердите операцию подписи.").showAndWait().
                filter(buttonType -> buttonType == ButtonType.OK)
                .isPresent();
    }

    private boolean showPasswordDialog(StringProperty privatePass) {
        final PrivateKeyPasswordController controller = PrivateKeyPasswordController.builder().create(keyStoreService);
        Optional<Map<PasswordKeyType, String>> map = controller.getMap();
        if (map.isPresent()) {
            String pass = map.get().get(PasswordKeyType.PRIVATE_PASS_KEY);
            privatePass.set(pass);
            if (controller.isRemember()) {
                passwordManagerService.save(new StoreKeyPass(pass, PasswordKeyType.PRIVATE_PASS_KEY));
            }
            return true;
        }
        return false;
    }

    private Optional<String> getSign(byte[] data, String keyPassword) {
        try {
           // return keyStoreService.getKey(keyPassword).map(privateKey -> sign(privateKey,data));
            return keyStoreService.getCertAndKey(keyPassword)
                    .map(storeInfo -> createPKCS7(data,storeInfo.getPrivateKey(),(X509Certificate) storeInfo.getCertificate()));//sign(privateKey, data));
        } catch (AuthException | KeyAccessException e) {
            log.error(e.getMessage());
            new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK).show();
        }
        catch (Exception e){
         log.error(e.getMessage());
        }
        return Optional.empty();
    }


    private String createPKCS7(byte[] data, PrivateKey privateKey,
                               X509Certificate certificate) {
        // Получаем бинарную подпись длиной 64 байта.
        try {
            final Signature signature = Signature.getInstance(JCP.GOST_SIGN_2012_256_NAME);
            signature.initSign(privateKey);
            signature.update(data);

            final byte[] sign = signature.sign();

            // Формируем контекст подписи формата PKCS7.

            final ContentInfo all = new ContentInfo();
            all.contentType = new Asn1ObjectIdentifier(
                    new OID( "1.2.840.113549.1.7.2").value);

            final SignedData cms = new SignedData();
            all.content = cms;
            cms.version = new CMSVersion(1);

            // Идентификатор алгоритма хеширования.

            cms.digestAlgorithms = new DigestAlgorithmIdentifiers(1);
            final DigestAlgorithmIdentifier a = new DigestAlgorithmIdentifier(
                    new OID(JCP.GOST_DIGEST_2012_256_OID).value);
            a.parameters = new Asn1Null();
            cms.digestAlgorithms.elements[0] = a;

            // Т.к. подпись отсоединенная, то содержимое отсутствует.

            cms.encapContentInfo = new EncapsulatedContentInfo(
                    new Asn1ObjectIdentifier(new OID("1.2.840.113549.1.7.1").value), null);

            // Добавляем сертификат подписи.

            cms.certificates = new CertificateSet(1);
            final ru.CryptoPro.JCP.ASN.PKIX1Explicit88.Certificate asnCertificate =
                    new ru.CryptoPro.JCP.ASN.PKIX1Explicit88.Certificate();

            final Asn1BerDecodeBuffer decodeBuffer =
                    new Asn1BerDecodeBuffer(certificate.getEncoded());
            asnCertificate.decode(decodeBuffer);

            cms.certificates.elements = new CertificateChoices[1];
            cms.certificates.elements[0] = new CertificateChoices();
            cms.certificates.elements[0].set_certificate(asnCertificate);

            // Добавялем информацию о подписанте.

            cms.signerInfos = new SignerInfos(1);
            cms.signerInfos.elements[0] = new SignerInfo();
            cms.signerInfos.elements[0].version = new CMSVersion(1);
            cms.signerInfos.elements[0].sid = new SignerIdentifier();

            final byte[] encodedName = certificate.getIssuerX500Principal().getEncoded();
            final Asn1BerDecodeBuffer nameBuf = new Asn1BerDecodeBuffer(encodedName);
            final Name name = new Name();
            name.decode(nameBuf);

            final CertificateSerialNumber num = new CertificateSerialNumber(
                    certificate.getSerialNumber());

            cms.signerInfos.elements[0].sid.set_issuerAndSerialNumber(
                    new IssuerAndSerialNumber(name, num));
            cms.signerInfos.elements[0].digestAlgorithm =
                    new DigestAlgorithmIdentifier(new OID(JCP.GOST_DIGEST_2012_256_OID).value);
            cms.signerInfos.elements[0].digestAlgorithm.parameters = new Asn1Null();
            cms.signerInfos.elements[0].signatureAlgorithm =
                    new SignatureAlgorithmIdentifier(new OID("1.2.840.113549.1.7.2").value);
            cms.signerInfos.elements[0].signatureAlgorithm.parameters = new Asn1Null();
            cms.signerInfos.elements[0].signature = new SignatureValue(sign);

            // Получаем закодированную подпись.

            final Asn1BerEncodeBuffer asnBuf = new Asn1BerEncodeBuffer();
            all.encode(asnBuf, true);

            return Base64.getEncoder().encodeToString(asnBuf.getMsgCopy());
        }catch (Exception e){
            log.error(e.getMessage());
        }
        return "";
    }
}
