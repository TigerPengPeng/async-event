package developer.github;

import developer.github.async.AsyncProducer;
import developer.github.event.ExampleEvent;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class AsyncProducerAndConsumerTest {

	@Test
	public void testSendMessageAndReceive() throws Throwable {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath*:spring/*-applicationContext.xml");

		AsyncProducer producer = context.getBean("exampleAsyncProducer", AsyncProducer.class);
		ExampleEvent exampleEvent = new ExampleEvent();
		exampleEvent.setMessage("this is a message from producer");
		producer.durabilityPublish(exampleEvent);

		Thread.sleep(5000);
	}
}
