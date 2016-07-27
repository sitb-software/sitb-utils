package software.sitb.utils.crypto;

import software.sitb.utils.binary.CodecUtils;

import javax.crypto.Cipher;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;

/**
 * RSA 工具类
 */
public class RSAUtil {


    /**
     * 使用指定的公钥进行加密,
     * 填充方式默认使用RSA/ECB/NoPadding
     *
     * @param publicKeyStr 16进制的公钥字符串
     * @param data         需要加密的数据
     * @return 16进制表示的加密后数据
     */
    public static String encryptWithHexStr(String publicKeyStr, byte[] data) throws Exception {
        return encryptWithHexStr(publicKeyStr, data, Cipher.getInstance("RSA/ECB/NoPadding"));
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
    public static String encryptWithHexStr(String publicKeyStr, byte[] data, Cipher cipher) throws Exception {
        byte[] cipherData = encryptToByte(publicKeyStr, data, cipher);
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
     * @param publicKeyStr 公钥字符串
     * @return 经加密后的数据(二进制)
     */
    public static byte[] encryptToByte(String publicKeyStr, byte[] data, Cipher cipher) throws Exception {

        //解析公钥串，拿到Modulus，Exponent
        String[] pubModExp = RSA.parsePublicKey(publicKeyStr);

        // 公钥初始化
        String mod = pubModExp[0];
        String exp = pubModExp[1];

        // 根据给定的16进制系数和专用指数字符串构造一个RSA专用的公钥对象
        RSAPublicKeySpec publicKeySpec = RSA.getRSAPublicKey(mod, exp);

        //根据公钥对象拿到公钥
        // 公钥
        PublicKey publicKey = RSA.getPublicKey(publicKeySpec);

//        switch (padding) {
//            case 1:
//                // JDK实现是"RSA/None/PKCS1Padding
//                cipher = Cipher.getInstance("RSA/ECB/NoPadding");
//                break;
//            case 2:
//                cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
//                break;
//            default:
//                cipher = Cipher.getInstance("RSA/ECB/NoPadding", new org.bouncycastle.jce.provider.BouncyCastleProvider());
//                // cipher = Cipher.getInstance("RSA/None/PKCS1Padding");
//                // Cipher cipher = Cipher.getInstance("RSA/None/NoPadding");//android系统的RSA实现是"RSA/None/NoPadding
//                break;
//        }

        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        // 加密数据
        return cipher.doFinal(data);
    }

}
