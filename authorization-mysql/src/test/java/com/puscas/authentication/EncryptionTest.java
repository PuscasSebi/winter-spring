package com.puscas.authentication;

import com.puscas.authentication.encryption.AesEncryptionUtil;
import org.junit.jupiter.api.Test;

import java.util.UUID;

public class EncryptionTest {


    @Test
    public void testEncryption(){
        String password = "IDKWHATSHAPENING";
        String salt = "SALTSALTSALTSALT";
        String token = "spusca@grubhub.com####"+ UUID.randomUUID();

        String encrypt = AesEncryptionUtil.encrypt(token, password, salt);
        System.out.println("encryty"+ encrypt);
        String decrypt = AesEncryptionUtil.decrypt("WSWTnTVjgcVxS6e0tDKrMvuBInVkagV7LVdiCCTBAhPkf2Rk8btqfA+EIU26eUOyG7GIZJGgj0RBVCRt0sbO4ln7Hyc30s0zmlHrzMmPLiU=", password, salt);
        System.out.println(decrypt);
    }
}
