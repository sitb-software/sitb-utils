package software.sitb.utils.crypto;

import software.sitb.utils.binary.CodecUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;

/**
 * RSA 工具类
 */
public class RSAUtil {


    /**
     * 使用指定的公钥进行加密,
     * 填充方式默认使用RSA/ECB/NoPadding
     *
     * @param publicKeyStr DER编码的公钥字符串
     * @param data         需要加密的数据
     * @return 16进制表示的加密后数据
     */
    public static String publicKeyEncryptWithDer(String publicKeyStr, byte[] data) throws Exception {
        return publicKeyEncryptWithDer(publicKeyStr, data, Cipher.getInstance("RSA/ECB/NoPadding"));
    }

    /**
     * 根据hex类型公钥 对数据进行加密
     * 首先公钥得到mod和exp然后生成对象，根据对象对数据进行加密
     *
     * @param data         待加密数据（不足8位或8的倍数补F）
     * @param cipher       注意填充方式：
     *                     1:如果调用了加密机【RSA/ECB/NoPadding】
     *                     2:此代码用了默认补位方式【RSA/None/PKCS1Padding】，不同JDK默认的补位方式可能不同，如Android默认是RSARSA/ECB/PKCS1Padding
     * @param publicKeyStr 公钥字符串
     * @return TheEncryptedData    经加密后的数据(16进制)
     */
    public static String publicKeyEncryptWithDer(String publicKeyStr, byte[] data, Cipher cipher) throws Exception {
        byte[] cipherData = publicKeyEncryptWidthDer(publicKeyStr, data, cipher);
        return CodecUtils.hexString(cipherData);
    }

    /**
     * 根据hex类型公钥 对数据进行加密
     * 首先公钥得到mod和exp然后生成对象，根据对象对数据进行加密
     *
     * @param data         待加密数据（不足8位或8的倍数补F）
     * @param cipher       注意填充方式：
     *                     1:如果调用了加密机【RSA/ECB/NoPadding】
     *                     2:此代码用了默认补位方式【RSA/None/PKCS1Padding】，不同JDK默认的补位方式可能不同，如Android默认是RSARSA/ECB/PKCS1Padding
     *                     RSA/None/NoPadding
     * @param publicKeyStr 公钥字符串
     * @return 经加密后的数据(二进制)
     */
    public static byte[] publicKeyEncryptWidthDer(String publicKeyStr, byte[] data, Cipher cipher) throws Exception {

        //解析公钥串，拿到Modulus，Exponent
        String[] pubModExp = RSA.parsePublicKey(publicKeyStr);

        String mod = pubModExp[0];
        String exp = pubModExp[1];

        // 公钥
        PublicKey publicKey = RSA.getPublicKey(mod, exp);

        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        // 加密数据
        return cipher.doFinal(data);
    }

    public static String privateKeyDecryptWithDer(String privateKeyStr, byte[] data) throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, InvalidKeyException, InvalidKeySpecException, BadPaddingException, IOException {
        byte[] result = privateKeyDecryptWithDer(privateKeyStr, data, Cipher.getInstance("RSA/ECB/NoPadding"));
        return CodecUtils.hexString(result);
    }


    /**
     * 私钥解密
     *
     * @param privateKeyStr 16进制 表示的私钥(der 格式)
     * @param data          需要解密的数据
     * @param cipher        填充方式
     * @return 解密以后的数据
     */
    public static byte[] privateKeyDecryptWithDer(String privateKeyStr, byte[] data, Cipher cipher) throws BadPaddingException, NoSuchAlgorithmException, IOException, IllegalBlockSizeException, InvalidKeyException, InvalidKeySpecException {
        return privateKeyDecryptWithDer(privateKeyStr, 9, data, cipher);
    }

    /**
     * 私钥解密
     *
     * @param privateKeyStr 16进制 表示的私钥(der 格式)
     * @param sequence      私钥段数,如果不包含其他信息,该值为9
     * @param data          需要解密的数据
     * @param cipher        填充方式
     * @return 解密以后的数据
     */
    public static byte[] privateKeyDecryptWithDer(String privateKeyStr, int sequence, byte[] data, Cipher cipher) throws NoSuchAlgorithmException, IOException, InvalidKeySpecException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        PrivateKey privateKey = RSA.getPrivateKey(privateKeyStr, sequence);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return cipher.doFinal(data);
    }

}
