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

package insideworld.engine.frameworks.quarkus.common.thread;

import com.google.common.collect.Lists;
import insideworld.engine.core.common.exception.CommonException;
import insideworld.engine.core.common.injection.ObjectFactory;
import insideworld.engine.frameworks.quarkus.common.threads.QuarkusMultiTaskBuilder;
import io.quarkus.test.junit.QuarkusTest;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import javax.enterprise.util.TypeLiteral;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class ThreadTest {

    private final ObjectFactory factory;

    private final AtomicInteger integer = new AtomicInteger();

    public ThreadTest(final ObjectFactory factory) {
        this.factory = factory;
    }

//    @Test
//    final void testTaskException() throws CommonException {
//        final QuarkusTaskBuilder<UUID, UUID> builder =
//            this.factory.createObject(new TypeLiteral<>() { });
//        builder.add(() -> { throw new ThreadTestException("Some exception"); });
//        builder.combine(uuid -> uuid.stream().findAny().get(), UUID.class);
//        final UUID result = builder.build().result();
////        MatcherAssert.assertThat(
////            "Should be exception",
////            () -> builder.build().result(),
////            ExceptionMatchers.catchException(TaskException.class)
////        );
//        System.out.println();
//    }

    @Test
    final void testTaskBuilder() throws CommonException {
        final QuarkusMultiTaskBuilder<UUID, List<UUID>> builder =
            this.factory.createObject(new TypeLiteral<>() {
            });
        for (int i = 0; i < 1000; i++) {
            builder.add(this::getRandomUUID);
        }
        builder.combine(input -> {
            System.out.println(Thread.currentThread() + " " + "combine");
            List<UUID> strings = Lists.newArrayListWithCapacity(5);
            strings.addAll(input);
            return strings;
        }, UUID.class);
        final List<UUID> build = builder.build().result();
        System.out.println(build);
    }

    @Test
    final void testException() {
        final QuarkusMultiTaskBuilder<?, ?> builder = this.factory.createObject(new TypeLiteral<>() {
        });
        builder
            .add(() -> {
                throw new RuntimeException("Some");
            })
//            .exception(throwable -> null)
//            .combine(objects -> null, null)
            .build().subscribe(o -> System.out.println("dddddd"));

    }

    private UUID getRandomUUID() {
        System.out.println(Thread.currentThread() + " " + "random");
        return UUID.randomUUID();
    }
}
