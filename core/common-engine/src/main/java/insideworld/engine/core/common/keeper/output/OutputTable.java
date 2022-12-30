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

package insideworld.engine.core.common.keeper.output;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import insideworld.engine.core.common.keeper.Record;
import insideworld.engine.core.common.injection.ObjectFactory;
import insideworld.engine.core.common.keeper.ListTable;
import java.io.Serializable;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

/**
 * Implementation of Output based in ListTable.
 * Using iterable serializer because Jackson by default parse it as a bean because records fields
 * is property (has getter).
 *
 * @since 0.0.1
 */
@Dependent
@JsonSerialize(as = Iterable.class)
public final class OutputTable extends ListTable implements Output, Serializable {

    /**
     * Default constructor.
     *
     * @param factory Object factory.
     */
    @Inject
    public OutputTable(final ObjectFactory factory) {
        super(factory);
    }

    @Override
    public Class<? extends Record> recordType() {
        return OutputRecord.class;
    }

}

