package developer.github.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @package: developer.github.utils
 * @class:
 * @author: 黄鹏
 * @date: 2016年08月10日 下午10:21
 * @description:
 */
@Slf4j
public class ReflectionUtils {

    /**
     * get collection of union(clazz, clazz's all super classes)
     * @param clazz
     * @return
     */
    public static Set<Class<?>> getSuperClasses(Class<?> clazz) {
        Set<Class<?>> supers = new HashSet();

        Class<?> added = clazz;
        while (added != null) {
            supers.add(added);
            added = added.getSuperclass();
        }

        return supers;
    }

    /**
     * 获取类实例的属性值
     * @param clazz 类名
     * @param includeParentClass 是否包括父类的属性值
     * @return 类名.属性名=属性
     */
    public static Map<String, Field> getClassFields(Class clazz, boolean includeParentClass) {
        Map<String, Field> map = new HashMap<>();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            map.put(field.getName(), field);
        }
        if (includeParentClass) {
            getParentClassFields(map, clazz.getSuperclass());
        }
        return map;
    }

    /**
     * 获取类实例的父类的属性值
     * @param map 类实例的属性值Map
     * @param clazz 类名
     * @return 类名.属性名=属性
     */
    private static Map<String, Field> getParentClassFields(Map<String, Field> map, Class clazz) {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            map.put(field.getName(), field);
        }
        if (clazz.getSuperclass() == null) {
        } else {
            getParentClassFields(map, clazz.getSuperclass());
        }
        return map;
    }

    public static Field getField(Class clazz, String fieldName) {
        Map<String, Field> map = getClassFields(clazz, true);
        return map.get(fieldName);
    }

    public static <T> T getFieldValue(Object target, String fieldName) {
        if (target == null) {
            return null;
        }
        Field field = getField(target.getClass(), fieldName);
        return getFieldValue(field, target);
    }

    public static <T> T getFieldValue(Field field, Object target) {
        if (target == null) {
            return null;
        }
        if (field == null) {
            return null;
        }
        field.setAccessible(true);
        Object value = null;
        try {
            value = field.get(target);
        } catch (IllegalAccessException e) {
            log.error("{}", e);
        }
        if (value == null) {
            return null;
        }
        return (T) value;
    }

    /**
     * 对collection 中的 元素 T, update T.xxxField = update.xxxField (if update.xxxField is not null)
     * @param collection
     * @param update
     * @param <T>
     * @return
     */
    public static <T> void setNotNullField(Collection<T> collection, Object update) {
        if (CollectionUtils.isEmpty(collection)) {
            return;
        }
        if (update == null) {
            return;
        }

        Map<String, Field> updateFields = getClassFields(update.getClass(), true);
        for (T object : collection) {
            if (object == null) {
                continue;
            }

            Map<String, Field> objectFields = getClassFields(object.getClass(), true);
            for (String field : objectFields.keySet()) {
                Field objectField = objectFields.get(field);

                Field updateField = updateFields.get(field);
                if (updateField == null) {
                    continue;
                }

                Object updateFieldValue = null;
                try {
                    updateFieldValue = updateField.get(update);
                } catch (IllegalAccessException e) {
                    log.error("{}", e);
                }
                if (updateFieldValue == null) {
                    continue;
                }

                try {
                    objectField.set(object, updateFieldValue);
                } catch (IllegalAccessException e) {
                    log.error("{}", e);
                }
            }
        }
    }

    /**
     * 将集合生成map, map的key是集合内对象的id, map的value是集合内的对象
     * 集合内的对象一定要有field: fieldName && field的类型为Long, 否则程序报错
     * @param collection
     * @param fieldName
     * @param <T>
     * @return
     */
    public static <T> Map<Long, T> makeObjectMap(Collection<T> collection, String fieldName) {
        if (CollectionUtils.isEmpty(collection)) {
            return new HashMap<>();
        }
        Map<Long, T> map = new HashMap<>();
        for (T item : collection) {
            try {
                Field field = getField(item.getClass(), fieldName);
                field.setAccessible(true);
                Long id = (Long) field.get(item);
                map.put(id, item);

            } catch (IllegalAccessException e) {
                e.printStackTrace();
                log.error(e.getMessage());
            }
        }
        return map;
    }

    /**
     * 将collection中的对象中id属性提取出来, 放在set中
     * 集合内的对象一定要有field: fieldName && field的类型为Long, 否则程序报错
     * @param collection
     * @param fieldName
     * @return
     */
    public static <T> Set<Long> makeCollectionIdSet(Collection<T> collection, String fieldName) {
        List<Long> list = makeCollectionIdList(collection, fieldName);
        return new HashSet<>(list);
    }

    /**
     *
     * @param collection
     * @param fieldName
     * @param <T>
     * @return
     */
    public static <T> List<Long> makeCollectionIdList(Collection<T> collection, String fieldName) {
        if (CollectionUtils.isEmpty(collection)) {
            return new LinkedList<>();
        }
        List<Long> result = new LinkedList();
        for (T item : collection) {
            try {
                Field field = getField(item.getClass(), fieldName);
                field.setAccessible(true);
                Long id = (Long) field.get(item);
                result.add(id);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                log.error(e.getMessage());
            }

        }
        return result;
    }

    /**
     * 对collection中的item按照sequence的顺序进行排序, 需要保证map中的key都在sequence列表中
     * @param collection
     * @param sequence
     * @return
     */
    public static <T> List<T> sortMapBySequence(Map<Long, T> collection, List<Long> sequence) {
        List<T> sortedList = new LinkedList<>();
        for (Long order : sequence) {
            T task = collection.get(order);
            if (task != null) {
                sortedList.add(task);
            }
        }
        return sortedList;
    }

    /**
     * 对collection中的item按照sequence的顺序进行排序, 需要保证map中的key都在sequence列表中
     * @param collection
     * @param fieldName
     * @param sequence
     * @param <T>
     * @return
     */
    public static <T> List<T> sortCollectionBySequence(Collection<T> collection, String fieldName, List<Long> sequence) {
        Map<Long, T> map = makeObjectMap(collection, fieldName);
        return sortMapBySequence(map, sequence);
    }
}
