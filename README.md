# async-event

java async event coding by rabbitmq (cluster)

主要流程
server --> (send a message) --> rabbitmq --> exhange --> queue --> consumers server

环境要求
需要能连接到rabbitmq-server, 配置信息在文件
    sys.rabbitmq.config.properties
中更改

    mvn build
    mvn clean install -DskipTests -U


    源码在src目录下
    demo在test目录下


开发环境补充:
    ide: IDEA
    要求: 安装插件lombok


doc:
如何创建属于自己的生产者呢？ 例如， 参考ExampleAsyncProducer.java

public class ExampleAsyncProducer： extends TopicAsyncProducer implements AsyncProducer {
    @Override
    protected String getDefaultRoutingKey() {
        return "example.async";
    }
}

你需要做的工作：
extends class TopicAsyncProducer
implements interface AsyncProducer
override method getDefaultRoutingKey()  （ 这个是你的消费者发消息的时候默认的routingKey， 当然，你也可以在发消息的指定routingKey ）


如何创建自己属于自己的消费者呢？ 例如， 参考ExampleAsyncConsumerNodeOne.java

public class ExampleAsyncConsumerNodeOne extends TopicAsyncConsumer implements AsyncConsumer {

    @Override
    public List<String> getBindingKeys() {
        return Arrays.asList("example.*");
    }

    @Override
    public String getQueueName() {
        return "example_queue";
    }
}

你需要做的工作有：
extends class TopicAsyncConsumer
implements interface AsyncConsumer
override method getBindingKeys()  （ 通过bindingKeys 实现queue 和 exchange 绑定， queue只接受对应routingKeys的消息 ）
override method getQueueName() （ 自定义的queue name， 没用随机生成的queue name， 好处是可以对同一queue 进行worker 扩充， 减少worker可能等待的时间 ）

生产者发的消息怎么才能被对应消费者接受并处理呢？ 例如参考ExampleEventListener.java
你需要做的工作：

在你的EventListener 中：
    @Inject
    protected AsyncRegister register;

    @PostConstruct
    public void init() {
        register.register(this);
    }

然后，在你需要被监听的异步方法上，加上@Async annotation， 并根据特定的一类异步事件，定义一个特定的class Event。 例如，任务更新后，需要更新任务的缓存和mongo， 就可以写以下两个形参一样的方法：

    @Async
    public void updateCache(ModelUpdate event) {
    }

   @Async
    public void updateMongo(ModelUpdate event) {
    }
    
    生产者方法调用为：
    ModelUpdate event = new ModelUpdate();
    //  event.set...()
    asyncProducer.durabilityPublish(event);

目前的不足：
  若消费者没有找到对应event的处理方法，消费者会将该条消息requeue到消息队列。风险就是，如果谁了一条不会没有对应处理方法的消息，这条消息会永远无法消费。

参考资料：http://www.rabbitmq.com/tutorials/tutorial-five-java.html
