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

package insideworld.engine.amqp.actions.tags;

import insideworld.engine.actions.keeper.tags.SingleTag;
import java.util.Map;

/**
 * Tags for AMQP engine.
 * @since 0.14.0
 */
public final class AmqpTags {

    public static final SingleTag<Map<String, Object>> AMQP_PROPERTIES = new SingleTag<>(
        "insideworld.engine.amqp.actions.tags.AmqpTags.AMQP_PROPERTIES"
    );

    public static final SingleTag<String> CALLBACK_ACTION = new SingleTag<>(
        "insideworld.engine.amqp.actions.tags.AmqpTags.CALLBACK_ACTION"
    );

    public static final SingleTag<Object> BULK = new SingleTag<>(
        "insideworld.engine.amqp.actions.tags.AmqpTags.BULK"
    );

    /**
     * Private constructor.
     */
    private AmqpTags() {
        //Nothing to do.
    }

}
