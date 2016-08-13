package developer.github.utils;

import developer.github.model.Second;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.*;

/**
 * @package: developer.github.utils
 * @class:
 * @author: 黄鹏
 * @date: 2016年08月10日 下午10:39
 * @description:
 */
public class ReflectionUtilsTest {

    @Test
    public void testGetSuperClasses() throws Exception {
        Second second = new Second();
        Set<Class<?>> supers = ReflectionUtils.getSuperClasses(second.getClass());
        System.out.println(supers);
    }
}