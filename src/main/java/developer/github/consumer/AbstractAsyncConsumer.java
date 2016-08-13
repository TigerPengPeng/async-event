package developer.github.consumer;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import developer.github.async.AsyncExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;

/**
 * @package: developer.github.consumer
 * @class:
 * @author: 黄鹏
 * @date: 2016年08月11日 上午10:31
 * @description:
 */
@Slf4j
public abstract class AbstractAsyncConsumer {

    @Autowired
    @Qualifier("rabbitConnection")
    private Connection connection;

    @Autowired
    @Qualifier("asyncExecutor")
    private AsyncExecutor syncExecutor;

    /**
     * 多个workers 订阅单个queue的消息, 不同的worker之间的channel需不同
     */
    protected Channel channel;

    /**
     *
     */
    protected String queue;

    /**
     * 多个workers 订阅单个queue的消息, 不同的worker之间的consumer需不同
     */
    protected Consumer consumer;

    protected ExecutorService executorService;

    @PostConstruct
    public final void init() {
        log.info("Init Rabbitmq default virtual host channel.");
        try {
            channel = connection.createChannel();
            declareChannel();
        } catch (Throwable t) {
            log.error("{}", t);
            throw new IllegalStateException("Can't connect to Rabbitmq server.");
        }

        consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                int subscribers = 0;
                try {
                    subscribers = syncExecutor.executeWorks(body);
                    if (subscribers == -1) {
                        channel.basicReject(envelope.getDeliveryTag(), true);
                    }
                } catch (Throwable t) {
                    log.error("{}", t);
                }
                if (subscribers != -1) {
                    channel.basicAck(envelope.getDeliveryTag(), false);
                }
            }
        };

        executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    channel.basicConsume(queue, false, consumer);
                } catch (IOException e) {
                    log.error("{}", e);
                } catch (Throwable t) {
                    log.error("{}", t);
                }
            }
        });
    }

    public abstract Channel declareChannel() throws IOException;

    public abstract String getType();

    public abstract String getExchange();

    public abstract String declareQueue() throws IOException;

    public abstract List<String> getBindingKeys();

    @PreDestroy
    public void destroy() {
        try {
            channel.close();
        } catch (IOException e) {
            log.error("{}", e);
        } catch (TimeoutException e) {
            log.error("{}", e);
        } catch (Throwable t) {
            log.error("{}", t);
        }

        executorService.shutdown();
    }
}
