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
//package insideworld.engine.core.data.core.converter;
//
//import insideworld.engine.core.action.chain.LinkException;
//import insideworld.engine.core.action.keeper.context.Context;
//import insideworld.engine.core.action.keeper.test.KeeperMatchers;
//import insideworld.engine.core.data.core.mock.MockTags;
//import insideworld.engine.core.data.core.mock.entities.positive.MockEntity;
//import insideworld.engine.core.common.exception.CommonException;
//import insideworld.engine.core.common.injection.ObjectFactory;
//import insideworld.engine.core.common.matchers.exception.ExceptionMatchers;
//import io.quarkus.test.junit.QuarkusTest;
//import javax.inject.Inject;
//import org.hamcrest.MatcherAssert;
//import org.hamcrest.Matchers;
//import org.junit.jupiter.api.Test;
//
///**
// * Test for import entity link.
// * @since 0.14.0
// */
//@QuarkusTest
//class ImportEntityTest {
//
//    /**
//     * Object factory.
//     */
//    private final ObjectFactory factory;
//
//    /**
//     * Default constructor.
//     * @param factory Object factory.
//     */
//    @Inject
//    ImportEntityTest(final ObjectFactory factory) {
//        this.factory = factory;
//    }
//
//    /**
//     * TC: Execute link with different parameters.
//     * ER:
//     * If link is not init should raise exception.
//     * Link should pass validation that entity tag not contain in record.
//     * Link should create a new value with entity tag in record.
//     */
//    @Test
//    final void testExceptions() {
//        final ImportEntityLink link = this.factory.createObject(ImportEntityLink.class);
//        MatcherAssert.assertThat(
//            "Wrong exception for both empty arguments.",
//            () -> link.process(null, null),
//            ExceptionMatchers.catchException(
//                LinkException.class,
//                ExceptionMatchers.messageMatcher(
//                    0,
//                    Matchers.containsString("Link is not init:")
//                )
//            )
//        );
//        link.setTag(null, MockEntity.class);
//        MatcherAssert.assertThat(
//            "Wrong exception for empty tag.",
//            () -> link.process(null, null),
//            ExceptionMatchers.catchException(
//                LinkException.class,
//                ExceptionMatchers.messageMatcher(
//                    0,
//                    Matchers.containsString("Link is not init:")
//                )
//            )
//        );
//        link.setTag(MockTags.ONE, null);
//        MatcherAssert.assertThat(
//            "Wrong exception for empty type.",
//            () -> link.process(null, null),
//            ExceptionMatchers.catchException(
//                LinkException.class,
//                ExceptionMatchers.messageMatcher(
//                    0,
//                    Matchers.containsString("Link is not init:")
//                )
//            )
//        );
//    }
//
//    /**
//     * TC.
//     * @throws CommonException Shouldn't raise.
//     */
//    @Test
//    final void testImport() throws CommonException {
//        final Context context = this.factory.createObject(Context.class);
//        final ImportEntityLink link = this.factory.createObject(ImportEntityLink.class);
//        link.setTag(MockTags.PRIMARY, MockEntity.class);
//        MatcherAssert.assertThat(
//            "Link can't process but should",
//            link.can(context),
//            Matchers.is(true)
//        );
//        link.process(context, null);
//        MatcherAssert.assertThat(
//            "Context doesn't have entity",
//            context,
//            KeeperMatchers.contain(MockTags.PRIMARY)
//        );
//        MatcherAssert.assertThat(
//            "Context doesn't have entity",
//            context.get(MockTags.PRIMARY).getId(),
//            Matchers.is(0L)
//        );
//        MatcherAssert.assertThat(
//            "Link can process but shouldn't",
//            link.can(context),
//            Matchers.is(false)
//        );
//    }
//}
