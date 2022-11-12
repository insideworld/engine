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

package insideworld.engine.entities.actions.link;

import insideworld.engine.actions.chain.LinkException;
import insideworld.engine.actions.keeper.context.Context;
import insideworld.engine.actions.keeper.output.Output;
import insideworld.engine.actions.keeper.test.KeeperMatchers;
import insideworld.engine.entities.StorageException;
import insideworld.engine.entities.actions.links.WriteEntityLink;
import insideworld.engine.entities.mock.MockTags;
import insideworld.engine.entities.mock.entities.positive.MockEntity;
import insideworld.engine.entities.mock.entities.positive.MockEntityImpl;
import insideworld.engine.entities.storages.Storage;
import insideworld.engine.injection.ObjectFactory;
import insideworld.engine.matchers.exception.ExceptionMatchers;
import io.quarkus.test.junit.QuarkusTest;
import java.util.Collection;
import java.util.List;
import javax.enterprise.util.TypeLiteral;
import javax.inject.Inject;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

/**
 * Write entity link test.
 * @since 0.14.0
 */
@QuarkusTest
class WriteEntityLinkTest {

    /**
     * Object factory.
     */
    private final ObjectFactory factory;

    /**
     * Storage.
     */
    private final Storage<MockEntity> storage;

    /**
     * Constructor.
     * @param factory Object factory.
     * @param storage Storage.
     */
    @Inject
    WriteEntityLinkTest(
        final ObjectFactory factory,
        final Storage<MockEntity> storage
    ) {
        this.factory = factory;
        this.storage = storage;
    }

