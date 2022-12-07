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

package insideworld.engine.actions.executors;

import insideworld.engine.actions.keeper.tags.MultipleTag;
import insideworld.engine.actions.keeper.tags.SingleTag;
import java.util.UUID;

/**
 * Test executor tags.
 * @since 0.14.0
 */
public final class TestExecutorTags {

    /**
     * UUID for compare.
     */
    public static final SingleTag<UUID> UUID =
        new SingleTag<>("engine.tests.unit.actions.executors.uuid");

    /**
     * UUID for compare.
     */
    public static final SingleTag<UUID> COPY_UUID =
        new SingleTag<>("engine.tests.unit.actions.executors.uuid.copy");

    /**
     * Just dummy tag.
     */
    public static final SingleTag<Object> DUMMY =
        new SingleTag<>("engine.tests.unit.actions.executors.uuid.dummy");

    /**
     * Using to test sequence of wrappers execution.
     */
    public static final MultipleTag<Integer> SEQUENCE =
        new MultipleTag<>("insideworld.engine.actions.executors.TestExecutorTags.SEQUENCE");

    /**
     * Which exception should call.
     */
    public static final SingleTag<Integer> EXCEPTION =
        new SingleTag<>("insideworld.engine.actions.executors.TestExecutorTags.EXCEPTION");

    /**
     * Private constructor.
     */
    private TestExecutorTags() {
        //Nothing to do.
    }
}