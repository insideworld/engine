package insideworld.engine.amqp.destination;

import io.vertx.mutiny.amqp.AmqpReceiver;
import io.vertx.mutiny.amqp.AmqpSender;

public interface Channel {

    AmqpReceiver createReceiver();

    AmqpSender createSender();
}
