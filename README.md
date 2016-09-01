# async-event #
    java async event coding by rabbitmq (cluster)

# process #
    server --> (send a message) --> rabbitmq --> exhange --> queue --> consumers server

# required #
    1. this module dependency https://github.com/TigerPengPeng/core.git.
       first, you need git clone https://github.com/TigerPengPeng/core.git to local
       and then, run mvn clean install -DskipTests -U
    2. if you are using ide to import this model, you need install plugins lombok
    3. you need to connect to rabbitmq server. the connection config in sys.rabbitmq.config.properties
    4. last, you can use maven to build module
       mvn build
       mvn clean install -DskipTests -U
       or
       mvn clean install -Dscale=/ (if you can connect to your rabbitmq server)

# samples #
    sample is at dir test.
    1. define producer(s)
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

    2. define consumer(s)
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

    3. define event
        @Data
        public class ExampleEvent {
            private String message;
        }

    4. define listeners
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

    5. last, push message
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

    then. you can see that rabbitmq push message to consumer(s), and consumer(s) execute method:
    public void exampleEventListener1(ExampleEvent event) {...}
    and
    public void exampleEventListener2(ExampleEvent event) {...}




rabbitmq docs: http://www.rabbitmq.com/tutorials/tutorial-five-java.html
