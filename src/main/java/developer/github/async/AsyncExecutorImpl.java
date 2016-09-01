package developer.github.async;

import com.fasterxml.jackson.databind.ObjectMapper;
import developer.github.event.ErrorLogEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @package: developer.github.async
 * @class:
 * @author: 黄鹏
 * @date: 2016年08月10日 下午10:52
 * @description:
 */
@Service("asyncExecutor")
@Slf4j
public class AsyncExecutorImpl implements AsyncExecutor {
    @Autowired
    @Qualifier("register")
    protected AsyncRegister asyncRegister;

    @Autowired
    @Qualifier("objectMapper")
    protected ObjectMapper objectMapper;

    @Autowired
    @Qualifier("errorLogAsyncProducer")
    protected AsyncProducer producer;

    protected AsyncBody receiveMessageBody(byte[] bytes) throws Throwable {
        if (bytes == null) {
            return null;
        }

        String value = new String(bytes, "UTF-8");
        log.info("async receive message {}", value);
        JSONObject jsonObject = new JSONObject(value);
        Object objectValue = jsonObject.get("object");
        String className = jsonObject.getString("event");
        Class event = Class.forName(className);

        Object object;
        if (event.equals(String.class)) {
            object = objectValue;
        } else {
            object = objectMapper.readValue(objectValue.toString(), event);
        }

        return new AsyncBody(object);
    }

    protected void pushErrorMessage(Throwable t, String body) {
        ErrorLogEvent errorLogEvent = new ErrorLogEvent(body, ExceptionUtils.getFullStackTrace(t));
        producer.durabilityPublish(errorLogEvent);
    }

    /**
     * 接受消息, 从register中获取执行方法的主要逻辑
     * @param bytes rabbitmq message payload
     * 序列化逻辑
     *        <br>
     *              bytes 需要满足格式{"object": ..., "event": ...}的格式
     *              bytes 反序列化失败, 抛出error log message, return 0
     *              bytes.event class not found, 抛出error log message, return 0
     *        </br>
     * @return
     *         <br>
     *             -1: 保留位, reject message
     *             非-1: 正常执行 or 反序列化失败; ack message
     *         </br>
     * @throws Throwable
     */
    @Override
    public int executeWorks(byte[] bytes) throws Throwable {
        AsyncBody body = null;
        try {
            body = receiveMessageBody(bytes);
        } catch (Throwable t) {
            log.error("{}" , t);
            pushErrorMessage(t, new String(bytes));
        }

        if (body == null) {
            return 0;
        }

        Class<?> event = body.getEvent();
        Object object = body.getObject();

        List<AsyncSubscriber> subscribers = asyncRegister.get(event);
        if (CollectionUtils.isEmpty(subscribers)) {
            log.error("{} find no subscribers", event);
            pushErrorMessage(new RuntimeException(event + "find no subscribers"), new String(bytes));
        }

        // 执行方法中抛错, 发送一条error message
        for (AsyncSubscriber subscriber : subscribers) {
            try {
                subscriber.invokeMethod(object);
            } catch (Throwable t) {
                log.error("{}", t);
                pushErrorMessage(t, new String(bytes));
            }
        }
        return subscribers.size();
    }
}
