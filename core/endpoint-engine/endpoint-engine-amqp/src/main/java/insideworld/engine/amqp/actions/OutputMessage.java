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

import insideworld.engine.actions.keeper.Record;
import insideworld.engine.actions.keeper.output.Output;
import insideworld.engine.amqp.connection.Message;
import java.util.Collection;
import java.util.Map;

public class OutputMessage implements Message {

    private final Output output;
    private final String action;
    private final Map<String, Object> properties;

    public OutputMessage(
        final Output output,
        final String action,
        final Map<String, Object> properties
    ) {
        this.output = output;
        this.action = action;
        this.properties = properties;
    }

    @Override
    public Collection<Map<String, Object>> getArray() {
        return this.output.getRecords().stream().map(Record::values).toList();
    }

    @Override
    public String getSubject() {
        return this.action;
    }

    @Override
    public Map<String, Object> getProperties() {
        return this.properties;
    }
}
