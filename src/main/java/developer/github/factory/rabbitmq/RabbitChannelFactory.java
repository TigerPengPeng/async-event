package developer.github.factory.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import developer.github.utils.ExceptionUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PreDestroy;
import java.io.IOException;

/**
 * @package: developer.github.factory.rabbitmq
 * @class:
 * @author: 黄鹏
 * @date: 2016年08月11日 上午11:10
 * @description:
 */
@Slf4j
@Data
public class RabbitChannelFactory {

    private Channel channel;

    public RabbitChannelFactory(Connection connection) {
        log.info("Init Rabbitmq default virtual host channel.");
        try {
            channel = connection.createChannel();
        } catch (IOException e) {
            log.error("Can't connect to Rabbitmq server.", e);
            throw new IllegalStateException(ExceptionUtils.getThrowableString(e));
        }
    }

    public Channel get() {
        return channel;
    }

    @PreDestroy
    public void destroy() {
        log.info("Close Rabbitmq default virtual host channel.");
        try {
            channel.close();
        } catch (Throwable t) {
            log.info("Ignore exception {}", t);
        }
    }

}