    /**
     * TC:
     * Check that link throw exception if tag to write not set.
     * ER:
     * Exception.
     */
    @Test
    final void testException() {
        final WriteEntityLink<MockEntity> link = this.factory.createObject(
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
    }

    /**
     * TC:
     * Write single link in different conditions.
     * ER:
     * If tag not present in context - nothing to do.
     * If tag present - write it and check count of entities in storage.
     * If count present - check output.
     * @throws LinkException Shouldn't throw.
     * @throws StorageException Shouldn't throw.
     */
    @Test
    final void testSingle() throws LinkException, StorageException {
        final WriteEntityLink<MockEntity> link = this.factory.createObject(
            new TypeLiteral<>() {
            }
        );
        final int size = this.storage.readAll().size();
        final Context context = this.factory.createObject(Context.class);
        final Output output = this.factory.createObject(Output.class);
        link.setTag(MockTags.PRIMARY);
        link.process(context, output);
        MatcherAssert.assertThat(
            "Context should be empty doesn't have entity",
            context,
            Matchers.not(KeeperMatchers.contain(MockTags.PRIMARY))
        );
        MatcherAssert.assertThat(
            "Entity was wrote to storage.",
            size,
            Matchers.is(this.storage.readAll().size())
        );
        context.put(MockTags.PRIMARY, new MockEntityImpl());
        link.process(context, output);
        MatcherAssert.assertThat(
            "Entity wasn't wrote",
            context,
            KeeperMatchers.match(
                MockTags.PRIMARY,
                Matchers.hasProperty(
                    "id",
                    Matchers.not(0L)
                )
            )
        );
        MatcherAssert.assertThat(
            "Entity wasn't wrote to storage.",
            size + 1,
            Matchers.is(this.storage.readAll().size())
        );
        link.setCount();
        link.process(context, output);
        MatcherAssert.assertThat(
            "Count in output is not present",
            output,
            Matchers.allOf(
                Matchers.iterableWithSize(1),
                Matchers.hasItem(
                    Matchers.allOf(
                        KeeperMatchers.match("type", Matchers.is(MockTags.PRIMARY.getTag())),
                        KeeperMatchers.match("count", Matchers.is(1))
                    )
                )
            )
        );
        MatcherAssert.assertThat(
            "Entity was write again but shouldn't",
            size + 1,
            Matchers.is(this.storage.readAll().size())
        );
    }

    /**
     * TC:
     * Write multiple link in different conditions.
     * ER:
     * If tag not present in context - nothing to do.
     * If tag present - write it and check count of entities in storage.
     * If count present - check output.
     * @throws LinkException Shouldn't throw.
     * @throws StorageException Shouldn't throw.
     */
    @Test
    final void testMulti() throws LinkException, StorageException {
        final WriteEntityLink<MockEntity> link = this.factory.createObject(
            new TypeLiteral<>() {
            }
        );
        final int size = this.storage.readAll().size();
        final Context context = this.factory.createObject(Context.class);
        final Output output = this.factory.createObject(Output.class);
        link.setTag(MockTags.PRIMARIES);
        link.process(context, output);
        MatcherAssert.assertThat(
            "Context should be empty doesn't have entity",
            context,
            Matchers.not(KeeperMatchers.contain(MockTags.PRIMARIES))
        );
        MatcherAssert.assertThat(
            "Entities were wrote to storage.",
            size,
            Matchers.is(this.storage.readAll().size())
        );
        final Collection<MockEntity> entities =
            List.of(new MockEntityImpl(), new MockEntityImpl());
        context.put(MockTags.PRIMARIES, entities);
        link.process(context, output);
        MatcherAssert.assertThat(
            "Entities wasn't wrote",
            context,
            KeeperMatchers.match(
                MockTags.PRIMARIES,
                Matchers.allOf(
                    Matchers.iterableWithSize(2),
                    Matchers.hasItem(
                        Matchers.hasProperty(
                            "id",
                            Matchers.not(0L)
                        )
                    )
                )
            )
        );
        MatcherAssert.assertThat(
            "Entities weren't wrote to storage.",
            size + entities.size(),
            Matchers.is(this.storage.readAll().size())
        );
        link.setCount();
        link.process(context, output);
        MatcherAssert.assertThat(
            "Count in output is not present",
            output,
            Matchers.allOf(
                Matchers.iterableWithSize(1),
                Matchers.hasItem(
                    Matchers.allOf(
                        KeeperMatchers.match("type", Matchers.is(MockTags.PRIMARIES.getTag())),
                        KeeperMatchers.match("count", Matchers.is(2))
                    )
                )
            )
        );
        MatcherAssert.assertThat(
            "Entities ware wrote again but shouldn't",
            size + entities.size(),
            Matchers.is(this.storage.readAll().size())
        );
    }

    /**
     * TC:
     * Write both - single and multiple tag in link with different conditions.
     * ER:
     * If tag not present in context - nothing to do.
     * If tag present - write it and check count of entities in storage.
     * If count present - check output.
     * @throws LinkException Shouldn't throw.
     * @throws StorageException Shouldn't throw.
     */
    @Test
    final void testBoth() throws LinkException, StorageException {
        final WriteEntityLink<MockEntity> link = this.factory.createObject(
            new TypeLiteral<>() {
            }
        );
        final int size = this.storage.readAll().size();
        final Context context = this.factory.createObject(Context.class);
        final Output output = this.factory.createObject(Output.class);
        link.setTag(MockTags.PRIMARY);
        link.setTag(MockTags.PRIMARIES);
        link.process(context, output);
        MatcherAssert.assertThat(
            "Context should be empty doesn't have entity",
            context,
            Matchers.not(KeeperMatchers.contain(MockTags.PRIMARIES))
        );
        MatcherAssert.assertThat(
            "Entities were wrote to storage.",
            size,
            Matchers.is(this.storage.readAll().size())
        );
        final Collection<MockEntity> entities =
            List.of(new MockEntityImpl(), new MockEntityImpl());
        context.put(MockTags.PRIMARY, new MockEntityImpl());
        context.put(MockTags.PRIMARIES, entities);
        link.process(context, output);
        MatcherAssert.assertThat(
            "Entities wasn't wrote",
            context,
            Matchers.allOf(
                KeeperMatchers.match(
                    MockTags.PRIMARIES,
                    Matchers.allOf(
                        Matchers.iterableWithSize(2),
                        Matchers.hasItem(
                            Matchers.hasProperty(
                                "id",
                                Matchers.not(0L)
                            )
                        )
                    )
                ),
                KeeperMatchers.match(
                    MockTags.PRIMARY,
                    Matchers.hasProperty(
                        "id",
                        Matchers.not(0L)
                    )
                )
            )
        );
        MatcherAssert.assertThat(
            "Entities weren't wrote to storage.",
            size + entities.size() + 1,
            Matchers.is(this.storage.readAll().size())
        );
        link.setCount();
        link.process(context, output);
        MatcherAssert.assertThat(
            "Count in output is not present",
            output,
            Matchers.allOf(
                Matchers.iterableWithSize(2),
                Matchers.hasItems(
                    Matchers.allOf(
                        KeeperMatchers.match("type", Matchers.is(MockTags.PRIMARIES.getTag())),
                        KeeperMatchers.match("count", Matchers.is(2))
                    ),
                    Matchers.allOf(
                        KeeperMatchers.match("type", Matchers.is(MockTags.PRIMARY.getTag())),
                        KeeperMatchers.match("count", Matchers.is(1))
                    )
                )
            )
        );
        MatcherAssert.assertThat(
            "Entities ware wrote again but shouldn't",
            size + entities.size() + 1,
            Matchers.is(this.storage.readAll().size())
        );
    }

}
