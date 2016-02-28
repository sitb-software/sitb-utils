package software.sitb.utils.crypto;


import software.sitb.utils.binary.CodecUtils;

public class DesUtils {

    public static byte[] desEncrypt(byte[] input, byte[] key) {
        SimpleDESCrypto crypto = new SimpleDESCrypto(key);

        return crypto.encrypt(input);
    }

    public static byte[] desDecrypt(byte[] input, byte[] key) {
        SimpleDESCrypto crypto = new SimpleDESCrypto(key);

        return crypto.decrypt(input);
    }

    public static byte[] tripDesEncrypt(byte[] input, byte[] key) {
        if (key.length == 8) {
            return desEncrypt(input, key);
        } else if (key.length == 16) {
            byte[] key1 = new byte[8];
            byte[] key2 = new byte[8];
            System.arraycopy(key, 0, key1, 0, 8);
            System.arraycopy(key, 8, key2, 0, 8);

            return tripDesEncrypt(input, key1, key2, key1);
        } else if (key.length == 24) {
            byte[] key1 = new byte[8];
            byte[] key2 = new byte[8];
            byte[] key3 = new byte[8];
            System.arraycopy(key, 0, key1, 0, 8);
            System.arraycopy(key, 8, key2, 0, 8);
            System.arraycopy(key, 16, key3, 0, 8);

            return tripDesEncrypt(input, key1, key2, key3);
        } else {
            throw new IllegalArgumentException("tripDesEncrypt only support keylength:8,16,24");
        }
    }

    /**
     * 3des 加密,参数都是16进制字符串
     *
     * @param data 待加密数据
     * @param key  密钥
     * @return 加密后的数据
     */
    public static String tripDesEncrypt(String data, String key) {
        byte[] byteData = CodecUtils.hex2byte(data);
        byte[] byteKey = CodecUtils.hex2byte(key);
        byte[] result = tripDesEncrypt(byteData, byteKey);
        return CodecUtils.hexString(result);
    }

    public static byte[] tripDesDecrypt(byte[] input, byte[] key) {
        if (key.length == 8) {
            return desDecrypt(input, key);
        } else if (key.length == 16) {
            byte[] key1 = new byte[8];
            byte[] key2 = new byte[8];
            System.arraycopy(key, 0, key1, 0, 8);
            System.arraycopy(key, 8, key2, 0, 8);
            return tripDesDecrypt(input, key1, key2, key1);
        } else if (key.length == 24) {
            byte[] key1 = new byte[8];
            byte[] key2 = new byte[8];
            byte[] key3 = new byte[8];
            System.arraycopy(key, 0, key1, 0, 8);
            System.arraycopy(key, 8, key2, 0, 8);
            System.arraycopy(key, 16, key3, 0, 8);

            return tripDesDecrypt(input, key1, key2, key3);
        } else {
            throw new IllegalArgumentException("tripDesDecrypt only support keylength:8,16,24");
        }
    }

    public static String tripDesDecrypt(String data, String key) {
        byte[] byteData = CodecUtils.hex2byte(data);
        byte[] byteKey = CodecUtils.hex2byte(key);
        byte[] result = tripDesDecrypt(byteData,byteKey);
        return CodecUtils.hexString(result);
    }

    public static byte[] tripDesEncrypt(byte[] input, byte[] key1, byte[] key2, byte[] key3) {
        byte[] rslt = desEncrypt(input, key1);
        rslt = desDecrypt(rslt, key2);
        rslt = desEncrypt(rslt, key3);
        return rslt;
    }

    public static byte[] tripDesDecrypt(byte[] input, byte[] key1, byte[] key2, byte[] key3) {
        byte[] rslt = desDecrypt(input, key3);
        rslt = desEncrypt(rslt, key2);
        rslt = desDecrypt(rslt, key1);
        return rslt;
    }

}
