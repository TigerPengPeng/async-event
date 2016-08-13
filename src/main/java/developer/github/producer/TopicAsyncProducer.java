package developer.github.producer;

import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;
import java.io.IOException;

/**
 * @package: developer.github.producer
 * @class:
 * @author: 黄鹏
 * @date: 2016年08月11日 上午10:38
 * @description:
 */
@Slf4j
public abstract class TopicAsyncProducer extends AbstractAsyncProducer {
    @PostConstruct
    public final void init() {
        try {
            channel.exchangeDeclare(getExchange(), getType(), true, false, null);
        } catch (IOException e) {
            log.error("{}", e);
        } catch (Throwable t) {
            log.error("{}", t);
        }
    }

    @Override
    protected String getType() {
        return "topic";
    }

    @Override
    protected String getExchange() {
        return "topic.async";
    }
}
