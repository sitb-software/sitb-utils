import org.junit.Test;
import software.sitb.utils.http.HttpUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 田尘殇Sean(sean.snow@live.com) createAt 2016/12/2
 */
public class HttpUtilsTest {

    @Test
    public void test() throws IllegalAccessException {
        Map<String, String> test = new HashMap<>();
        test.put("abc", "sssssss");
        test.put("bdc", "sssssss");
        test.put("zzz", "sssssss");
        test.put("ccc", "sssssss");

        System.out.println(HttpUtils.toUrlArgs(test));
    }


}
