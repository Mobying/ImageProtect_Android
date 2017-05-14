package bying.imageprotect.util;

import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES算法编程实现
 *
 * @author xzb
 */
public class AES256Util {

    //改成256位的
    public static final String CIPHER_ALGORITHM="AES/ECB/PKCS7Padding";

    /**
     * 生成密钥
     *
     * @throws Exception
     */
    public static byte[] initKey() throws Exception {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        //密钥生成器
        KeyGenerator keyGen = KeyGenerator.getInstance("AES","BC");
        //初始化密钥生成器
        keyGen.init(256); //默认128，获得无政策权限后可用192或256
        //生成密钥
        SecretKey secretKey = keyGen.generateKey();
        return secretKey.getEncoded();
    }

    /**
     * 加密
     *
     * @throws Exception
     */
    public static byte[] encryptAES(byte[] data, byte[] key) throws Exception {
        //恢复密钥
        SecretKey secretKey = new SecretKeySpec(key, "AES");
        //Cipher完成加密
        //Cipher cipher = Cipher.getInstance("AES");
        Cipher cipher=Cipher.getInstance(CIPHER_ALGORITHM, "BC");
        //根据密钥对cipher进行初始化
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        //加密
        byte[] encrypt = cipher.doFinal(data);
        return encrypt;
    }

    /**
     * 解密
     */
    public static byte[] decryptAES(byte[] data, byte[] key) throws Exception {
        //恢复密钥生成器
        SecretKey secretKey = new SecretKeySpec(key, "AES");
        //Cipher完成解密
        //Cipher cipher = Cipher.getInstance("AES");//128位
        Cipher cipher=Cipher.getInstance(CIPHER_ALGORITHM, "BC");
        //根据密钥对cipher进行初始化
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] plain = cipher.doFinal(data);
        return plain;
    }
}
