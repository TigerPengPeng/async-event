package developer.github.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @package: developer.github.utils
 * @class:
 * @author: 黄鹏
 * @date: 2016年08月11日 上午11:16
 * @description:
 */
public class ExceptionUtils {

    public static String getThrowableString(Throwable t) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        t.printStackTrace(printWriter);
        return stringWriter.toString();
    }
}
