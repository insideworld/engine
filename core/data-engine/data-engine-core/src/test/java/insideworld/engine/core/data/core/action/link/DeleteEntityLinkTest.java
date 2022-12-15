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

package insideworld.engine.core.data.core.action.link;

import insideworld.engine.core.action.chain.LinkException;
import insideworld.engine.core.action.keeper.context.Context;
import insideworld.engine.core.action.keeper.output.Output;
import insideworld.engine.core.action.keeper.test.KeeperMatchers;
import insideworld.engine.core.data.core.StorageException;
import insideworld.engine.core.data.core.action.links.DeleteEntityLink;
import insideworld.engine.core.data.core.mock.InitMock;
import insideworld.engine.core.data.core.mock.MockTags;
import insideworld.engine.core.data.core.mock.entities.exclude.MockExcludeEntity;
import insideworld.engine.core.data.core.mock.entities.positive.MockEntity;
import insideworld.engine.core.data.core.storages.Storage;
import insideworld.engine.core.common.exception.CommonException;
import insideworld.engine.core.common.injection.ObjectFactory;
import insideworld.engine.core.common.matchers.exception.ExceptionMatchers;
import io.quarkus.test.junit.QuarkusTest;
import java.util.Collection;
import java.util.List;
import javax.enterprise.util.TypeLiteral;
import javax.inject.Inject;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

/**
 * Test delete link.
 * @since 0.14.0
 */
@QuarkusTest
class DeleteEntityLinkTest {

    /**
     * Object factory.
     */
    private final ObjectFactory factory;

    /**
     * Storage.
     */
    private final Storage<MockEntity> storage;

    /**
     * Mock init.
     */
    private final InitMock init;

    /**
     * Constructor.
     * @param factory Object factory.
     * @param storage Storage.
     * @param init Mock init.
     */
    @Inject
    DeleteEntityLinkTest(
        final ObjectFactory factory,
        final Storage<MockEntity> storage,
        final InitMock init
    ) {
        this.factory = factory;
        this.storage = storage;
        this.init = init;
    }

    /**
     * TC:
     * Check that link throw exception if tag to write not set.
     * ER:
     * Exception.
     * @throws CommonException Shouldn't raise.
     */
    @Test
    final void testException() throws CommonException {
        final DeleteEntityLink<MockEntity> link = this.factory.createObject(
            new TypeLiteral<>() {
            }
        );
        MatcherAssert.assertThat(
            "Should be exception because link init skipped.",
            () -> link.process(null, null),
            ExceptionMatchers.catchException(
                LinkException.class,
                ExceptionMatchers.messageMatcher(
                    0,
                    Matchers.endsWith("Link was not init")
                )
            )
        );
        link.setType(MockEntity.class);
        MatcherAssert.assertThat(
            "Should be exception because link init skipped.",
            () -> link.process(null, null),
            ExceptionMatchers.catchException(
                LinkException.class,
                ExceptionMatchers.messageMatcher(
                    0,
                    Matchers.endsWith("Link was not init")
                )
            )
        );
        final DeleteEntityLink<MockEntity> both = this.factory.createObject(
            new TypeLiteral<>() {
            }
        );
        both.setTag(MockTags.PRIMARY);
        both.setTags(MockTags.PRIMARIES);
        MatcherAssert.assertThat(
            "Should be exception because storage not found for this.",
            () -> both.process(null, null),
            ExceptionMatchers.catchException(
                LinkException.class,
                ExceptionMatchers.messageMatcher(
                    0,
                    Matchers.containsString("Link was not init")
                )
            )
        );
        final DeleteEntityLink<MockExcludeEntity> wrong = this.factory.createObject(
            new TypeLiteral<>() {
            }
        );
        MatcherAssert.assertThat(
            "Should be exception because storage not found for this.",
            () -> wrong.setType(MockExcludeEntity.class),
            ExceptionMatchers.catchException(
                StorageException.class,
                ExceptionMatchers.messageMatcher(
                    0,
                    Matchers.containsString("Storage not found")
                )
            )
        );
    }

