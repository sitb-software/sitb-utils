package software.sitb.utils.crypto;

import org.apache.commons.codec.binary.Base64;
import software.sitb.utils.binary.CodecUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;

/**
 * RSA 工具类
 */
public class RSAUtil {

    public static final String DEFAULT_CIPHER = "RSA/ECB/PKCS1Padding";

    /**
     * 默认签名算法
     */
    public static final String DEFAULT_SIGN_ALGORITHMS = "SHA1WithRSA";


    public static final int DEFAULT_PK_BLOCK_SIZE = 53;

    public static final int DEFAULT_PRK_BLOCK_SIZE = 64;

    /**
     * 使用私钥解密
     *
     * @param hexPrivateKey 16进制 表示的私钥
     * @param ciphertext    密文
     * @return 明文
     * @throws Exception 解密失败
     */
    public static String privateKeyDecryptWithHex(String hexPrivateKey, byte[] ciphertext) throws Exception {
        byte[] result = privateKeyDecryptWithDer(hexPrivateKey, ciphertext, Cipher.getInstance(DEFAULT_CIPHER));
        return CodecUtils.hexString(result);
    }

    /**
     * 私钥解密
     *
     * @param hexPrivateKey 16进制 表示的私钥
     * @param ciphertext    密文
     * @param cipher        填充方式
     * @return 解密以后的数据
     */
    public static byte[] privateKeyDecryptWithDer(String hexPrivateKey, byte[] ciphertext, Cipher cipher) throws BadPaddingException, NoSuchAlgorithmException, IOException, IllegalBlockSizeException, InvalidKeyException, InvalidKeySpecException, NoSuchPaddingException {
        return privateKeyDecryptWithDer(hexPrivateKey, 9, ciphertext, cipher);
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
    public static byte[] privateKeyDecryptWithDer(String privateKeyStr, int sequence, byte[] data, Cipher cipher) throws NoSuchAlgorithmException, IOException, InvalidKeySpecException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, NoSuchPaddingException {
        PrivateKey privateKey = RSA.getPrivateKey(privateKeyStr, sequence);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return cipher.doFinal(data);
    }

    /**
     * 私钥解密
     *
     * @param privateKeyBase64 私钥
     * @param encrypted        密文
     * @return 解密后的数据
     */
    public static String privateKeyDecryptWithBase64(String privateKeyBase64, byte[] encrypted) throws Exception {
        return privateKeyDecryptWithBase64(privateKeyBase64, encrypted, Cipher.getInstance(DEFAULT_CIPHER));
    }

    /**
     * 私钥解密
     *
     * @param privateKeyBase64 私钥
     * @param encrypted        密文
     * @param cipher           填充方式
     * @return 解密后的数据
     */
    public static String privateKeyDecryptWithBase64(String privateKeyBase64, byte[] encrypted, Cipher cipher) throws Exception {
        PrivateKey privateKey = RSA.parsePrivateKeyWithBase64(privateKeyBase64);
        return Base64.encodeBase64String(privateKeyDecrypt(privateKey, encrypted, cipher));
    }

    /**
     * 私钥解密 使用默认的解密算法进行解密<code>RSA/ECB/PKCS1Padding</code>
     *
     * @param privateKey 私钥
     * @param encrypted  加密的数据
     * @return 明文
     * @throws Exception 解密失败
     */
    public static byte[] privateKeyDecrypt(PrivateKey privateKey, byte[] encrypted) throws Exception {
        return privateKeyDecrypt(privateKey, encrypted, Cipher.getInstance(DEFAULT_CIPHER));
    }

    public static byte[] privateKeyDecrypt(PrivateKey privateKey, byte[] encrypted, Cipher cipher) throws Exception {
        return privateKeyDecrypt(privateKey, encrypted, cipher, DEFAULT_PRK_BLOCK_SIZE);
    }

    public static byte[] privateKeyDecrypt(PrivateKey privateKey, byte[] encrypted, Cipher cipher, int blockSize) throws Exception {
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            for (int offset = 0; offset < encrypted.length; offset += blockSize) {
                int inputLen = encrypted.length - offset;
                if (inputLen > blockSize) {
                    inputLen = blockSize;
                }

                byte[] decryptedBlock = cipher.doFinal(encrypted, offset, inputLen);
                outputStream.write(decryptedBlock);
            }

            return outputStream.toByteArray();
        } catch (IllegalBlockSizeException e) {
            throw new Exception("解密块大小不合法", e);
        } catch (BadPaddingException e) {
            throw new Exception("错误填充模式", e);
        } catch (IOException e) {
            throw new Exception("字节输出流异常", e);
        }
    }

