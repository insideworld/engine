/*
 * Copyright
 * This code is property of Anton Eliseev.
 * If you get this code looks like you hack my private repository and don't see this text.
 *
 */

package insideworld.engine.amqp.sender;

import insideworld.engine.actions.keeper.Record;
import insideworld.engine.amqp.destination.Channel;
import insideworld.engine.datatransfer.endpoint.PreExecute;
import io.vertx.core.json.JsonObject;
import io.vertx.mutiny.amqp.AmqpMessageBuilder;
import io.vertx.mutiny.core.buffer.Buffer;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Collection;
import org.apache.commons.lang3.tuple.Pair;

public abstract class AbstractActionSender extends AbstractSender<Pair<String, Record>> {

    private final Collection<PreExecute<AmqpMessageBuilder>> executes;

    public AbstractActionSender(final Channel channel,
                                final Collection<PreExecute<AmqpMessageBuilder>> executes) {
        super(channel);
        this.executes = executes;
    }

    @Override
    protected void build(final Pair<String, Record> pair, final AmqpMessageBuilder builder)
        throws Exception {
        final String action = pair.getLeft();
        final Record record = pair.getRight();
        for (final PreExecute<AmqpMessageBuilder> execute : this.executes) {
            execute.preExecute(record, builder);
        }
        builder.withJsonObjectAsBody(new JsonObject(record.values()));
//        final ByteArrayOutputStream out = new ByteArrayOutputStream();
//        final ObjectOutputStream os;
//        try {
//            os = new ObjectOutputStream(out);
//            os.writeObject(record);
//            builder.withBufferAsBody(Buffer.buffer(out.toByteArray()));
//        } catch (IOException e) {
//            throw new RuntimeException("Can't send a message");
//        }
        builder.subject(action);
    }
}