    /**
     * TC:
     * Delete single link in different conditions.
     * ER:
     * If tag not present in context - nothing to do.
     * If tag present - delete it and check count of entities in storage.
     * @throws LinkException Shouldn't throw.
     * @throws StorageException Shouldn't throw.
     */
    @Test
    final void testSingle() throws LinkException, StorageException {
        final DeleteEntityLink<MockEntity> link = this.factory.createObject(
            new TypeLiteral<>() {
            }
        );
        final MockEntity entity = this.init.createPrimary();
        final int size = this.storage.readAll().size();
        final Context context = this.factory.createObject(Context.class);
        final Output output = this.factory.createObject(Output.class);
        link.setTag(MockTags.PRIMARY);
        link.setType(MockEntity.class);
        link.process(context, output);
        MatcherAssert.assertThat(
            "Context should be empty doesn't have entity",
            context,
            Matchers.not(KeeperMatchers.contain(MockTags.PRIMARY))
        );
        MatcherAssert.assertThat(
            "Entity was deleted from storage.",
            size,
            Matchers.is(this.storage.readAll().size())
        );
        context.put(MockTags.PRIMARY, entity);
        link.process(context, output);
        MatcherAssert.assertThat(
            "Entity wasn't deleted from storage.",
            size - 1,
            Matchers.is(this.storage.readAll().size())
        );
        link.process(context, output);
        MatcherAssert.assertThat(
            "Entity another entity has deleted from storage.",
            size - 1,
            Matchers.is(this.storage.readAll().size())
        );
    }

    /**
     * TC:
     * Delete multiple link in different conditions.
     * ER:
     * If tag not present in context - nothing to do.
     * If tag present - delete it and check count of entities in storage.
     * @throws LinkException Shouldn't throw.
     * @throws StorageException Shouldn't throw.
     */
    @Test
    final void testMulti() throws LinkException, StorageException {
        final DeleteEntityLink<MockEntity> link = this.factory.createObject(
            new TypeLiteral<>() {
            }
        );
        final Collection<MockEntity> entities =
            List.of(this.init.createPrimary(), this.init.createPrimary());
        final int size = this.storage.readAll().size();
        final Context context = this.factory.createObject(Context.class);
        final Output output = this.factory.createObject(Output.class);
        link.setTags(MockTags.PRIMARIES);
        link.setType(MockEntity.class);
        link.process(context, output);
        MatcherAssert.assertThat(
            "Context should be empty doesn't have entity",
            context,
            Matchers.not(KeeperMatchers.contain(MockTags.PRIMARIES))
        );
        MatcherAssert.assertThat(
            "Entities were deleted from storage.",
            size,
            Matchers.is(this.storage.readAll().size())
        );
        context.put(MockTags.PRIMARIES, entities);
        link.process(context, output);
        MatcherAssert.assertThat(
            "Entities weren't deleted from storage.",
            size - entities.size(),
            Matchers.is(this.storage.readAll().size())
        );
        link.process(context, output);
        MatcherAssert.assertThat(
            "Entities were deleted again but shouldn't",
            size - entities.size(),
            Matchers.is(this.storage.readAll().size())
        );
    }

    /**
     * TC:
     * Delete both - single and multiple tag in link with different conditions.
     * ER:
     * If tag not present in context - nothing to do.
     * If tag present - delete it and check count of entities in storage.
     * If count present - check output.
     * @throws LinkException Shouldn't throw.
     * @throws StorageException Shouldn't throw.
     */
    @Test
    final void testBoth() throws LinkException, StorageException {
        final DeleteEntityLink<MockEntity> link = this.factory.createObject(
            new TypeLiteral<>() {
            }
        );
        final MockEntity entity = this.init.createPrimary();
        final Collection<MockEntity> entities =
            List.of(this.init.createPrimary(), this.init.createPrimary());
        final int size = this.storage.readAll().size();
        final Context context = this.factory.createObject(Context.class);
        final Output output = this.factory.createObject(Output.class);
        link.setType(MockEntity.class);
        link.setTag(MockTags.PRIMARY);
        link.setTags(MockTags.PRIMARIES);
        link.process(context, output);
        MatcherAssert.assertThat(
            "Context should be empty doesn't have entity",
            context,
            Matchers.not(KeeperMatchers.contain(MockTags.PRIMARIES))
        );
        MatcherAssert.assertThat(
            "Entities were deleted to storage.",
            size,
            Matchers.is(this.storage.readAll().size())
        );
        context.put(MockTags.PRIMARY, entity);
        context.put(MockTags.PRIMARIES, entities);
        link.process(context, output);
        MatcherAssert.assertThat(
            "Entities weren't deleted from storage.",
            size - entities.size() - 1,
            Matchers.is(this.storage.readAll().size())
        );
        link.process(context, output);
        MatcherAssert.assertThat(
            "Entities were deleted again but shouldn't",
            size - entities.size() - 1,
            Matchers.is(this.storage.readAll().size())
        );
    }

}
