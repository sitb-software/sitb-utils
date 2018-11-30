import org.junit.Test;
import software.sitb.utils.netty.NettyClient;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * @author 田尘殇Sean Create At 2018/11/30 14:30
 */
public class NettyClientTest {

    @Test
    public void test() throws Exception {
        NettyClient client = new NettyClient.Builder()
                .host("ccwc.fengcg.com")
                .port(443)
                .addHandler(new StringDecoder())
                .addHandler(new StringEncoder())
                .build();

        System.out.println((String) client.send("GET /api/v1"));
    }
}
