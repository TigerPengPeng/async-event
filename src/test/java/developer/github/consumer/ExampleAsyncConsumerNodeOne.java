package developer.github.consumer;

import developer.github.async.AsyncConsumer;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * @package: developer.github.consumer
 * @class:
 * @author: 黄鹏
 * @date: 2016年08月11日 下午5:56
 * @description:
 */
@Service("exampleAsyncConsumerNodeOne")
public class ExampleAsyncConsumerNodeOne extends TopicAsyncConsumer implements AsyncConsumer {
    @Override
    public String getQueueName() {
        return "example_queue";
    }

    @Override
    public List<String> getBindingKeys() {
        return Arrays.asList("example.*");
    }
}
