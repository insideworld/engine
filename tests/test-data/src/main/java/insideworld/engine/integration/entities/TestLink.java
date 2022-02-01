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
