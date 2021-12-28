package insideworld.engine.integration.tests.entities.generated;

import insideworld.engine.entities.storages.Storage;
import insideworld.engine.entities.storages.StorageException;
import insideworld.engine.injection.ObjectFactory;
import insideworld.engine.integration.entities.generated.MyEntity;
import io.quarkus.test.junit.QuarkusTest;
import javax.enterprise.util.TypeLiteral;
import javax.inject.Inject;
import javax.transaction.Transactional;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class TestGenerated {

    private final ObjectFactory factory;

    @Inject
    public TestGenerated(ObjectFactory factory) {
        this.factory = factory;
    }

    @Test
    @Transactional
    public void test() throws StorageException {
        final Storage<MyEntity> storage = factory.createObject(new TypeLiteral<Storage<MyEntity>>() {});
        final MyEntity object = factory.createObject(MyEntity.class);
        object.setMessage("Some shiit");
        System.out.println(object.getMessage());
        storage.write(object);
        System.out.println(storage.read(1));
//        final ClassCreator build = ClassCreator.builder().superClass(AbstractEntity.class).interfaces(Entity.class)
//            .className("OneMore").classOutput(new ClassOutput() {
//                @Override
//                public void write(String s, byte[] bytes) {
//                    final String s1 = new String(bytes);
//                    System.out.println(s);
//
//                }
//            }).build();
////        build.get
//        build.close();
//        build.addAnnotation();
//        build

//        final MyEntity object = factory.createObject(MyEntity.class);

//        assert object != null;

    }


}
