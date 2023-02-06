///*
// * Copyright (c) 2022 Anton Eliseev
// *
// * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
// * associated documentation files (the "Software"), to deal in the Software without restriction,
// * including without limitation the rights to use, copy, modify, merge, publish, distribute,
// * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software
// * is furnished to do so, subject to the following conditions:
// *
// * The above copyright notice and this permission notice shall be included in all copies or
// * substantial portions of the Software.
// *
// * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
// * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
// * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
// * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
// */
//
//package insideworld.engine.core.endpoint.amqp.actions;
//
//import com.google.common.collect.ImmutableMap;
//import insideworld.engine.core.action.executor.key.Key;
//import insideworld.engine.core.common.exception.CommonException;
//import insideworld.engine.core.common.serializer.SerializerFacade;
//import insideworld.engine.core.common.startup.OnStartUp;
//import insideworld.engine.core.endpoint.amqp.connection.AmqpSender;
//import insideworld.engine.core.endpoint.amqp.connection.Connection;
//import insideworld.engine.core.endpoint.base.action.ActionSender;
//import insideworld.engine.core.common.serializer.Serializer;
//import insideworld.engine.core.common.serializer.SerializerException;
//import io.netty.buffer.ByteBufOutputStream;
//import io.netty.buffer.Unpooled;
//import io.vertx.core.json.JsonObject;
//import io.vertx.mutiny.amqp.AmqpMessageBuilder;
//import io.vertx.mutiny.core.buffer.Buffer;
//import java.io.IOException;
//import java.util.Comparator;
//import java.util.List;
//
//
//public abstract class AbstractAmqpActionSender implements ActionSender, OnStartUp {
//
//    private final Connection connection;
//    private final String channel;
//    private final SerializerFacade serializer;
//
//    /**
//     * AMQP sender.
//     */
//    private AmqpSender sender;
//
//    /**
//     * Default constructor.
//     *
//     */
//    public AbstractAmqpActionSender(
//        final Connection connection,
//        final String channel,
//        final SerializerFacade serializer
//    ) {
//        this.connection = connection;
//        this.channel = channel;
//        this.serializer = serializer;
//    }
//
//    @Override
//    public <I, O> void send(final Key<I, O> action, final Key<O, ?> callback, final I input)
//        throws CommonException {
//        this.sender.send(builder -> {
//            builder.subject(action.getKey());
//            final ImmutableMap.Builder<String, Object> map = ImmutableMap.builder();
//            this.additional(builder);
//            if (callback != null) {
//                map.put(AmqpTags.AMQP_CALLBACK.getTag(), callback.getKey());
//            }
//            builder.applicationProperties(new JsonObject(map.build()));
//            final ByteBufOutputStream output = new ByteBufOutputStream(Unpooled.buffer());
//            final Class<I> type = (Class<I>) input.getClass();
//            this.serializer.getSerializer(input.getClass()).serialize(input, output);
//            for (final Serializer serializer : serializers) {
//                if (serializer.applicable(input.getClass())) {
//                    serializer.serialize(input, output);
//                    break;
//                }
//            }
//            builder.withBufferAsBody(Buffer.buffer(output.buffer()));
//            try {
//                output.close();
//            } catch (final IOException exp) {
//                throw new SerializerException(exp);
//            }
//        });
//    }
//
//    @Override
//    public void startUp() throws CommonException {
//        this.sender = this.connection.createSender(this.channel);
//    }
//
//    @Override
//    public long startOrder() {
//        return 100_000;
//    }
//
//    protected abstract void additional(AmqpMessageBuilder builder);
//
//}
