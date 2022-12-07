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

import insideworld.engine.actions.keeper.tags.SingleTag;
import insideworld.engine.entities.mock.entities.positive.MockEntity;
import insideworld.engine.entities.tags.EntityTag;

/**
 * Tags for mock entity.
 * @since 0.14.0
 */
public final class ConverterTestTags {

    /**
     * Mock entity tag.
     */
    public static final EntityTag<MockEntity> MOCK_ENTITY =
        new EntityTag<>("insideworld.engine.entities.converter.ConverterTestTags.MOCK_ENTITY");

    /**
     * Output value for test.
     */
    public static final SingleTag<Object> OUTPUT_VALUE =
        new SingleTag<>("insideworld.engine.entities.extractor.ExtractorTags.OUTPUT_VALUE");

    /**
     * Private constructor.
     */
    private ConverterTestTags() {
        //Nothing to do.
    }
}