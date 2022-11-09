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

package insideworld.engine.entities.mock;

import insideworld.engine.actions.keeper.tags.MultipleTag;
import insideworld.engine.actions.keeper.tags.SingleTag;
import insideworld.engine.entities.mock.entities.positive.MockOneEntity;
import insideworld.engine.entities.mock.entities.positive.MockTwoEntity;
import insideworld.engine.entities.tags.EntitiesTag;
import insideworld.engine.entities.tags.EntityTag;
import java.util.Date;

/**
 * Tags for mock entity.
 * @since 0.14.0
 */
public final class MockTags {

    /**
     * String.
     */
    public static final SingleTag<String> STRPRIM = new SingleTag<>("strprim");

    /**
     * Wrapped primitive.
     */
    public static final SingleTag<Long> WRAPPRIM = new SingleTag<>("wrapprim");

    /**
     * Primitive.
     */
    public static final SingleTag<Long> PRIM = new SingleTag<>("prim");

    /**
     * String primitives.
     */
    public static final MultipleTag<String> STRPRIMS = new MultipleTag<>("strprims");

    /**
     * Wrapped primitives.
     */
    public static final MultipleTag<Long> WRAPPRIMS = new MultipleTag<>("wrapprims");

    /**
     * Date.
     */
    public static final SingleTag<Date> DATE = new SingleTag<>("date");

    /**
     * Dates.
     */
    public static final MultipleTag<Date> DATES = new MultipleTag<>("dates");

    /**
     * One.
     */
    public static final SingleTag<Long> ONE_ID = new SingleTag<>("oneId");

    /**
     * Test on null.
     */
    public static final SingleTag<String> TEST_NULL = new SingleTag<>("testnull");

    /**
     * One.
     */
    public static final EntityTag<MockOneEntity> ONE = new EntityTag<>("one");

    /**
     * Twos ids.
     */
    public static final MultipleTag<Long> TWOS_IDS = new MultipleTag<>("twosIds");

    /**
     * Twos.
     */
    public static final EntitiesTag<MockTwoEntity> TWOS = new EntitiesTag<>("twos");

    /**
     * No method field.
     */
    public static final SingleTag<String> NO_METHODS = new SingleTag<>("nomethods");

    /**
     * No set method field.
     */
    public static final SingleTag<String> NO_SET = new SingleTag<>("noset");

    /**
     * No get method field.
     */
    public static final SingleTag<String> NO_GET = new SingleTag<>("noget");

    /**
     * Ignore tag.
     */
    public static final SingleTag<String> IGNORE = new SingleTag<>("ignore");

    /**
     * Ignore tag.
     */
    public static final MultipleTag<Object> OBJECTS = new MultipleTag<>("objects");

    /**
     * Private constructor.
     */
    private MockTags() {
        //Nothing to do.
    }

}
