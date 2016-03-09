import org.junit.Test;
import software.sitb.utils.binary.Base64Utils;

import java.io.File;
import java.io.IOException;

/**
 * @author Sean sean.snow@live.com
 */
public class Base64UtilsTest {

    @Test
    public void toImageFile() throws IOException {
        String s = "iVBORw0KGgoAAAANSUhEUgAAAAkAAAAJAQMAAADaX5RTAAAAA3NCSVQICAjb4U/gAAAABlBMVEX///+ZmZmOUEqyAAAAAnRSTlMA/1uRIrUAAAAJcEhZcwAACusAAArrAYKLDVoAAAAWdEVYdENyZWF0aW9uIFRpbWUAMDkvMjAvMTIGkKG+AAAAHHRFWHRTb2Z0d2FyZQBBZG9iZSBGaXJld29ya3MgQ1M26LyyjAAAAB1JREFUCJljONjA8LiBoZyBwY6BQQZMAtlAkYMNAF1fBs/zPvcnAAAAAElFTkSuQmCC";

        Base64Utils.toImageFile(new File("123.png"), s);
    }
}
