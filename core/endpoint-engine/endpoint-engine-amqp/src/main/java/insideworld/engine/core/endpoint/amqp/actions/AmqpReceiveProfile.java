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
//import insideworld.engine.core.action.executor.profiles.AbstractExecuteProfile;
//import insideworld.engine.core.action.executor.profiles.DefaultExecuteProfile;
//import insideworld.engine.core.action.executor.profiles.ExecuteProfile;
//import insideworld.engine.core.action.executor.profiles.wrapper.ExecuteWrapper;
//import insideworld.engine.core.endpoint.base.action.EndpointProfile;
//import java.util.Collection;
//import java.util.List;
//import javax.inject.Inject;
//import javax.inject.Singleton;
//
///**
// * Profile which will use for AMQP received messages.
// * @since 0.14.0
// */
//@Singleton
//public class AmqpReceiveProfile extends AbstractExecuteProfile implements EndpointProfile {
//
//    /**
//     * Default constructor.
//     *
//     * @param executors Collection of all executors in the system.
//     */
//    @Inject
//    public AmqpReceiveProfile(final List<ExecuteWrapper> executors) {
//        super(executors);
//    }
//
//    @Override
//    protected final Collection<Class<? extends ExecuteProfile>> profiles() {
//        return List.of(
//            DefaultExecuteProfile.class,
//            this.getClass()
//        );
//    }
//}
