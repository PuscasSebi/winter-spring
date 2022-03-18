package com.puscas.authentication.encryption;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.time.Instant;
import java.util.UUID;

public class AesEncryptionUtil {

    public static String encrypt(String plainText, String password, String salt){
        try {
            SecretKey keyFromPassword = AESUtil.getKeyFromPassword(password, salt);
            return AESUtil.encryptPasswordBased(plainText, keyFromPassword, new IvParameterSpec(salt.getBytes()));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String decrypt(String encryptedText, String password, String salt){
        SecretKey keyFromPassword = null;
        try {
            keyFromPassword = AESUtil.getKeyFromPassword(password, salt);
            return         AESUtil.decryptPasswordBased(encryptedText,keyFromPassword, new IvParameterSpec(salt.getBytes()));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static final long NUM_HUNDRED_NANOS_IN_A_SECOND = 10_000_000L;

    private static final long NUM_HUNDRED_NANOS_FROM_UUID_EPOCH_TO_UNIX_EPOCH = 122_192_928_000_000_000L;


    /**
     * Extracts the Instant (with the maximum available 100ns precision) from the given time-based (version 1) UUID.
     *
     * @return the {@link Instant} extracted from the given time-based UUID
     * @throws UnsupportedOperationException If this UUID is not a version 1 UUID
     */
    public static Instant getInstantFromUUID(final UUID uuid) {
        final long hundredNanosSinceUnixEpoch = uuid.timestamp() - NUM_HUNDRED_NANOS_FROM_UUID_EPOCH_TO_UNIX_EPOCH;
        final long secondsSinceUnixEpoch = hundredNanosSinceUnixEpoch / NUM_HUNDRED_NANOS_IN_A_SECOND;
        final long nanoAdjustment = ((hundredNanosSinceUnixEpoch % NUM_HUNDRED_NANOS_IN_A_SECOND) * 100);
        return Instant.ofEpochSecond(secondsSinceUnixEpoch, nanoAdjustment);
    }
}
