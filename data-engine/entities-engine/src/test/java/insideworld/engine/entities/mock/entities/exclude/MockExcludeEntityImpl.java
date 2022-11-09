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

package insideworld.engine.entities.mock.entities.exclude;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Collection;
import java.util.List;
import javax.enterprise.context.Dependent;

/**
 * Class to test absent methods for field.
 * @since 0.14.0
 */
@Dependent
public class MockExcludeEntityImpl implements MockExcludeEntity {

    /**
     * Id.
     */
    private long id;

    /**
     * Field without getter.
     */
    private String noget = "testnoget";

    /**
     * Field without setter.
     */
    @SuppressWarnings("PMD.ImmutableField")
    private String noset = "testnoset";

    /**
     * Field without methods.
     */
    @SuppressWarnings("PMD.ImmutableField")
    private String nomethods = "testnomethods";

    /**
     * Test string for ignore case.
     */
    private String ignore = "ignore";

    /**
     * Collection of objects.
     */
    private Collection<Object> objects = List.of(new Object(), new Object());

    @Override
    public final long getId() {
        return this.id;
    }

    @Override
    public final void setNoget(final String pnoget) {
        this.noget = pnoget;
    }

    @Override
    public final String getNoset() {
        return this.noset;
    }

    @Override
    public final boolean check(final String nogetvalue) {
        return "testnoset".equals(this.noset)
            && "testnomethods".equals(this.nomethods)
            && "ignore".equals(this.ignore)
            && nogetvalue.equals(this.noget);
    }

    @JsonIgnore
    @Override
    public final String getIgnore() {
        return this.ignore;
    }

    @JsonIgnore
    @Override
    public final void setIgnore(final String pignore) {
        this.ignore = pignore;
    }

    @Override
    public final Collection<Object> getObjects() {
        return this.objects;
    }

    @Override
    public final void setObjects(final Collection<Object> pobjects) {
        this.objects = pobjects;
    }
}
