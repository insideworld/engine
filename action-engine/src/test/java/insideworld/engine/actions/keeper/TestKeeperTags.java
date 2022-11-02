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

package insideworld.engine.actions.keeper;

import insideworld.engine.actions.keeper.tags.SingleTag;
import java.util.UUID;

/**
 * Tags to test keeper.
 * @since 0.14.0
 */
public final class TestKeeperTags {

    /**
     * UUID tag for tests.
     */
    public static final SingleTag<UUID> UUID =
        new SingleTag<>("insideworld.engine.actions.keeper.TestKeeperTags.uuid");

    /**
     * Dummy object.
     */
    public static final SingleTag<Object> DUMMY =
        new SingleTag<>("insideworld.engine.actions.keeper.TestKeeperTags.dummy");

    /**
     * Private constructor.
     */
    private TestKeeperTags() {
        //Nothing to do.
    }
}
