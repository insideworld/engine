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

package insideworld.engine.core.endpoint.amqp.actions;

import insideworld.engine.core.action.executor.ExecutorTags;
import insideworld.engine.core.action.executor.key.StringKey;
import insideworld.engine.core.common.startup.OnStartUp;
import insideworld.engine.core.endpoint.amqp.connection.Connection;
import insideworld.engine.core.endpoint.base.action.ActionReceiver;
import insideworld.engine.core.endpoint.base.action.ActionSender;
import insideworld.engine.core.common.serializer.SerializerException;
import io.netty.buffer.ByteBufInputStream;
import io.vertx.mutiny.amqp.AmqpMessage;
import java.io.IOException;
import org.apache.commons.lang3.ObjectUtils;

public class AmqpActionReceiver implements OnStartUp {

    private final Connection connection;
    private final String channel;
    private final ActionSender callback;
    private final ActionReceiver receiver;

    /**

     */
    public AmqpActionReceiver(
        final Connection connection,
        final String channel,
        final ActionSender callback,
        final ActionReceiver receiver
    ) {
        this.connection = connection;
        this.channel = channel;
        this.callback = callback;
        this.receiver = receiver;
    }

    @Override
    public void startUp() {
        this.connection.createReceiver(this.channel).receive(this::execute);
    }

    private void execute(final AmqpMessage message) {
        final ByteBufInputStream input =
            new ByteBufInputStream(message.bodyAsBinary().getByteBuf());
        this.receiver.executeTask(
            new StringKey<>(message.subject()),
            input,
            context -> {
                context.put(ExecutorTags.PROFILE, AmqpProfile.class);
                context.put(AmqpTags.AMQP_PROPERTIES, message.applicationProperties().getMap());
            }
        ).subscribe(result -> {
            try {
                input.close();
            } catch (final IOException exp) {
                throw new SerializerException(exp);
            }
            final String callback = message.applicationProperties().getString(
                AmqpTags.AMQP_CALLBACK.getTag()
            );
            if (ObjectUtils.allNotNull(callback, this.receiver)) {
                this.callback.send(
                    new StringKey<>(callback),
                    null,
                    result
                );
            }
        });
    }

    @Override
    public long startOrder() {
        return 900_000;
    }
}
