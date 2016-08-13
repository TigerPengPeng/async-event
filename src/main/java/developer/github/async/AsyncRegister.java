package developer.github.async;


import developer.github.annotaion.Async;
import developer.github.utils.ReflectionUtils;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @package: developer.github.async
 * @class:
 * @author: 黄鹏
 * @date: 2016年08月10日 下午10:15
 * @description:
 */
@Service("register")
public class AsyncRegister {
    private Map<Class<?>, List<AsyncSubscriber>> register;

    public AsyncRegister() {
        register = new HashMap<>();
    }

    public List<AsyncSubscriber> get(Class<?> clazz) {
        if (clazz == null) {
            return null;
        }
        return register.get(clazz);
    }

    public void register(Object listener) {
        if (listener == null) {
            return;
        }
        Class<?> clazz = listener.getClass();
        for (Method method : getAnnotatedMethods(clazz)) {
            Class<?>[] parameterTypes = method.getParameterTypes();
            Class<?> eventType = parameterTypes[0];
            AsyncSubscriber subscriber = makeSubscriber(listener, method);
            add(eventType, subscriber);
        }
    }

    private Collection<Method> getAnnotatedMethods(Class<?> clazz) {
        Set<? extends Class<?>> supers = ReflectionUtils.getSuperClasses(clazz);
        Set<Method> annotationMethods = new HashSet<>();

        for (Class<?> superClass : supers) {
            Method[] methods = superClass.getDeclaredMethods();
            for (Method method : methods) {
                Async annotation = method.getAnnotation(Async.class);
                if (annotation != null && !method.isBridge()) {
                    annotationMethods.add(method);
                }
            }
        }
        return annotationMethods;
    }

    private AsyncSubscriber makeSubscriber(Object listener, Method method) {
        return new AsyncSubscriber(listener, method);
    }

    private void add(Class<?> clazz, AsyncSubscriber subscriber) {
        if (clazz == null || subscriber == null) {
            return;
        }
        if (register.containsKey(clazz)) {
            List<AsyncSubscriber> subscribers = register.get(clazz);
            subscribers.add(subscriber);
        } else {
            List<AsyncSubscriber> subscribers = new LinkedList<>();
            subscribers.add(subscriber);
            register.put(clazz, subscribers);
        }
    }

    public void clear() {
        register = null;
    }
}
