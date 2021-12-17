package insideworld.engine.amqp.quarkus.beans;

import insideworld.engine.datatransfer.endpoint.PreExecute;
import insideworld.engine.quarkus.AbstractBeans;
import io.vertx.mutiny.amqp.AmqpMessage;
import io.vertx.mutiny.amqp.AmqpMessageBuilder;
import java.util.Collection;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;

public class AmqpBeans extends AbstractBeans {

    @Produces
    public Collection<PreExecute<AmqpMessage>> amqpMessagePre(final Instance<PreExecute<AmqpMessage>> instance) {
        return this.fromInstance(instance);
    }

    @Produces
    public Collection<PreExecute<AmqpMessageBuilder>> amqpMessageBuilderPre(final Instance<PreExecute<AmqpMessageBuilder>> instance) {
        return this.fromInstance(instance);
    }

}
