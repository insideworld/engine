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

package insideworld.engine.actions.chain;

import insideworld.engine.actions.keeper.tags.MultipleTag;
import insideworld.engine.actions.keeper.tags.SingleTag;
import java.util.UUID;

/**
 * Tags for test execute.
 * @since 0.14.0
 */
public final class TestChainTags {

    /**
     * UUID for compare.
     */
    public static final SingleTag<UUID> UUID =
        new SingleTag<>("engine.tests.unit.actions.chain.execute.uuid");

    /**
     * One more UUID.
     */
    public static final SingleTag<UUID> UUID_ADDITIONAL =
        new SingleTag<>("engine.tests.unit.actions.chain.execute.uuid.additional");

    /**
     * Some string to test propagation child to parent output.
     */
    public static final SingleTag<String> OUTPUT_ADDITIONAL =
        new SingleTag<>("engine.tests.unit.actions.chain.execute.output.additional");

    /**
     * Just tag to validate propagation.
     */
    public static final SingleTag<Object> ONE =
        new SingleTag<>("engine.tests.unit.actions.chain.execute.one");

    /**
     * Just tag to validate propagation.
     */
    public static final SingleTag<Object> TWO =
        new SingleTag<>("engine.tests.unit.actions.chain.execute.two");

    /**
     * String tag.
     */
    public static final SingleTag<Object> STRING =
        new SingleTag<>("engine.tests.unit.actions.chain.execute.string");

    /**
     * Integer tag.
     */
    public static final SingleTag<Object> INTEGER =
        new SingleTag<>("engine.tests.unit.actions.chain.execute.integer");

    /**
     * List of integers.
     */
    public static final MultipleTag<Integer> LIST =
        new MultipleTag<>("engine.tests.unit.actions.chain.execute.list");

    /**
     * Which exception should call.
     */
    public static final SingleTag<Integer> EXCEPTION =
        new SingleTag<>("insideworld.engine.actions.chain.TestChainTags.EXCEPTION");

    /**
     * Private constructor.
     */
    private TestChainTags() {
        //Nothing to do.
    }

}