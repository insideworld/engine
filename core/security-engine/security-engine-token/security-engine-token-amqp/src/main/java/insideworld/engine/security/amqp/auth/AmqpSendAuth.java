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
//import insideworld.engine.properties.PropertiesException;
//import insideworld.engine.properties.PropertiesProvider;
//import insideworld.engine.security.common.entities.User;
//import insideworld.engine.security.common.storages.UserStorage;
//import io.vertx.core.json.JsonObject;
//import io.vertx.mutiny.amqp.AmqpMessageBuilder;
//import javax.inject.Inject;
//import javax.inject.Singleton;
//import javax.naming.AuthenticationException;
//import org.eclipse.microprofile.config.inject.ConfigProperty;
//
//@Singleton
//public class AmqpSendAuth implements PreExecute<AmqpMessageBuilder> {
//
//    private final User user;
//
//    @Inject
//    public AmqpSendAuth(final PropertiesProvider properties,
//                        final UserStorage storage)
//        throws AuthenticationException, PropertiesException {
//        final String username = properties.provide("engine.amqp.username", String.class);
//        this.user = storage.getByName(username).orElseThrow(
//            () -> new AuthenticationException("User for AMQP auth is not found")
//        );
//    }
//
//    @Override
//    public void preExecute(final Record context, final AmqpMessageBuilder parameter) throws Exception {
//        parameter.applicationProperties(new JsonObject().put("token", this.user.getToken()));
//    }
//}
