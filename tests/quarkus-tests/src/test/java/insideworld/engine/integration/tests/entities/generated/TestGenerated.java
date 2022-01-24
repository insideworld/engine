package insideworld.engine.integration.tests.entities.generated;

import insideworld.engine.entities.storages.StorageException;
import insideworld.engine.injection.ObjectFactory;
import io.quarkus.test.junit.QuarkusTest;
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
//        Instance<Storage<? extends Entity>> select = CDI.current().select(new TypeLiteral<Storage<? extends Entity>>() {
//        });
//        Instance<Storage<GeneratedChildEntity>> select1 = CDI.current().select(new TypeLiteral<Storage<GeneratedChildEntity>>() {
//        });
//        final Storage<GeneratedTopEntity> storage = factory.createObject(new TypeLiteral<Storage<GeneratedTopEntity>>() {});
//        final Storage<GeneratedChildEntity> storage1 = factory.createObject(new TypeLiteral<Storage<GeneratedChildEntity>>() {});
//        System.out.println("qwe");
//        factory.createObject(SomeClass.class);
//        final Storage<TestMain> storage1 = factory.createObject(new TypeLiteral<Storage<TestMain>>() {
//        });
//        final TestMain read = storage1.read(1);
//
//
//        final GeneratedTopEntity object = factory.createObject(GeneratedTopEntity.class);
//        object.setMessage("Some shiit");
//        System.out.println(object.getMessage());
//        storage.write(object);
//        System.out.println(storage.read(1));
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

    public static void qwe(String qwe) {

    }

    public void qwe() {

    }

}
