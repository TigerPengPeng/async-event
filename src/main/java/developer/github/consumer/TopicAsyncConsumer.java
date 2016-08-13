package developer.github.consumer;

import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.List;

/**
 * @package: developer.github.consumer
 * @class:
 * @author: 黄鹏
 * @date: 2016年08月11日 上午10:36
 * @description:
 */
public abstract class TopicAsyncConsumer extends AbstractAsyncConsumer {
    @Override
    public Channel declareChannel() throws IOException {
        channel.exchangeDeclare(getExchange(), getType(), true, false, null);
        for (String bindingKey : getBindingKeys()) {
            queue = declareQueue();
            channel.queueBind(queue, getExchange(), bindingKey);
        }
        return channel;
    }

    @Override
    public String getType() {
        return "topic";
    }

    @Override
    public String getExchange() {
        return "topic.async";
    }

    public abstract String getQueueName();

    @Override
    public String declareQueue() throws IOException {
        return channel.queueDeclare(getQueueName(), true, false, false, null).getQueue();
    }

    @Override
    public abstract List<String> getBindingKeys();
}
