/*
 * Copyright (c) 2022 Anton Eliseev
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package insideworld.engine.integration.tests.entities.convertor;

import com.google.common.collect.ImmutableList;
import insideworld.engine.actions.ActionException;
import insideworld.engine.actions.keeper.Record;
import insideworld.engine.actions.keeper.context.Context;
import insideworld.engine.entities.converter.EntityConverter;
import insideworld.engine.entities.storages.Storage;
import insideworld.engine.entities.storages.StorageException;
import insideworld.engine.injection.ObjectFactory;
import insideworld.engine.integration.entities.convertor.TestByteCode;
import insideworld.engine.integration.entities.convertor.TestMain;
import insideworld.engine.integration.entities.convertor.TestOne;
import io.quarkus.test.junit.QuarkusTest;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@QuarkusTest
public class TestConvertor {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestConvertor.class);

    private final EntityConverter converter;
    private final ObjectFactory factory;
    private final Storage<TestMain> storage;

    @Inject
    public TestConvertor(final EntityConverter converter,
                         final ObjectFactory factory,
                         final Storage<TestMain> storage) {
        this.converter = converter;
        this.factory = factory;
        this.storage = storage;
    }

    @Test
    @Transactional
    public void test() throws ActionException, StorageException {
        final TestMain entity = this.storage.read(1);
        TestOne one = entity.getOne();
        final Record convert = this.converter.convert(entity);
        LOGGER.trace("Test");
        final Context context = factory.createObject(Context.class);
        context.put("id", 1L);
        context.put("name", "entity");
        context.put("description", "desctiption of entity");
        context.put("arraysIds", ImmutableList.of(1L, 3L, 4L, 10L));
        final TestMain qwe = this.converter.convert(context, TestMain.class);
        LOGGER.trace(convert.toString());
    }
}
