package developer.github.event;

import lombok.Getter;

import java.io.Serializable;

/**
 * @package: developer.github.event
 * @class:
 * @author: 黄鹏
 * @date: 2016年08月10日 下午10:55
 * @description:
 */
public class ErrorLogEvent implements Serializable {
    @Getter
    private String object;

    @Getter
    private String throwable;

    private ErrorLogEvent() {}

    public ErrorLogEvent(String _object, String _t) {
        object = _object;
        throwable = _t;
    }
}
