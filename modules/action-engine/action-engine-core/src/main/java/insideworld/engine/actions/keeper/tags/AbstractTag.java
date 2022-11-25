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

package insideworld.engine.actions.keeper.tags;

/**
 * Abstract tag based on string key.
 * For creating a new tag type just extend from this class.
 * @param <T> Data type for this tag.
 * @since 0.1.0
 */
public abstract class AbstractTag<T> implements Tag<T> {

    /**
     * Tag key.
     */
    private final String tag;

    /**
     * Default constructor.
     * @param ptag Tag key.
     */
    public AbstractTag(final String ptag) {
        this.tag = ptag;
    }

    @Override
    public final String getTag() {
        return this.tag;
    }

    @Override
    public final boolean equals(final Object ptag) {
        final boolean equal;
        if (ptag instanceof Tag) {
            equal = this.tag.equals(((Tag) ptag).getTag());
        } else {
            equal = false;
        }
        return equal;
    }

    @Override
    public final int hashCode() {
        return this.tag.hashCode();
    }
}
