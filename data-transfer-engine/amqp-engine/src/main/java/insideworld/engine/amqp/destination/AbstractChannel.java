package insideworld.engine.amqp.destination;

import insideworld.engine.amqp.connection.Connection;
import io.vertx.mutiny.amqp.AmqpReceiver;
import io.vertx.mutiny.amqp.AmqpSender;

public abstract class AbstractChannel implements Channel {

    private final Connection connection;

    public AbstractChannel(final Connection connection) {
        this.connection = connection;
    }

    @Override
    public AmqpReceiver createReceiver() {
        return this.connection.connection().createReceiverAndAwait(this.from());
    }

    @Override
    public AmqpSender createSender() {
        return this.connection.connection().createSenderAndAwait(this.to());
    }

    protected abstract String from();

    protected abstract String to();

}
