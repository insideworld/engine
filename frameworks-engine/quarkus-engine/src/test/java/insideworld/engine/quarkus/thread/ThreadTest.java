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

package insideworld.engine.quarkus.thread;

import com.google.common.collect.Lists;
import insideworld.engine.injection.ObjectFactory;
import insideworld.engine.quarkus.threads.QuarkusTaskBuilder;
import insideworld.engine.threads.ThreadPool;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.infrastructure.Infrastructure;
import io.vertx.mutiny.core.Vertx;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import javax.enterprise.util.TypeLiteral;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class ThreadTest {

    private final ObjectFactory factory;

    public ThreadTest(final ObjectFactory factory) {

        this.factory = factory;
    }

    private volatile AtomicInteger integer = new AtomicInteger();

    @Test
    final void test() throws ExecutionException, InterruptedException {

//        Collection<Future<String>> results = Lists.newArrayListWithCapacity(100);
//        for (int i = 0; i < 100; i++) {
//            final Future<String> execute = this.pool.execute(() -> {
//                Thread.sleep(1000);
//                System.out.println(Thread.currentThread().getName() + " " + this.integer.incrementAndGet());
//                return "Hello";
//            });
//            results.add(execute);
//        }
//        System.out.println(Thread.currentThread().getName());
//        for (Future<String> result : results) {
//            System.out.println(result.get());
//        }
    }

    @Test
    final void testTaskBuilder() {
        final QuarkusTaskBuilder<UUID, List<UUID>> builder =
            this.factory.createObject(new TypeLiteral<>() { } );
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
    final void testUni() {
        Uni.createFrom().item(this::getRandomUUID).runSubscriptionOn(Infrastructure.getDefaultWorkerPool());
        final ExecutorService service = Executors.newFixedThreadPool(10);
        Uni.combine().all().unis(
            Uni.createFrom().item( this::getRandomUUID).runSubscriptionOn(Infrastructure.getDefaultWorkerPool()),
            Uni.createFrom().item( this::getRandomUUID).runSubscriptionOn(Infrastructure.getDefaultWorkerPool()),
            Uni.createFrom().item( this::getRandomUUID).runSubscriptionOn(Infrastructure.getDefaultWorkerPool()),
            Uni.createFrom().item( this::getRandomUUID).runSubscriptionOn(Infrastructure.getDefaultWorkerPool()),
            Uni.createFrom().item( this::getRandomUUID).runSubscriptionOn(Infrastructure.getDefaultWorkerPool()),
            Uni.createFrom().item( this::getRandomUUID).runSubscriptionOn(Infrastructure.getDefaultWorkerPool()),
            Uni.createFrom().item( this::getRandomUUID).runSubscriptionOn(Infrastructure.getDefaultWorkerPool()),
            Uni.createFrom().item( this::getRandomUUID).runSubscriptionOn(Infrastructure.getDefaultWorkerPool()),
            Uni.createFrom().item( this::getRandomUUID).runSubscriptionOn(Infrastructure.getDefaultWorkerPool()),
            Uni.createFrom().item( this::getRandomUUID).runSubscriptionOn(Infrastructure.getDefaultWorkerPool()),
            Uni.createFrom().item( this::getRandomUUID).runSubscriptionOn(Infrastructure.getDefaultWorkerPool())

        ).combinedWith(qwe -> {
            System.out.println(Thread.currentThread() + " " + "combine");
            List<UUID> strings = Lists.newArrayListWithCapacity(5);
            for (Object o : qwe) {
                strings.add((UUID) o);
            }
            return strings;
        }).subscribe().with(list ->
        {
            System.out.println(Thread.currentThread() + " " + "subscribe");
            for (UUID s : list) {
                System.out.println(s);
            }
        });
        System.out.println(Thread.currentThread() + " " + "Current");
        System.out.println("qwe");
    }

    private UUID getRandomUUID() {
        System.out.println(Thread.currentThread() + " " + "random");
        return UUID.randomUUID();
    }
}