    /**
     * 使用指定的公钥进行加密,
     * 填充方式默认使用RSA/ECB/NoPadding
     *
     * @param hexPublicKey 十六进制表示DER编码的公钥字符串
     * @param plaintext    需要加密的数据
     * @return TheEncryptedData    经加密后的数据 十六进制表示
     */
    public static String publicKeyEncryptWithHex(String hexPublicKey, byte[] plaintext) throws Exception {
        return publicKeyEncryptWithHex(hexPublicKey, plaintext, Cipher.getInstance(DEFAULT_CIPHER));
    }

    /**
     * 根据hex类型公钥 对数据进行加密
     * 首先公钥得到mod和exp然后生成对象，根据对象对数据进行加密
     *
     * @param plaintext    待加密数据（不足8位或8的倍数补F）
     * @param cipher       注意填充方式：
     *                     1:如果调用了加密机【RSA/ECB/NoPadding】
     *                     2:此代码用了默认补位方式【RSA/None/PKCS1Padding】，不同JDK默认的补位方式可能不同，如Android默认是RSARSA/ECB/PKCS1Padding
     * @param hexPublicKey 十六进制表示DER编码的公钥字符串
     * @return TheEncryptedData    经加密后的数据 十六进制表示
     */
    public static String publicKeyEncryptWithHex(String hexPublicKey, byte[] plaintext, Cipher cipher) throws Exception {
        PublicKey publicKey = RSA.parsePublicKeyWithHex(hexPublicKey);
        byte[] cipherData = publicKeyEncrypt(publicKey, plaintext, cipher);
        return CodecUtils.hexString(cipherData);
    }

    /**
     * 使用指定的公钥进行加密,
     * 填充方式默认使用RSA/ECB/NoPadding
     *
     * @param publicKey 公钥
     * @param plaintext 需要加密的数据
     * @return TheEncryptedData    经加密后的数据 十六进制表示
     */
    public static String publicKeyEncryptWithHex(PublicKey publicKey, byte[] plaintext) throws Exception {
        return publicKeyEncryptWithHex(publicKey, plaintext, Cipher.getInstance(DEFAULT_CIPHER));
    }

    /**
     * 使用指定的公钥进行加密,
     *
     * @param publicKey 公钥
     * @param plaintext 需要加密的数据
     * @param cipher    cipher
     * @return TheEncryptedData    经加密后的数据 十六进制表示
     */
    public static String publicKeyEncryptWithHex(PublicKey publicKey, byte[] plaintext, Cipher cipher) throws Exception {
        byte[] cipherData = publicKeyEncrypt(publicKey, plaintext, cipher);
        return CodecUtils.hexString(cipherData);
    }

    /**
     * 使用指定的公钥进行加密,
     * 填充方式默认使用RSA/ECB/NoPadding
     *
     * @param base64PublicKey base64表示的公钥字符串
     * @param plaintext       需要加密的数据
     * @return TheEncryptedData    经加密后的数据 base64
     */
    public static String publicKeyEncryptWithBase64(String base64PublicKey, byte[] plaintext) throws Exception {
        return publicKeyEncryptWithBase64(base64PublicKey, plaintext, Cipher.getInstance(DEFAULT_CIPHER));
    }

    /**
     * 使用指定的公钥进行加密,
     *
     * @param base64PublicKey base64表示的公钥字符串
     * @param plaintext       需要加密的数据
     * @param cipher          填充方式
     * @return TheEncryptedData    经加密后的数据 base64
     */
    public static String publicKeyEncryptWithBase64(String base64PublicKey, byte[] plaintext, Cipher cipher) throws Exception {
        PublicKey publicKey = RSA.parsePublicKeyWithBase64(base64PublicKey);
        return publicKeyEncryptWithBase64(publicKey, plaintext, cipher);
    }

    /**
     * 使用指定的公钥进行加密,
     *
     * @param publicKey 公钥
     * @param plaintext 需要加密的数据
     * @return TheEncryptedData    经加密后的数据 base64
     */
    public static String publicKeyEncryptWithBase64(PublicKey publicKey, byte[] plaintext) throws Exception {
        byte[] ciphertext = publicKeyEncrypt(publicKey, plaintext, Cipher.getInstance(DEFAULT_CIPHER));
        return Base64.encodeBase64String(ciphertext);
    }

