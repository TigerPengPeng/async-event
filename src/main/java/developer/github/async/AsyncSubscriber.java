package developer.github.async;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;

/**
 * @package: developer.github.async
 * @class:
 * @author: 黄鹏
 * @date: 2016年08月10日 下午9:52
 * @description:
 */
@Slf4j
public class AsyncSubscriber {
    private final Object target;

    private final Method method;

    public AsyncSubscriber(Object _object, Method _method) {
        if (_object == null || _method == null) {
            log.error("{}", "create AsyncSubscriber object failed: either object or method can not be null");
            throw new IllegalArgumentException("create AsyncSubscriber object failed: either object or method can not be null");
        }
        target = _object;
        method = _method;
        method.setAccessible(true);
    }

    public void invokeMethod(Object parameter) throws Throwable {
        method.invoke(target, parameter);
    }
}
