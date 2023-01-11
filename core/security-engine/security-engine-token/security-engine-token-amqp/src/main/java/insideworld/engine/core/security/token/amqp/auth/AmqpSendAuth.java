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
//package insideworld.engine.core.security.token.amqp.auth;
//
//import insideworld.engine.core.endpoint.amqp.connection.Message;
//import insideworld.engine.core.endpoint.base.action.PreSend;
//import insideworld.engine.core.common.exception.CommonException;
//import insideworld.engine.core.common.properties.PropertiesProvider;
//import insideworld.engine.core.security.core.data.User;
//import insideworld.engine.core.security.core.data.UserStorage;
//import insideworld.engine.core.security.token.base.TokenStorage;
//import insideworld.engine.core.common.startup.OnStartUp;
//import javax.inject.Inject;
//import javax.inject.Singleton;
//
//@Singleton
//public class AmqpSendAuth implements PreSend<Message>, OnStartUp {
//
//    private final PropertiesProvider properties;
//    private final TokenStorage tokens;
//    private final UserStorage users;
//
//    private String token;
//
//    @Inject
//    public AmqpSendAuth(final PropertiesProvider properties,
//                        final TokenStorage tokens,
//                        final UserStorage users
//                        ) {
//        this.properties = properties;
//        this.tokens = tokens;
//        this.users = users;
//    }
//
//    @Override
//    public final void execute(final Message parameter) {
//        parameter.getProperties().put("token", this.token);
//    }
//
//    @Override
//    public final void startUp() throws CommonException {
//        final String username = this.properties.provide("engine.amqp.username", String.class);
//        final User user = this.users.getByName(username).orElseThrow(
//            () -> new SecurityException("User for AMQP auth is not found")
//        );
//        this.token = this.tokens.getByUser(user).getToken();
//    }
//
//    @Override
//    public final long startOrder() {
//        return 100_000;
//    }
//}
