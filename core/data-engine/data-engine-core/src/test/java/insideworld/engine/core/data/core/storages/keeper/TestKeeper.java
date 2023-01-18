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

package insideworld.engine.core.data.core.storages.keeper;

import insideworld.engine.core.common.injection.ObjectFactory;
import insideworld.engine.core.common.matchers.exception.ExceptionMatchers;
import insideworld.engine.core.data.core.StorageException;
import insideworld.engine.core.data.core.mock.entities.positive.MockEntity;
import insideworld.engine.core.data.core.mock.entities.positive.MockEntityImpl;
import io.quarkus.test.junit.QuarkusTest;
import java.util.Collections;
import javax.inject.Inject;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

/**
 * Test keeper.
 *
 * @since 0.14.0
 */
@QuarkusTest
class TestKeeper {

    /**
     * Storages.
     */
    private final StorageKeeper storages;

    /**
     * Object factory.
     */
    private final ObjectFactory factory;

    /**
     * Default constructor.
     *
     * @param storages Storage.
     * @param factory Object factory.
     */
    @Inject
    TestKeeper(final StorageKeeper storages, final ObjectFactory factory) {
        this.storages = storages;
        this.factory = factory;
    }

    /**
     * TC: Try to get storage by interface and class entity.
     * ER: Objects should equal by address.
     *
     * @throws StorageException Test.
     */
    @Test
    final void testStorageGet() throws StorageException {
        MatcherAssert.assertThat(
            "Storage from keeper by implementation different that by interface",
            this.storages.getStorage(MockEntity.class),
            Matchers.is(this.storages.getStorage(MockEntityImpl.class))
        );
    }

    /**
     * TC: Add storage which entity doesn't have implementation to a new keeper.
     * ER: Should raise an exception.
     */
    @Test
    final void testNegativeInit() {
        final HashStorageKeeper keeper = new HashStorageKeeper(
            Collections.singletonList(new DummyStorage()),
            this.factory
        );
        MatcherAssert.assertThat(
            "Expected exception at init",
            keeper::startUp,
            ExceptionMatchers.catchException(
                StorageException.class,
                ExceptionMatchers.messageMatcher(
                    0,
                    Matchers.containsString("Can't init HashStorage keeper")
                )
            )
        );
    }

    /**
     * TC: Try to get unimplemented storage for entity.
     */
    @Test
    final void testNegativeGet() {
        MatcherAssert.assertThat(
            "Expected exception at init",
            () -> this.storages.getStorage(DummyEntity.class),
            ExceptionMatchers.catchException(
                StorageException.class,
                ExceptionMatchers.messageMatcher(
                    0,
                    Matchers.containsString("Storage not found for entity")
                )
            )
        );
    }
}
