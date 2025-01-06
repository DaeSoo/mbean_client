package org.daesoo.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Publisher {

    String exchangeName = "amq.direct";
    String routingKey = "routingkey";

    public void directProducer() throws IOException, TimeoutException {
        ConnectionFactory factory = setConnectionFactory();

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        String message = "Hello World";
        byte[] body = message.getBytes();

        // Publish
        channel.basicPublish(exchangeName, routingKey, null, body);

        channel.close();
        connection.close();
    }

    public static void sendToRabbitMQ(String message) {
        ConnectionFactory factory = setConnectionFactory();

        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {

            // 큐 선언 (idempotent)
            channel.queueDeclare(RabbitMQProperties.MBEAN_METRICS.getValue(), true, false, false, null);

            // 메시지 전송
            channel.basicPublish("", RabbitMQProperties.MBEAN_METRICS.getValue(), null, message.getBytes("UTF-8"));
            System.out.println(" [x] Sent: " + message);

        } catch (IOException | RuntimeException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            throw new RuntimeException(e);
        }
    }

    public static ConnectionFactory setConnectionFactory() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(RabbitMQProperties.HOST.getValue());
        factory.setPort(Integer.parseInt(RabbitMQProperties.PORT.getValue()));
        factory.setUsername(RabbitMQProperties.USERNAME.getValue());
        factory.setPassword(RabbitMQProperties.PASSWORD.getValue());
        return factory;
    }
}
