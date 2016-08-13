package developer.github.async;

import lombok.Getter;

/**
 * @package: developer.github.async
 * @class:
 * @author: 黄鹏
 * @date: 2016年08月10日 下午10:50
 * @description:
 */
public class AsyncBody {
    /**
     * 该class必须implements序列化, 必须有默认的构造方法
     */
    @Getter
    private Object object;

    @Getter
    private Class<?> event;

    private AsyncBody() {}

    public AsyncBody(Object _object) {
        object = _object;
        event = _object.getClass();
    }
}
