package cn.surveyking.server.core.uitls;

import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;

import javax.annotation.PostConstruct;
import javax.crypto.Cipher;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;

/**
 *
 */
@Component
public class RSAUtils {

    // 默认的公钥和私钥初始化为一个已知的值，将在应用启动时更新
    public static String DEFAULT_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCAh/pzDlAqf13DbP5lVE2koX7RvJKGfrA5Ml5XWCQ6kNQkyb2Mv1L8GOIMLvlyMWt3My5weilkJGWS9R943g7Ze3bZZiCV994BFvX0IdJstVoppKiJllF1i7+6UNjo9Or/qzFBRmy70Z5i44BhvKws32gPHS4Sib1p1mDXVbV6JQIDAQAB";
    public static String DEFAULT_PRIVATE_KEY = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAICH+nMOUCp/XcNs/mVUTaShftG8koZ+sDkyXldYJDqQ1CTJvYy/UvwY4gwu+XIxa3czLnB6KWQkZZL1H3jeDtl7dtlmIJX33gEW9fQh0my1WimkqImWUXWLv7pQ2Oj06v+rMUFGbLvRnmLjgGG8rCzfaA8dLhKJvWnWYNdVtXolAgMBAAECgYBI72CSS4v4IaBOVhoh2+3XPwEc+TnYcimDu25HeC/OwAJyAby7EpJ/lYsoSLuqLhsCYBu5HclBF1pAQzKhvriDqSqq8fs0psToB3PrRDbTbqg6XrWxOjf/5xqa1mN/tICZgqItnNkFT0w+WkBJpxZfMdohw3raEDPGSrr9UZj9vQJBALWvD3Y3oAFgaOdI3AiZKkZ+FunuTlq0r1bBNs/NYKxJBxI5HdZhKDt9JCv8Us3NGox9M4auSjwO/BQ/rOyTVE8CQQC1Gw+TXR6sbmrMlbgFYlEQiK0gJvz/V/MaJqO2lad+ojgEFu2CmXahlKPJoul4F40Etzft5B3HVs8Tz2132wlLAkAznFQ/F9QbMAD82qSuyJvKxJzLvUeC2tsIQQDKDSSOLHyWv6TrNlRQed8ho57+GWqWSCav9qjd4L/ZHLGJztxfAkAZLiIEQzY4k0GWIFrtpLXQrrAjgEg82GWchTLN+BDJspRHPUjYl62+2YPMTTJY2C1rMm48TTM2vAMepgB6YaHxAkEAgyAh6Zw4V4BROS+ysU7pjdsqqGIr8NWpjtQWKPLSn5RgDgzK6UMWpB7A39p3C/5vYR/Os288GdYUrASJzWr6Cw==";

    @PostConstruct
    @SneakyThrows
    public void init() {
        // 在 Spring 容器初始化后生成新的密钥对
        HashMap<String, String> keyPair = genKeyPair();
        DEFAULT_PUBLIC_KEY = keyPair.get("publicKey");
        DEFAULT_PRIVATE_KEY = keyPair.get("privateKey");
    }

    /**
     * 生成随机 RSA 密钥对
     *
     * @return 包含公钥和私钥的 HashMap
     * @throws NoSuchAlgorithmException 如果没有 RSA 算法则抛出异常
     */
    public static HashMap<String, String> genKeyPair() throws NoSuchAlgorithmException {
        HashMap<String, String> keyMap = new HashMap<>();
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        keyPairGen.initialize(1024, new SecureRandom());  // 建议将密钥长度改为 2048
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        String publicKeyStr = Base64Utils.encodeToString(publicKey.getEncoded());
        String privateKeyStr = Base64Utils.encodeToString(privateKey.getEncoded());
        keyMap.put("publicKey", publicKeyStr);
        keyMap.put("privateKey", privateKeyStr);
        return keyMap;
    }

    /**
     * 使用 RSA 公钥加密字符串
     *
     * @param str       待加密的字符串
     * @param publicKey 公钥
     * @return 加密后的字符串，以 Base64 编码
     * @throws Exception 加密过程中的异常信息
     */
    public static String encrypt(String str, String publicKey) throws Exception {
        byte[] decoded = Base64Utils.decodeFromString(publicKey);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        RSAPublicKey RSAKey = (RSAPublicKey) keyFactory.generatePublic(new X509EncodedKeySpec(decoded));
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, RSAKey);
        return Base64Utils.encodeToString(cipher.doFinal(str.getBytes("UTF-8")));
    }

    /**
     * 使用 RSA 私钥解密字符串
     *
     * @param str        加密后的字符串
     * @param privateKey 私钥
     * @return 解密后的明文字符串
     * @throws Exception 解密过程中的异常信息
     */
    public static String decrypt(String str, String privateKey) throws Exception {
        byte[] inputByte = Base64Utils.decodeFromString(str);
        byte[] decoded = Base64Utils.decodeFromString(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(decoded);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        RSAPrivateKey RSAKey = (RSAPrivateKey) keyFactory.generatePrivate(pkcs8KeySpec);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, RSAKey);
        return new String(cipher.doFinal(inputByte));
    }

    /**
     * 使用 RSA 私钥解密字符串
     *
     * @param str        加密后的字符串
     * @return 解密后的明文字符串
     * @throws Exception 解密过程中的异常信息
     */
    public static String decrypt(String str) throws Exception {
       return decrypt(str, DEFAULT_PRIVATE_KEY);
    }
}
