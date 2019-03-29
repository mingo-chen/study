package cm.study.asm.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    public static String YYYYMMDDHHmmss = "yyyy-MM-dd HH:mm:ss";

    private static Logger ILOG = LoggerFactory.getLogger(DateUtil.class);

    public static Date fromString(String source, String format) {
        try {
            return new SimpleDateFormat(format).parse(source);
        } catch (Exception e) {
            ILOG.error("date format error: {}", source, e);
            return null;
        }
    }
}
