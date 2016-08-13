package developer.github.annotaion;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @package: developer.github.annotaion
 * @class:
 * @author: 黄鹏
 * @date: 2016年08月10日 下午10:17
 * @description:
 */
@Target({ TYPE, FIELD, PARAMETER, METHOD })
@Retention(RUNTIME)
public @interface Async {}
