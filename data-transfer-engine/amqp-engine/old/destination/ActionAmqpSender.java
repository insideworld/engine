/*
 * Copyright (c) 2022 Anton Eliseev
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software
 * is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package insideworld.engine.amqp.destination;

import insideworld.engine.actions.Action;
import insideworld.engine.actions.executor.ActionExecutor;
import insideworld.engine.actions.keeper.context.Context;
import insideworld.engine.actions.keeper.output.Output;
import insideworld.engine.amqp.connection.Connection;
import insideworld.engine.datatransfer.endpoint.PreExecute;
import insideworld.engine.datatransfer.endpoint.send.Sender;
import insideworld.engine.startup.OnStartUp;
import insideworld.engine.startup.StartUpException;
import io.vertx.mutiny.amqp.AmqpMessage;
import io.vertx.mutiny.amqp.AmqpSender;
import java.util.Map;
import org.apache.commons.lang3.tuple.Pair;

/**
 * Class to send data to antoher action like system.
 * @since 0.14.0
 */
public class ActionAmqpSender implements Sender<Pair<String, Output>>, OnStartUp {

    private final Connection connection;
    private final String receive;
    private final String send;
    private final ActionExecutor<String> executor;

    private AmqpSender sender;

    /**
     *
     * @param connection Connection to AMQP server.
     * @param receive Receive channel.
     * @param send Send channel.
     */
    public ActionAmqpSender(
        final Connection connection,
        final String receive,
        final String send,
        final ActionExecutor<String> executor
    ) {
        this.connection = connection;
        this.receive = receive;
        this.send = send;
        this.executor = executor;
    }

    @Override
    public void send(final Pair<String, Output> output, Class<? extends Action> response) {

    }

    @Override
    public void startUp() throws StartUpException {
        this.sender = this.connection.connection().createSenderAndAwait(this.send);
        this.connection.connection().createReceiverAndAwait(this.receive)
            .handler(this::receive);

    }

    private void send(final AmqpMessage message) {
        this.sender.writeAndForget(message);
    }

    private void receive(final AmqpMessage message) {
        final Map<String, Object> map = this.reader
            .readValue(message.bodyAsJsonObject().toString());
        final Context context = this.executor.createContext();
        this.parseObject(map).values().forEach(context::put);
        for (final PreExecute<AmqpMessage> execute : this.executes) {
            execute.preExecute(context, message);
        }
        return this.executor.execute(message.subject(), context);
    }

    @Override
    public int order() {
        return 50_000;
    }

}
