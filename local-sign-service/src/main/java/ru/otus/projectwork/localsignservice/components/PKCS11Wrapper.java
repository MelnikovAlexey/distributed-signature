package ru.otus.projectwork.localsignservice.components;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import sun.security.pkcs11.wrapper.CK_C_INITIALIZE_ARGS;
import sun.security.pkcs11.wrapper.PKCS11;

@Component
public class PKCS11Wrapper {

    private final PKCS11 pkcs11;

    private static final  String FUNCTION_LIST = "C_GetFunctionList";

    @SneakyThrows
    public PKCS11Wrapper(@Value("${pkcs.lib.name}")String libName) {
        this.pkcs11 = PKCS11.getInstance(libName, FUNCTION_LIST, new CK_C_INITIALIZE_ARGS(), false);
    }

    public PKCS11 getPkcs11() {
        return pkcs11;
    }
}
