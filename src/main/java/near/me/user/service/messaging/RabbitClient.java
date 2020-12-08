package near.me.user.service.messaging;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import near.me.user.shared.JsonMapper;
import org.springframework.stereotype.Service;

@Service
public class RabbitClient {

    public static final String SOCIAL_NETWORK_QUEUE = "social_network";
    private final ConnectionFactory factory;

    public RabbitClient() {
        factory = new ConnectionFactory();
        factory.setHost("localhost");
    }

    public void sendEvent(Object model, String queue) {

        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {

            System.out.println("RABBITMQ: Sending event to queue [ " + queue + " ]. " + JsonMapper.parseAsString(model));

            channel.basicPublish("", queue, null, JsonMapper.parse(model));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
