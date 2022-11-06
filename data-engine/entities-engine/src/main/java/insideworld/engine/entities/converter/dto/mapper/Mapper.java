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

package insideworld.engine.entities.converter.dto.mapper;

import insideworld.engine.actions.ActionException;
import insideworld.engine.actions.keeper.Record;
import insideworld.engine.entities.Entity;
import insideworld.engine.entities.converter.dto.descriptors.Descriptor;

/**
 * Mapper for data.
 * Using descriptor to identify field and row in record to propagate.
 * @since 0.6.0
 */
public interface Mapper {

    /**
     * Map record value to entity field.
     * @param record Record.
     * @param entity Entity.
     * @param descriptor What needs to map.
     * @throws ActionException Something went wrong.
     */
    void toEntity(Record record, Entity entity, Descriptor descriptor)
        throws ActionException;

    /**
     * Map entity field to record value.
     * @param record Record.
     * @param entity Entity.
     * @param descriptor What needs to map.
     * @throws ActionException Something went wrong.
     */
    void toRecord(Record record, Entity entity, Descriptor descriptor)
        throws ActionException;

    /**
     * Can apply provided descriptor to this mapper.
     * @param descriptor Descriptor.
     * @return If can - true, if not - false.
     */
    boolean canApply(Descriptor descriptor);

}
