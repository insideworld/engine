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

package insideworld.engine.amqp.old.io;

import insideworld.engine.amqp.connection.Connection;
import insideworld.engine.startup.OnStartUp;
import io.vertx.mutiny.amqp.AmqpReceiver;
import io.vertx.mutiny.amqp.AmqpSender;

/**
 * Create a channel to interact with another application using AMQP protocol.
 *
 * @param <T> Type of data to transfer.
 * @since 0.4.0
 */
public abstract class AbstractChannel<T> implements Channel<T>, OnStartUp {

    /**
     * Connection for channel.
     */
    private final Connection connection;
    private final String send;
    private final String receive;

    private AmqpReceiver receiver;

    private AmqpSender sender;

    public AbstractChannel(
        final Connection connection,
        final String send,
        final String receive
    ) {
        this.connection = connection;
        this.send = send;
        this.receive = receive;
    }

    @Override
    public final void startUp() {
        if (this.send != null) {
            this.sender = this.connection.connection().createSenderAndAwait(this.send);
        }
        if (this.receive != null) {
            this.receiver = this.connection.connection().createReceiverAndAwait(this.receive);
            this.receiver.handler()
        }
    }

    @Override
    public final int order() {
        return 50_000;
    }



}
