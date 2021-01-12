package ru.otus.projectwork.signservice.service.impl;

import com.objsys.asn1j.runtime.Asn1BerDecodeBuffer;
import com.objsys.asn1j.runtime.Asn1BerEncodeBuffer;
import com.objsys.asn1j.runtime.Asn1Null;
import com.objsys.asn1j.runtime.Asn1ObjectIdentifier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.CryptoPro.JCP.ASN.CryptographicMessageSyntax.*;
import ru.CryptoPro.JCP.ASN.PKIX1Explicit88.CertificateSerialNumber;
import ru.CryptoPro.JCP.ASN.PKIX1Explicit88.Name;
import ru.CryptoPro.JCP.JCP;
import ru.CryptoPro.JCP.params.OID;
import ru.otus.projectwork.signservice.components.DynamicFeignClient;
import ru.otus.projectwork.signservice.entity.CertificateAlias;
import ru.otus.projectwork.signservice.entity.UserToken;
import ru.otus.projectwork.signservice.repository.ServiceTokenRepository;
import ru.otus.projectwork.signservice.repository.UserSignRepository;
import ru.otus.projectwork.signservice.repository.UserTokenRepository;
import ru.otus.projectwork.signservice.service.KeyStoreService;
import ru.otus.projectwork.signservice.service.SignService;
import ru.otus.projectwork.signservice.service.exception.SignException;

import java.io.IOException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class SignServiceImpl implements SignService {
    private final UserSignRepository repository;
    private final UserTokenRepository tokenRepository;
    private final ServiceTokenRepository serviceRepository;
    private final DynamicFeignClient feignClient;
    private final KeyStoreService keyStore;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Optional<String> doSign(MultipartFile data, long userId, String pwd) throws Exception {

        // Для начала проверяем наличие данных пользователя в центральном хранилище
        final Optional<CertificateAlias> localUserData = Optional.ofNullable(repository.findByUserId(userId));
        if (localUserData.isPresent()) {
            final CertificateAlias alias = localUserData.get();
            if (checkPassword(pwd, alias.getPassword())) {
                // выполняем подпись на текущем сервисе и возвращаем подпись
                byte[] dataBytes = new byte[0];
                try {
                    dataBytes = data.getBytes();
                } catch (IOException e) {
                    log.error("");
                }
                PrivateKey key = keyStore.getPrivateKey(alias.getAlias(), alias.getPassword());
                Certificate certificate = keyStore.getCertificate(alias.getAlias());
                return Optional.of(createPKCS7(dataBytes, key, (X509Certificate) certificate));
            } else {
                throw new SignException("Не совпадение с паролем в БД");
            }
        }
        // Если пользователя нет, то ищем данные по привязке номера токена и пользователя
        final Optional<UserToken> userToken = Optional.ofNullable(tokenRepository.findUserByUserId(userId));
        return userToken
                .flatMap(token -> serviceRepository.findByToken(token.getTokenId()))
                // Если нашли данные по сервису, в котором на данный момент найденный токен, то перенаправляем запрос ему
                .flatMap(service ->
                        feignClient.doSign(service.getServiceName(), data)
                );
    }

    private boolean checkPassword(String passFromRequest, String passFromDB) {
        return passwordEncoder.matches(passFromDB,passFromRequest);
       // return Objects.equals(passFromDB, new String(Base64.getDecoder().decode(passFromRequest)));
    }



    private String createPKCS7(byte[] data, PrivateKey privateKey,
                               X509Certificate certificate) throws Exception {

        // Получаем бинарную подпись длиной 64 байта.

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
    }
}
