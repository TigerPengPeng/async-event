package developer.github.factory.rabbitmq;

import com.rabbitmq.client.Address;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import developer.github.utils.ExceptionUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PreDestroy;

/**
 * @package: developer.github.factory.rabbitmq
 * @class:
 * @author: 黄鹏
 * @date: 2016年08月11日 上午10:58
 * @description:
 */
@Slf4j
@Data
public class RabbitConnectionFactory {

    private Connection connection;

    public RabbitConnectionFactory(String username,
                                   String password,
                                   String vhost,
                                   String address) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername(username);
        factory.setPassword(password);
        factory.setVirtualHost(vhost);
        factory.setAutomaticRecoveryEnabled(true);
        factory.setNetworkRecoveryInterval(5000);

        try {
            Address[] addresses = Address.parseAddresses(address);
            connection = factory.newConnection(addresses);
        } catch (Throwable t) {
            log.error("Can't connect to Rabbitmq server.", t);
            throw new IllegalStateException(ExceptionUtils.getThrowableString(t));
        }
    }

    public Connection get() {
        return connection;
    }

    @PreDestroy
    public void destroy() {
        try {
            connection.close();
        } catch (Throwable t) {
            log.error("Ignore exception {}", t);
        }
    }
}
