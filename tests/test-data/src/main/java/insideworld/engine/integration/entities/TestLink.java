/*
 * Copyright (c) 2022 Anton Eliseev
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package insideworld.engine.integration.entities;

import com.google.common.collect.ImmutableList;
import insideworld.engine.actions.keeper.output.Output;
import insideworld.engine.generator.links.AbstractTypeLink;
import insideworld.engine.generator.links.LinkInput;
import insideworld.engine.injection.ObjectFactory;
import java.util.Collection;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class TestLink extends AbstractTypeLink<TestLink.TestLinkInput> {

    @Inject
    public TestLink(final ObjectFactory factory) {
        super(factory);
    }

    @Override
    public void process(final TestLinkInput input, final Output output) {
        System.out.println(input.getSome());
        input.setWriteTest("Modified some");
        System.out.println(input.getCollection());
        input.setSecondCollection(ImmutableList.of("Test1", "Test2"));

    }

    @Override
    protected Class<TestLinkInput> inputType() {
        return TestLinkInput.class;
    }

    public interface TestLinkInput extends LinkInput {

        String getSome();

        void setSome(String some);

        String getWriteTest();

        void setWriteTest(String some);

        Collection<String> getCollection();

        void setSecondCollection(Collection<String> collection);

    }
}
