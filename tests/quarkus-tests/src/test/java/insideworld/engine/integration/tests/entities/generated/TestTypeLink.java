package insideworld.engine.integration.tests.entities.generated;

import com.google.common.collect.ImmutableList;
import insideworld.engine.actions.ActionException;
import insideworld.engine.actions.keeper.context.Context;
import insideworld.engine.actions.keeper.output.Output;
import insideworld.engine.injection.ObjectFactory;
import insideworld.engine.integration.entities.TestLink;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class TestTypeLink {

    private ObjectFactory factory;

    public TestTypeLink(final ObjectFactory factory) {
        this.factory = factory;
    }

    @Test
    public void test() throws ActionException {
        final TestLink link = this.factory.createObject(TestLink.class);
        final Context context = this.factory.createObject(Context.class);
        final Output output = this.factory.createObject(Output.class);
        context.put("some", "Some test");
        context.put("collection", ImmutableList.of("One", "Two"));
        link.process(context, output);
        System.out.println(context.get("writeTest").toString());
        System.out.println(context.get("secondCollection").toString());
    }


}