    /**
     * 使用指定的公钥进行加密,
     *
     * @param publicKey 公钥
     * @param plaintext 需要加密的数据
     * @param cipher    填充方式
     * @return TheEncryptedData    经加密后的数据 base64
     */
    public static String publicKeyEncryptWithBase64(PublicKey publicKey, byte[] plaintext, Cipher cipher) throws Exception {
        byte[] ciphertext = publicKeyEncrypt(publicKey, plaintext, cipher);
        return Base64.encodeBase64String(ciphertext);
    }

    /**
     * 公钥加密,使用填充方式RSA/ECB/NoPadding
     *
     * @param publicKey 公钥
     * @param plaintext 明文
     * @return 加密后的数据
     * @throws Exception 加密发生异常
     */
    public static byte[] publicKeyEncrypt(PublicKey publicKey, byte[] plaintext) throws Exception {
        return publicKeyEncrypt(publicKey, plaintext, Cipher.getInstance(DEFAULT_CIPHER));
    }

    public static byte[] publicKeyEncrypt(PublicKey publicKey, byte[] plaintext, Cipher cipher) throws Exception {
        return publicKeyEncrypt(publicKey, plaintext, cipher, DEFAULT_PK_BLOCK_SIZE);
    }

    /**
     * 公钥加密
     *
     * @param publicKey 公钥
     * @param plaintext 待加密的数据(明文)
     * @param cipher    cipher
     * @return 加密后的数据
     */
    public static byte[] publicKeyEncrypt(PublicKey publicKey, byte[] plaintext, Cipher cipher, int blockSize) throws Exception {
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            for (int offset = 0; offset < plaintext.length; offset += blockSize) {
                int inputLen = plaintext.length - offset;
                if (inputLen > blockSize) {
                    inputLen = blockSize;
                }

                byte[] encryptedBlock = cipher.doFinal(plaintext, offset, inputLen);
                outputStream.write(encryptedBlock);
            }
            return outputStream.toByteArray();
        } catch (IllegalBlockSizeException e) {
            throw new Exception("加密块大小不合法", e);
        } catch (BadPaddingException e) {
            throw new Exception("错误填充模式", e);
        } catch (IOException e) {
            throw new Exception("字节输出流异常", e);
        }
    }

    public static String signWithBase64(String privateKeyStr, String data) throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, SignatureException {
        return signWithBase64(privateKeyStr, DEFAULT_SIGN_ALGORITHMS, data);
    }

    /**
     * 对数据进行签名
     *
     * @param privateKeyStr base64 格式的私钥
     * @param algorithm     签名所用的algorithm
     * @param data          待签名的数据
     * @return 签名
     * @throws InvalidKeySpecException  InvalidKeySpecException
     * @throws NoSuchAlgorithmException NoSuchAlgorithmException
     * @throws SignatureException       SignatureException
     * @throws InvalidKeyException      InvalidKeyException
     */
    public static String signWithBase64(String privateKeyStr, String algorithm, String data) throws InvalidKeySpecException, NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        byte[] dataBytes = Base64.decodeBase64(data);
        PrivateKey privateKey = RSA.parsePrivateKeyWithBase64(privateKeyStr);
        byte[] sign = sign(privateKey, algorithm, dataBytes);
        return Base64.encodeBase64String(sign);
    }

    public static byte[] sign(PrivateKey privateKey, String algorithm, byte[] data) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature signature = Signature.getInstance(algorithm);
        signature.initSign(privateKey);
        signature.update(data);
        return signature.sign();
    }

    public static boolean signVerifyWithBase64(String publicKey, String data, String signature) throws InvalidKeySpecException, NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        return signVerifyWithBase64(publicKey, DEFAULT_SIGN_ALGORITHMS, data, signature);
    }

    public static boolean signVerifyWithBase64(String publicKey, String algorithm, String data, String signature) throws InvalidKeySpecException, NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        return signVerify(
                RSA.parsePublicKeyWithBase64(publicKey),
                algorithm,
                Base64.decodeBase64(data),
                Base64.decodeBase64(signature)
        );
    }

    public static boolean signVerify(PublicKey publicKey, String algorithm, byte[] data, byte[] signature) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature sign = Signature.getInstance(algorithm);
        sign.initVerify(publicKey);
        sign.update(data);
        return sign.verify(signature);
    }
}
