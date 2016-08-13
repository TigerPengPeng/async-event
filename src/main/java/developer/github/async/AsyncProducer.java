package developer.github.async;

/**
 * @package: developer.github.async
 * @class:
 * @author: 黄鹏
 * @date: 2016年08月10日 下午10:48
 * @description:
 */
public interface AsyncProducer {
    /**
     * 发送持久化的消息
     * 并不一定能保证消息持久化
     * 还需要declare exchange && queue 持久化
     * @param body
     */
    void durabilityPublish(Object body);

    /**
     * 发送多个持久化的消息
     * 并不一定能保证消息持久化
     * 还需要declare exchange && queue 持久化
     * @param objects
     */
    void durabilityPublish(Object... objects);

    /**
     * 发送持久化的消息
     * 并不一定能保证消息持久化
     * 还需要declare exchange && queue 持久化
     * @param body
     * @param routingKey
     */
    void durabilityPublish(Object body, String routingKey);
}
