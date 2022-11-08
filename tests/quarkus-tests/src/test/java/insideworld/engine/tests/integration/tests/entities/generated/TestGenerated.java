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

package insideworld.engine.tests.integration.tests.entities.generated;

import insideworld.engine.entities.StorageException;
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
