package insideworld.engine.integration.entities.convertor;

import java.util.Collection;
import javax.enterprise.context.Dependent;

@Dependent
public class TestByteCode {

    private Collection<TestOne> test;

    public Collection<TestOne> getTest() {
        return test;
    }

    public void setTest(Collection<TestOne> test) {
        this.test = test;
    }
}
