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
//package insideworld.engine.core.action.chain.execute;
//
//import com.google.common.collect.Lists;
//import insideworld.engine.core.action.ActionException;
//import insideworld.engine.core.action.chain.Link;
//import insideworld.engine.core.action.chain.LinkException;
//import insideworld.engine.core.action.executor.ActionExecutor;
//import insideworld.engine.core.action.keeper.context.Context;
//import insideworld.engine.core.action.keeper.output.Output;
//import insideworld.engine.core.action.keeper.tags.Tag;
//import insideworld.engine.core.common.injection.ObjectFactory;
//import java.util.Arrays;
//import java.util.Collection;
//import javax.inject.Inject;
//import org.apache.commons.lang3.tuple.Pair;
//
///**
// * Abstract logic for execute action inside chain action.
// * Need to extend realisation to support different key types.
// *
// * @since 0.1.0
// */
//public abstract class AbstractExecuteActionLink<I, O> implements Link<I> {
//
//    /**
//     * Object factory.
//     */
//    private final ObjectFactory factory;
//
//
//
//    /**
//     * Default constructor.
//     *
//     * @param executor Action executor.
//     * @param factory Object factory.
//     */
//    @Inject
//    public AbstractExecuteActionLink(
//        final ObjectFactory factory
//    ) {
//        this.factory = factory;
//    }
//
//    @Override
//    public final boolean process(final I input) throws LinkException {
//
//        return true;
//    }
//
//    //Как вызывать класс
//    //Маппер с инпута
//    //Маппер с аутпута
//    //Контекс заполнитель
//
//}
