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
//package insideworld.engine.security.amqp.auth;
//
//import insideworld.engine.actions.keeper.Record;
//import insideworld.engine.datatransfer.endpoint.PreExecute;
//import insideworld.engine.security.common.auth.Auth;
//import insideworld.engine.security.common.auth.TokenContainer;
//import io.vertx.mutiny.amqp.AmqpMessage;
//import javax.inject.Inject;
//import javax.inject.Singleton;
//
//@Singleton
//public class AmqpReceiveAuth implements PreExecute<AmqpMessage> {
//
//    private final Auth<TokenContainer> auth;
//
//    @Inject
//    public AmqpReceiveAuth(final Auth<TokenContainer> auth) {
//        this.auth = auth;
//    }
//
//    @Override
//    public void preExecute(final Record context, final AmqpMessage parameter)
//        throws Exception {
//        this.auth.performAuth(
//            context,
//            () -> parameter.applicationProperties().getString("token")
//        );
//    }
//}
