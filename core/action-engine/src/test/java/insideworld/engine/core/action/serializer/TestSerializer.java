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

package insideworld.engine.core.action.serializer;

import insideworld.engine.core.action.executor.ActionExecutor;
import insideworld.engine.core.action.executor.key.ClassKey;
import insideworld.engine.core.common.exception.CommonException;
import io.quarkus.test.junit.QuarkusTest;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

@QuarkusTest
class TestSerializer {

    private final ActionSerializer serializer;
    private final ActionExecutor executor;

    TestSerializer(
        final ActionSerializer serializer,
        final ActionExecutor executor
    ) {
        this.serializer = serializer;
        this.executor = executor;
    }

    /**
     * TC: Create a JSON with UUID and try to serialize and deserialize it.
     * @throws CommonException Shouldn't raise.
     */
    @Test
    void testSingle() throws CommonException {
        final UUID uuid = UUID.randomUUID();
        final InputStream input = new ByteArrayInputStream(
            String.format("{\"uuid\": \"%s\"}", uuid).getBytes()
        );
        final TestInput inobj = this.serializer.deserialize(
            new ClassKey<>(TestSingleAction.class),
            input
        );
        final TestOutput outobj = this.executor.execute(
            new ClassKey<>(TestSingleAction.class),
            inobj
        );
        final OutputStream output = new ByteArrayOutputStream();
        this.serializer.serialize(new ClassKey<>(TestSingleAction.class), outobj, output);
        MatcherAssert.assertThat(
            "Can't find UUID",
            output.toString(),
            Matchers.containsString(uuid.toString())
        );
    }

    /**
     * TC: Create a JSON with UUID and try to serialize and deserialize it using array.
     * @throws CommonException Shouldn't raise.
     */
    @Test
    void testArray() throws CommonException, IOException {
        final List<UUID> uuids = Stream.generate(UUID::randomUUID).limit(10).toList();
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        for (final UUID uuid : uuids) {
            builder.append(String.format(String.format("{\"uuid\": \"%s\"},", uuid)));
        }
        builder.deleteCharAt(builder.length() - 1);
        builder.append("]");
        final InputStream input = new ByteArrayInputStream(builder.toString().getBytes());
        final TestInput[] deserialize = this.serializer.deserialize(
            new ClassKey<>(TestArrayAction.class),
            input
        );
        final TestOutput[] outpuobj = this.executor.execute(
            new ClassKey<>(TestArrayAction.class),
            deserialize
        );
        final OutputStream output = new ByteArrayOutputStream();
        this.serializer.serialize(new ClassKey<>(TestArrayAction.class), outpuobj, output);
        System.out.println("qwe");
    }

    @Test
    void testCollection() throws CommonException, IOException {
        final List<UUID> uuids = Stream.generate(UUID::randomUUID).limit(10).toList();
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        for (final UUID uuid : uuids) {
            builder.append(String.format(String.format("{\"uuid\": \"%s\"},", uuid)));
        }
        builder.deleteCharAt(builder.length() - 1);
        builder.append("]");
        final InputStream input = new ByteArrayInputStream(builder.toString().getBytes());
        final Collection<TestInput> deserialize = this.serializer.deserialize(
            new ClassKey<>(TestCollectionAction.class),
            input
        );
        final Collection<TestOutput> outpuobj = this.executor.execute(
            new ClassKey<>(TestCollectionAction.class),
            deserialize
        );
        final OutputStream output = new ByteArrayOutputStream();
        this.serializer.serialize(new ClassKey<>(TestCollectionAction.class), outpuobj, output);
        System.out.println(output);
    }

}
