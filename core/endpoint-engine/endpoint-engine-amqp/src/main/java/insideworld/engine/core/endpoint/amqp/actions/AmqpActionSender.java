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

import com.google.common.collect.ImmutableMap;
import insideworld.engine.core.action.executor.key.Key;
import insideworld.engine.core.common.exception.CommonException;
import insideworld.engine.core.endpoint.base.serializer.SerializerFacade;
import insideworld.engine.core.common.startup.OnStartUp;
import insideworld.engine.core.endpoint.amqp.connection.AmqpSender;
import insideworld.engine.core.endpoint.base.serializer.SerializerException;
import insideworld.engine.core.endpoint.amqp.connection.Connection;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.buffer.Unpooled;
import io.vertx.core.json.JsonObject;
import io.vertx.mutiny.core.buffer.Buffer;
import java.io.IOException;

/**
 * AMQP sender.
 * How to create it:
 */
public class AmqpActionSender implements OnStartUp {

    private final Connection connection;

    private final String channel;
    private final SerializerFacade serializer;

    private AmqpSender sender;

    /**
     * Default constructor.
     */
    public AmqpActionSender(
        final Connection connection,
        final String channel,
        final SerializerFacade serializer
    ) {
        this.connection = connection;
        this.channel = channel;
        this.serializer = serializer;
    }

    public final <I, O> void execute(
        final Key<I, O> remote,
        final Key<O, ?> callback,
        final I input
    )
        throws CommonException {
        this.sender.send(builder -> {
            builder.subject(remote.getKey());
            final ImmutableMap.Builder<String, Object> properties = ImmutableMap.builder();
            if (callback != null) {
                properties.put(AmqpTags.AMQP_CALLBACK.getTag(), callback.getKey());
            }
            builder.applicationProperties(new JsonObject(properties.build()));
            final ByteBufOutputStream output = new ByteBufOutputStream(Unpooled.buffer());
            this.serializer.serialize(input, output);
            builder.withBufferAsBody(Buffer.buffer(output.buffer()));
            try {
                output.close();
            } catch (final IOException exp) {
                throw new SerializerException(exp);
            }
        });
    }


    public void startUp() throws CommonException {
        this.sender = this.connection.createSender(this.channel);
    }

    public long startOrder() {
        return 100_000;
    }

}
