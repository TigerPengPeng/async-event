package developer.github.listener;

import developer.github.annotaion.Async;
import developer.github.async.AsyncAbstractListener;
import developer.github.event.ExampleEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @package: developer.github.listener
 * @class:
 * @author: 黄鹏
 * @date: 2016年08月11日 下午5:58
 * @description:
 */
@Service("exampleEventListener")
@Slf4j
public class ExampleEventListener extends AsyncAbstractListener {

    @Async
    public void exampleEventListener1(ExampleEvent event) {
        String message = event.getMessage();
        log.info("exampleEventListener1: {}", message);
    }

    @Async
    public void exampleEventListener2(ExampleEvent event) {
        String message = event.getMessage();
        log.info("exampleEventListener2: {}", message);
    }
}
