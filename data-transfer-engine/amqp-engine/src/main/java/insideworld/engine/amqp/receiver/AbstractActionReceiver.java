/*
 * Copyright
 * This code is property of Anton Eliseev.
 * If you get this code looks like you hack my private repository and don't see this text.
 *
 */

package insideworld.engine.amqp.receiver;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import insideworld.engine.actions.facade.impl.KeyActionExecutor;
import insideworld.engine.actions.keeper.MapRecord;
import insideworld.engine.actions.keeper.Record;
import insideworld.engine.actions.keeper.context.Context;
import insideworld.engine.actions.keeper.output.Output;
import insideworld.engine.amqp.destination.Channel;
import insideworld.engine.datatransfer.endpoint.PreExecute;
import insideworld.engine.threads.ThreadPool;
import io.vertx.core.json.JsonObject;
import io.vertx.mutiny.amqp.AmqpMessage;
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.Collection;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract action receiver. Execute an action from message.
 * Message should have action key in subject and contains record.
 * @see Record
 *
 * Because receive operation using I/O vertex thread - need to inject
 * an additional thread pull where which execute the primary logic with neccesary
 * concurency.
 *
 * @since 0.4.0
 */
public abstract class AbstractActionReceiver extends AbstractReceiver {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractActionReceiver.class);

    /**
     * Thread pool where execute actions.
     */
    private final ThreadPool pool;

    /**
     * Action key executor.
     */
    private final KeyActionExecutor executor;

    private final Collection<PreExecute<AmqpMessage>> executes;
    private final ObjectReader reader;

    /**
     * Default constructor.
     * @param pool Thread pool which using for execute actions.
     * @param executor Key action executor.

     */
    protected AbstractActionReceiver(final ThreadPool pool,
                                     final KeyActionExecutor executor,
                                     final Collection<PreExecute<AmqpMessage>> executes,
                                     final Channel channel) {
        super(channel);
        this.pool = pool;
        this.executor = executor;
        this.executes = executes;
        this.reader = new ObjectMapper()
            .readerFor(Map.class)
            .with(DeserializationFeature.USE_LONG_FOR_INTS);
    }

    @Override
    protected final void process(final AmqpMessage message) {
        this.pool.execute(() -> {
            try {
               return this.execute(message);
            } catch (final Exception exp) {
                LOGGER.error("Can't process message", exp);
                throw exp;
            }
        });
    }

    private Output execute(final AmqpMessage message) throws Exception {
        final Map<String, Object> map = this.reader
            .readValue(message.bodyAsJsonObject().toString());
        final Context context = this.executor.createContext();
        this.parseObject(map).values().forEach(context::put);
        for (final PreExecute<AmqpMessage> execute : this.executes) {
            execute.preExecute(context, message);
        }
        return this.executor.execute(message.subject(), context);
    }

    //TODO - create a handler mechanism
    private Record parseObject(final Map<String, Object> map) {
        final Record record = new MapRecord();
        for (var entry : map.entrySet()) {
            final Object value = entry.getValue();
            if (value instanceof Map) {
                record.put(entry.getKey(), this.parseObject((Map) entry.getValue()));
            } else {
                record.put(entry.getKey(), value);
            }
        }
        return record;
    }

}
