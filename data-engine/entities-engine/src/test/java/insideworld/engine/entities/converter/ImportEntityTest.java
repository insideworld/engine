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

import insideworld.engine.actions.chain.LinkException;
import insideworld.engine.actions.keeper.context.Context;
import insideworld.engine.injection.ObjectFactory;
import io.quarkus.test.junit.QuarkusTest;
import javax.inject.Inject;
import org.hamcrest.Matcher;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class ImportEntityTest {

    private final ObjectFactory factory;

    @Inject
    ImportEntityTest(final ObjectFactory factory) {
        this.factory = factory;
    }

    @Test
    final void testExceptions() {
        final ImportEntityLink link = this.factory.createObject(ImportEntityLink.class);
        final var both = Assertions.assertThrows(
            LinkException.class,
            () -> link.process(null, null),
            "Should be an exception with empty tags"
        );
        MatcherAssert.assertThat(
            "Message not about init link",
            both.getMessage(),
            Matchers.endsWith("Link is not init: tag null type null")
        );
        final var tag = Assertions.assertThrows(
            LinkException.class,
            () -> link.process(null, null),
            "Should be an exception"
        );

    }

    @Test
    final void testImport() {
        final Context context = this.factory.createObject(Context.class);
//        this.link
    }
}
