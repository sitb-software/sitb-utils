import org.junit.Test;
import software.sitb.utils.StringUtils;
import software.sitb.utils.binary.CodecUtils;
import software.sitb.utils.crypto.DesUtils;

import java.io.UnsupportedEncodingException;

/**
 * @author Sean(sean.snow@live.com) 2016/1/7
 */
public class DesUtilsTest {


    @Test
    public void tripDesEncrypt() {
        byte[] key = CodecUtils.hex2byte("88888888888888888888888888888888");
        byte[] data = CodecUtils.hex2byte("A2718333015D9F9D605626A17A242B20");

        try {
            byte[] encrypt = DesUtils.tripDesEncrypt(key, data);
            String result = CodecUtils.hexString(encrypt);
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
