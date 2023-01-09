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
//package insideworld.engine.core.endpoint.base.action;
//
//import insideworld.engine.core.action.executor.old.ActionExecutor;
//import insideworld.engine.core.action.executor.profiles.ExecuteProfile;
//import insideworld.engine.core.action.keeper.context.Context;
//import insideworld.engine.core.action.keeper.output.Output;
//import insideworld.engine.core.common.threads.Task;
//import insideworld.engine.core.common.threads.TaskPredicate;
//import java.util.Collection;
//import java.util.List;
//
///**
// * Provide some implementation to execute an action with specific profiler and contexts consumers.
// * @since 0.14.0
// */
//public abstract class AbstractActionReceiver<T> implements ActionReceiver<T> {
//
//    /**
//     * Output task builder.
//     */
//    private final OutputTaskBuilder builder;
//
//    /**
//     * Action executor.
//     */
//    private final ActionExecutor<String> executor;
//
//    /**
//     * Execute profile for given endpoint.
//     */
//    private final Class<? extends ExecuteProfile> profile;
//
//    /**
//     * Default constructor.
//     * @param builder Output task builder.
//     * @param executor Action executor.
//     * @param profile Execute profile for given endpoint.
//     */
//    public AbstractActionReceiver(
//        final OutputTaskBuilder builder,
//        final ActionExecutor<String> executor,
//        final Class<? extends ExecuteProfile> profile
//    ) {
//        this.builder = builder;
//        this.executor = executor;
//        this.profile = profile;
//    }
//
//    public final Task<Output> execute(final String action, final T message) {
//        final List<TaskPredicate<Output>> predicates = this.contextBuilders(message).stream().map(
//            context -> (TaskPredicate<Output>)
//                () -> this.executor.execute(
//                    action,
//                    context.build(),
//                    this.profile
//                )
//        ).toList();
//        return this.builder.createTask(predicates);
//    }
//
//    /**
//     * Just create context propagation from executor.
//     * @return New context.
//     */
//    protected final Context createContext() {
//        return this.executor.createContext();
//    }
//
//    /**
//     * Create context builders from received message.
//     * @param message Input parameter.
//     * @return Collection of builders contexts.
//     */
//    protected abstract Collection<ContextBuilder> contextBuilders(T message);
//
//}
