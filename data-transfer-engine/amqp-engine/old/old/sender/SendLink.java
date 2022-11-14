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

package insideworld.engine.amqp.old.sender;

import insideworld.engine.actions.ActionException;
import insideworld.engine.actions.chain.ChainTags;
import insideworld.engine.actions.chain.Link;
import insideworld.engine.actions.keeper.Record;
import insideworld.engine.actions.keeper.context.Context;
import insideworld.engine.actions.keeper.output.Output;
import insideworld.engine.injection.ObjectFactory;
import java.util.Iterator;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import org.apache.commons.lang3.tuple.Pair;

/**
 * Link to send output to AMQP.
 * Each record in AMQP will send in separate message.
 *
 * @param <T> Sender type.
 * @since 0.4.0
 */
@Dependent
public class SendLink<T extends AbstractActionSender> implements Link {

    /**
     * Object factory.
     */
    private final ObjectFactory factory;

    /**
     * Sender.
     */
    private AbstractActionSender sender;

    /**
     * Action key to execute.
     */
    private String key;

    /**
     * Send bulk message.
     */
    private boolean bulk = false;

    /**
     * Default constructor.
     *
     * @param factory Object factory.
     */
    @Inject
    public SendLink(final ObjectFactory factory) {
        this.factory = factory;
    }

    @Override
    public final void process(final Context context, final Output output) throws ActionException {
        if (this.bulk) {
            this.sendBulk(output);
        } else {
            this.send(output);
        }
    }

    private void sendBulk(final Output output) throws ActionException {
        final Record result = this.factory.createObject(Record.class);
        final Iterator<Record> iterator = output.getRecords().iterator();
        for (int i = 0; i < output.getRecords().size(); i++) {
            result.put(String.valueOf(i), iterator.next());
        }
        result.put(ChainTags.BULK, "");
        this.send(Pair.of(this.key, result));
    }

    private void send(final Output output) throws ActionException {
        for (final Record record : output) {
            this.send(Pair.of(this.key, record));
        }
    }

    private void send(final Pair<String, Record> data) throws ActionException {
        try {
            this.sender.send(data);
        } catch (final Exception exp) {
            throw new ActionException("Can't send message", exp);
        }
    }

    /**
     * Set action.
     *
     * @param key Key.
     * @return The same instance.
     */
    public SendLink<T> setAction(final String key) {
        this.key = key;
        return this;
    }

    /**
     * Set sender type.
     *
     * @param clazz Sender type.
     * @return The same instance.
     */
    public SendLink<T> setSender(final Class<T> clazz) {
        this.sender = this.factory.createObject(clazz);
        return this;
    }

    public SendLink<T> setSendBulk() {
        this.bulk = true;
        return this;
    }
}
