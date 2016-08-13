package developer.github.producer;

import developer.github.async.AsyncProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @package: developer.github.producer
 * @class:
 * @author: 黄鹏
 * @date: 2016年08月11日 上午10:39
 * @description:
 */
@Slf4j
@Service("errorLogAsyncProducer")
public class ErrorLogAsyncProducer extends TopicAsyncProducer implements AsyncProducer {
    /**
     * exchange:
     * topic: defaultRoutingKey
     * direct: queue name
     *
     * @return
     */
    @Override
    protected String getDefaultRoutingKey() {
        return "error.log.async";
    }
}
