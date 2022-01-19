package com.puscas.authentication.util;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;


public class KeyReader {
    public static PrivateKey getPrivateKey(String filename)
            throws Exception {

        byte[] keyBytes = Files.readAllBytes(Paths.get(filename));

        PKCS8EncodedKeySpec spec =
                new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(spec);
    }

    public static PublicKey getPublicKey(String filename)
            throws Exception {

        byte[] keyBytes = Files.readAllBytes(Paths.get(filename));

        X509EncodedKeySpec spec =
                new X509EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(spec);
    }
    public JWKSource<SecurityContext> jwkSource() throws Exception {
        String filePath = "src/main/resources/keystore.jks";
        // PrivateKey privateKey = KeyReader.getPrivateKey(filePath);
        // PublicKey publicKey = KeyReader.getPublicKey(filePath);
        if(!new File(filePath).exists()){
            filePath = "authorization-mysql/src/main/resources/keystore.jks";
        }
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        keyStore.load(new FileInputStream(filePath), "password".toCharArray());
        RSAKey load = RSAKey.load(keyStore, "selfsigned", "password".toCharArray());

        JWKSet jwkSet = new JWKSet(load);
        return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
    }
    public RSAKey getRsaKey() throws Exception {
        String filePath = "authentication/src/main/resources/keystore.jks";
        // PrivateKey privateKey = KeyReader.getPrivateKey(filePath);
        // PublicKey publicKey = KeyReader.getPublicKey(filePath);

        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        keyStore.load(new FileInputStream(filePath), "password".toCharArray());
        return RSAKey.load(keyStore, "selfsigned", "password".toCharArray());

    }
}
