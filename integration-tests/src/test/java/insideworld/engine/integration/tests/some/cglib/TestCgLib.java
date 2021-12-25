package insideworld.engine.integration.tests.some.cglib;

import insideworld.engine.database.AbstractEntity;
import insideworld.engine.entities.Entity;
import insideworld.engine.entities.storages.Storage;
import insideworld.engine.entities.storages.keeper.StorageKeeper;
import insideworld.engine.injection.ObjectFactory;
import insideworld.engine.quarkus.extension.MyEntity;
import insideworld.engine.security.common.entities.User;
import io.quarkus.gizmo.ClassCreator;
import io.quarkus.gizmo.ClassOutput;
import io.quarkus.test.junit.QuarkusTest;
import javax.enterprise.util.TypeLiteral;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class TestCgLib {

    private final ObjectFactory factory;

    @Inject
    public TestCgLib(ObjectFactory factory) {
        this.factory = factory;
    }

    @Test
    @Transactional
    public void test() {
        final Storage<MyEntity> object1 = factory.createObject(new TypeLiteral<Storage<MyEntity>>() {});
        final MyEntity object = factory.createObject(MyEntity.class);
        object.setMessage("Some shiit");
        System.out.println(object.getMessage());
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
