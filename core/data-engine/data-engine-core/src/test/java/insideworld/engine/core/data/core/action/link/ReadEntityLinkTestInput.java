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
import insideworld.engine.core.action.keeper.test.KeeperMatchers;
import insideworld.engine.core.data.core.StorageException;
import insideworld.engine.core.data.core.action.links.ReadEntityLink;
import insideworld.engine.core.data.core.mock.InitMock;
import insideworld.engine.core.data.core.mock.MockTags;
import insideworld.engine.core.data.core.mock.entities.positive.MockEntity;
import insideworld.engine.core.data.core.storages.Storage;
import insideworld.engine.core.data.core.tags.StorageTags;
import insideworld.engine.core.common.injection.ObjectFactory;
import insideworld.engine.core.common.matchers.exception.ExceptionMatchers;
import insideworld.engine.core.common.matchers.exception.ExecutableException;
import io.quarkus.test.junit.QuarkusTest;
import java.util.Collection;
import java.util.List;
import javax.enterprise.util.TypeLiteral;
import javax.inject.Inject;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

/**
 * Read entity link test.
 *
 * @since 0.14.0
 */
@QuarkusTest
class ReadEntityLinkTestInput {

    /**
     * Object factory.
     */
    private final ObjectFactory factory;

    /**
     * Init mock.
     */
    private final InitMock init;

    /**
     * Storage.
     */
    private final Storage<MockEntity> storage;

    /**
     * Constructor.
     *
     * @param factory Object factory.
     * @param init Init mock.
     * @param storage Storage.
     */
    @Inject
    ReadEntityLinkTestInput(
        final ObjectFactory factory,
        final InitMock init,
        final Storage<MockEntity> storage
    ) {
        this.factory = factory;
        this.init = init;
        this.storage = storage;
    }

    /**
     * TC: Check that link throw exception if not init.
     * ER: Should raise an exception.
     */
    @Test
    final void testException() {
        final ReadEntityLink<MockEntity> link = this.factory.createObject(
            new TypeLiteral<>() {
            }
        );
        MatcherAssert.assertThat(
            "Should be exception because link init skipped.",
            () -> link.process(null, null),
            ExceptionMatchers.catchException(
                LinkException.class,
                ExceptionMatchers.messageMatcher(0, Matchers.endsWith("Link was not init"))
            )
        );
        final ExecutableException[] executables = {
            () -> link.setTag(null, null),
            () -> link.setTag(StorageTags.ID, null),
            () -> link.setTag(null, MockTags.PRIMARY),
            () -> link.setTags(null, null),
            () -> link.setTags(StorageTags.IDS, null),
            () -> link.setTags(null, MockTags.PRIMARIES),
        };
        for (final ExecutableException executable : executables) {
            MatcherAssert.assertThat(
                "Should be exception because invalid arguments",
                executable,
                ExceptionMatchers.catchException(
                    LinkException.class,
                    ExceptionMatchers.messageMatcher(
                        0, Matchers.endsWith("One or both arguments is null")
                    )
                )
            );
        }
    }

    /**
     * TC: Test that entity will read with set ID and doesn't read without.
     *
     * @throws StorageException Shouldn't raise.
     * @throws LinkException Shouldn't raise.
     */
    @Test
    final void testSingle() throws StorageException, LinkException {
        final ReadEntityLink<MockEntity> link = this.factory.createObject(
            new TypeLiteral<>() {
            }
        );
        final MockEntity entity = this.init.createPrimary();
        link.setType(MockEntity.class);
        link.setTag(StorageTags.ID, MockTags.PRIMARY);
        final Context context = this.factory.createObject(Context.class);
        link.process(context, null);
        MatcherAssert.assertThat(
            "Shouldn't read because ID is not set",
            context,
            Matchers.not(KeeperMatchers.contain(MockTags.PRIMARY))
        );
        context.put(StorageTags.ID, entity.getId());
        link.process(context, null);
        MatcherAssert.assertThat(
            "Should read because ID is set",
            context,
            KeeperMatchers.match(
                MockTags.PRIMARY,
                Matchers.hasProperty("id", Matchers.is(entity.getId()))
            )
        );
    }

    /**
     * TC: Test that will read all entities if IDS is not set and read only specific if set.
     *
     * @throws StorageException Shouldn't raise.
     * @throws LinkException Shouldn't raise.
     */
    @Test
    final void testMulti() throws StorageException, LinkException {
        final ReadEntityLink<MockEntity> link = this.factory.createObject(
            new TypeLiteral<>() {
            }
        );
        final Collection<MockEntity> entities =
            List.of(this.init.createPrimary(), this.init.createPrimary());
        link.setType(MockEntity.class);
        link.setTags(StorageTags.IDS, MockTags.PRIMARIES);
        final Context context = this.factory.createObject(Context.class);
        link.process(context, null);
        MatcherAssert.assertThat(
            "Should read all because ID's is not set",
            context,
            KeeperMatchers.match(
                MockTags.PRIMARIES,
                Matchers.iterableWithSize(this.storage.readAll().size())
            )
        );
        context.put(StorageTags.IDS, entities.stream().map(MockEntity::getId).toList());
        link.process(context, null);
        MatcherAssert.assertThat(
            "Should read because IDs is set",
            context,
            KeeperMatchers.match(
                MockTags.PRIMARIES,
                Matchers.iterableWithSize(entities.size())
            )
        );
    }

    /**
     * TC: Test both cases with single and multiple together.
     *
     * @throws StorageException Shouldn't raise.
     * @throws LinkException Shouldn't raise.
     */
    @Test
    final void testBoth() throws StorageException, LinkException {
        final ReadEntityLink<MockEntity> link = this.factory.createObject(
            new TypeLiteral<>() {
            }
        );
        final MockEntity entity = this.init.createPrimary();
        final Collection<MockEntity> entities =
            List.of(this.init.createPrimary(), this.init.createPrimary());
        link.setType(MockEntity.class);
        link.setTag(StorageTags.ID, MockTags.PRIMARY);
        link.setTags(StorageTags.IDS, MockTags.PRIMARIES);
        final Context context = this.factory.createObject(Context.class);
        context.put(StorageTags.ID, entity.getId());
        context.put(StorageTags.IDS, entities.stream().map(MockEntity::getId).toList());
        link.process(context, null);
        MatcherAssert.assertThat(
            "Should read because ID is set",
            context,
            KeeperMatchers.match(
                MockTags.PRIMARY,
                Matchers.hasProperty("id", Matchers.is(entity.getId()))
            )
        );
        MatcherAssert.assertThat(
            "Should read because IDs is set",
            context,
            KeeperMatchers.match(
                MockTags.PRIMARIES,
                Matchers.iterableWithSize(entities.size())
            )
        );
    }

}
