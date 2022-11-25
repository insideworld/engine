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

package insideworld.engine.amqp.actions;

import com.google.common.collect.Maps;
import insideworld.engine.actions.keeper.output.Output;
import insideworld.engine.amqp.actions.tags.AmqpTags;
import insideworld.engine.amqp.connection.AmqpSender;
import insideworld.engine.amqp.connection.Connection;
import insideworld.engine.datatransfer.endpoint.actions.ActionSender;
import insideworld.engine.injection.ObjectFactory;
import insideworld.engine.startup.OnStartUp;
import insideworld.engine.startup.StartUpException;
import java.util.Collections;
import java.util.Map;

public class AmqpActionSender implements ActionSender, OnStartUp {

    private final ObjectFactory factory;
    private final Connection connection;
    private final String channel;
    private AmqpSender sender;

    public AmqpActionSender(
        final ObjectFactory factory,
        final Connection connection,
        final String channel
    ) {
        this.factory = factory;
        this.connection = connection;
        this.channel = channel;
    }

    public void send(final String action, final String callback, final Output output) {
        final Map<String, Object> properties;
        if (callback == null) {
            properties = Collections.emptyMap();
        } else {
            properties = Map.of(AmqpTags.CALLBACK_ACTION.getTag(), callback);
        }
        this.sender.send(
            this.factory.createObject(
                OutputMessage.class, output, action, properties
            )
        );
    }

    @Override
    public void startUp() throws StartUpException {
        this.sender = this.connection.createSender(this.channel);
    }

    @Override
    public int order() {
        return 70_000;
    }
}
