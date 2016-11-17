package software.sitb.utils.crypto;

import software.sitb.utils.binary.CodecUtils;
import sun.security.util.DerInputStream;
import sun.security.util.DerValue;

import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateCrtKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

public class RSA {


    /**
     * 根据给定的16进制系数和专用指数字符串构造一个RSA专用的公钥对象。
     * 注意：【此代码用了默认补位方式，为RSA/None/PKCS1Padding，不同JDK默认的补位方式可能不同，如Android默认是RSA
     * /None/NoPadding】 如果调用了加密机【RSA/ECB/NoPadding】
     *
     * @param hexModulus        系数。
     * @param hexPublicExponent 专用指数。
     * @return RSA专用公钥对象。
     */
    public static RSAPublicKeySpec getRSAPublicKey(String hexModulus, String hexPublicExponent) {
        BigInteger m = new BigInteger(hexModulus, 16);
        BigInteger e = new BigInteger(hexPublicExponent, 16);
        return new RSAPublicKeySpec(m, e);
    }


    /**
     * 根据给定的16进制系数和专用指数字符串构造一个RSA专用的公钥对象，并生成公钥。
     * <p>
     * 注意：【此代码用了默认补位方式，为RSA/None/PKCS1Padding，不同JDK默认的补位方式可能不同，如Android默认是RSA
     * /None/NoPadding】 如果调用了加密机【RSA/ECB/NoPadding】
     *
     * @param hexModulus        系数。
     * @param hexPublicExponent 专用指数。
     * @return RSA公钥。
     */
    public static PublicKey getPublicKey(String hexModulus, String hexPublicExponent) {
        RSAPublicKeySpec rsaPubKeySpec = getRSAPublicKey(hexModulus, hexPublicExponent);
        return getPublicKey(rsaPubKeySpec);
    }

    /**
     * 根据 公钥对象生成 公钥
     *
     * @param rsaPublicKeySpec 公钥对象
     * @return 公钥
     */
    public static PublicKey getPublicKey(RSAPublicKeySpec rsaPublicKeySpec) {
        KeyFactory fact;
        PublicKey publicKey = null;
        try {
            fact = KeyFactory.getInstance("RSA");
            publicKey = fact.generatePublic(rsaPublicKeySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e1) {
            e1.printStackTrace();
        }

        return publicKey;
    }


    /**
     * 使用模和指数生成RSA私钥 对象
     * 注意：【此代码用了默认补位方式，为RSA/None/PKCS1Padding，不同JDK默认的补位方式可能不同，如Android默认是RSA
     * /None/NoPadding】 如果调用了加密机【RSA/ECB/NoPadding】
     *
     * @param hexModulus  模
     * @param hexExponent 指数
     * @return 私钥对象
     */
    public static RSAPrivateKeySpec getRSAPrivateKey(String hexModulus, String hexExponent) {
        BigInteger mod = new BigInteger(hexModulus, 16);
        BigInteger exp = new BigInteger(hexExponent, 16);
        return new RSAPrivateKeySpec(mod, exp);
    }

    /**
     * 使用模和指数生成RSA私钥
     * 注意：【此代码用了默认补位方式，为RSA/None/PKCS1Padding，不同JDK默认的补位方式可能不同，如Android默认是RSA
     * /None/NoPadding】 如果调用了加密机【RSA/ECB/NoPadding】
     *
     * @param hexModulus  模
     * @param hexExponent 指数
     * @return 私钥
     */
    public static PrivateKey getPrivateKey(String hexModulus, String hexExponent) {
        RSAPrivateKeySpec privateKeySpec = getRSAPrivateKey(hexModulus, hexExponent);
        return getPrivateKey(privateKeySpec);
    }

    /**
     * 使用私钥对象生成RSA私钥
     * 注意：【此代码用了默认补位方式，为RSA/None/PKCS1Padding，不同JDK默认的补位方式可能不同，如Android默认是RSA
     * /None/NoPadding】 如果调用了加密机【RSA/ECB/NoPadding】
     *
     * @param rsaPrivateKeySpec 私钥对象
     * @return 私钥
     */
    public static PrivateKey getPrivateKey(RSAPrivateKeySpec rsaPrivateKeySpec) {
        KeyFactory keyFactory;
        PrivateKey privateKey = null;

        try {
            keyFactory = KeyFactory.getInstance("RSA");
            privateKey = keyFactory.generatePrivate(rsaPrivateKeySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }

        return privateKey;
    }

    public static PrivateKey getPrivateKey(String hexDerPrivateKey, int sequence) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        DerInputStream dis = new DerInputStream(CodecUtils.hex2byte(hexDerPrivateKey));
        DerValue[] ders = dis.getSequence(sequence);
        //依次读取 RSA 因子信息
//        int version = ders[0].getBigInteger().intValue();
        BigInteger modulus = ders[1].getBigInteger();
        BigInteger publicExponent = ders[2].getBigInteger();
        BigInteger privateExponent = ders[3].getBigInteger();
        BigInteger primeP = ders[4].getBigInteger();
        BigInteger primeQ = ders[5].getBigInteger();
        BigInteger primeExponentP = ders[6].getBigInteger();
        BigInteger primeExponentQ = ders[7].getBigInteger();
        BigInteger crtCoefficient = ders[8].getBigInteger();
        RSAPrivateCrtKeySpec rsaPrivateKeySpec = new RSAPrivateCrtKeySpec(modulus, publicExponent, privateExponent, primeP, primeQ, primeExponentP, primeExponentQ, crtCoefficient);

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(rsaPrivateKeySpec);
    }


    /**
     * 解析公钥字符串，得到Modulus Exponent
     *
     * @param key 公钥的ascii
     * @return mod exp 数组{mod,exp}
     */
    public static String[] parsePublicKey(String key) {
        // 计算key的字节长度并保存到数组
        String[] bytes = new String[key.length() / 2];
        for (int i = 0; i < key.length() / 2; i++) {
            bytes[i] = key.substring(i * 2, i * 2 + 2);
        }
        // Byte Length
        int index = 1;
        int length = Integer.parseInt(bytes[index], 16);
        if (length > 128)
            index += length - 128;

        // Modulus Length
        index += 2;
        length = Integer.parseInt(bytes[index], 16);
        int tmp[] = calLength(bytes, index, length);
        index = tmp[0];
        length = tmp[1];

        // 保存mod值
        StringBuilder modBuff = new StringBuilder();
        for (int i = index + 1; i < index + 1 + length; i++)
            modBuff.append(bytes[i]);

        // Exponent Length
        index += length + 2;
        length = Integer.parseInt(bytes[index], 16);

        tmp = calLength(bytes, index, length);
        index = tmp[0];
        length = tmp[1];

        // 保存exponent值
        index += 1;
        StringBuilder expBuff = new StringBuilder();
        for (int i = index; i < index + length; i++)
            expBuff.append(bytes[i]);

        return new String[]{modBuff.toString(), expBuff.toString()};
    }


    private static int[] calLength(String[] bytes, int index, int length) {
        if (length > 128) {
            int i = length - 128;
            String lenStr = "";
            for (int j = index + 1; j < index + i + 1; j++)
                lenStr += bytes[j];

            index += i;
            length = Integer.parseInt(lenStr, 16);
        }

        return new int[]{index, length};
    }

}
