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

package insideworld.engine.entities.converter;

import insideworld.engine.actions.chain.LinkException;
import insideworld.engine.actions.keeper.context.Context;
import insideworld.engine.actions.keeper.output.Output;
import insideworld.engine.actions.keeper.test.KeeperMatchers;
import insideworld.engine.entities.StorageException;
import insideworld.engine.entities.mock.MockTags;
import insideworld.engine.entities.mock.entities.positive.MockEntity;
import insideworld.engine.entities.storages.Storage;
import insideworld.engine.entities.tags.EntityTag;
import insideworld.engine.entities.tags.StorageTags;
import insideworld.engine.injection.ObjectFactory;
import insideworld.engine.matchers.exception.ExceptionMatchers;
import io.quarkus.test.junit.QuarkusTest;
import java.util.Collection;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

/**
 * Test export entity link.
 *
 * @since 0.14.0
 */
@QuarkusTest
class ExportEntityTest {

    /**
     * Factory.
     */
    private final ObjectFactory factory;

    /**
     * Storage.
     */
    private final Storage<MockEntity> storage;

    /**
     * Default constructor.
     *
     * @param factory Factory.
     * @param storage Storage.
     */
    ExportEntityTest(
        final ObjectFactory factory,
        final Storage<MockEntity> storage
    ) {
        this.factory = factory;
        this.storage = storage;
    }

    /**
     * TC:
     * Try to execute export for all entities from storage using single and multiple tags.
     * ER:
     * If link is not init - exception.
     * If set only single tag - one record with ID of entity by tag PRIMARY.
     * if set only multiple tag - how much records as storage has.
     * if set both - entities from storage + 1.
     * @throws StorageException Shouldn't raise.
     * @throws LinkException Shouldn't raise.
     */
    @Test
    final void test() throws StorageException, LinkException {
        final Collection<MockEntity> entities = this.storage.readAll();
        final MockEntity entity = entities.iterator().next();
        final Context context = this.factory.createObject(Context.class);
        context.put(MockTags.PRIMARY, entity);
        context.put(MockTags.PRIMARIES, entities);
        final Output output = this.factory.createObject(Output.class);
        final ExportEntityLink link = this.factory.createObject(ExportEntityLink.class);
        MatcherAssert.assertThat(
            "Should be exception because link is not init.",
            () -> link.process(context, output),
            ExceptionMatchers.catchException(
                LinkException.class,
                ExceptionMatchers.messageMatcher(
                    0,
                    Matchers.containsString("Link is not init!")
                )
            )
        );
        link.setTag(MockTags.PRIMARY);
        link.process(context, output);
        MatcherAssert.assertThat(
            "Output should contain one record",
            output,
            Matchers.allOf(
                Matchers.iterableWithSize(1),
                Matchers.hasItem(KeeperMatchers.match(StorageTags.ID, Matchers.is(entity.getId())))
            )
        );
        output.getRecords().clear();
        link.setTag((EntityTag<?>) null);
        link.setTag(MockTags.PRIMARIES);
        link.process(context, output);
        MatcherAssert.assertThat(
            "Output should contain several records",
            output,
            Matchers.allOf(
                Matchers.iterableWithSize(entities.size()),
                Matchers.hasItem(KeeperMatchers.match(StorageTags.ID, Matchers.is(entity.getId())))
            )
        );
        output.getRecords().clear();
        link.setTag(MockTags.PRIMARY);
        link.process(context, output);
        MatcherAssert.assertThat(
            "Output should contain several records",
            output,
            Matchers.allOf(
                Matchers.iterableWithSize(entities.size() + 1),
                Matchers.hasItem(KeeperMatchers.match(StorageTags.ID, Matchers.is(entity.getId())))
            )
        );
    }
}
