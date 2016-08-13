package developer.github.producer;

import developer.github.async.AsyncProducer;
import org.springframework.stereotype.Service;

/**
 * @package: developer.github.producer
 * @class:
 * @author: 黄鹏
 * @date: 2016年08月11日 下午5:54
 * @description:
 */
@Service("exampleAsyncProducer")
public class ExampleAsyncProducer extends TopicAsyncProducer implements AsyncProducer {
    /**
     * exchange:
     * topic: defaultRoutingKey
     * direct: queue name
     *
     * @return
     */
    @Override
    protected String getDefaultRoutingKey() {
        return "example.async";
    }
}
