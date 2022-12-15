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

package insideworld.engine.core.security.token.amqp.auth;

import insideworld.engine.core.action.keeper.context.Context;
import insideworld.engine.core.endpoint.amqp.actions.AmqpReceiveProfile;
import insideworld.engine.core.endpoint.amqp.actions.tags.AmqpTags;
import insideworld.engine.core.endpoint.amqp.connection.Message;
import insideworld.engine.core.endpoint.base.action.EndpointProfile;
import insideworld.engine.core.security.core.SecurityException;
import insideworld.engine.core.security.token.base.AbstractTokenAuth;
import insideworld.engine.core.security.token.base.TokenStorage;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AmqpReceiveAuth extends AbstractTokenAuth {

    @Inject
    public AmqpReceiveAuth(final TokenStorage storage) {
        super(storage);
    }

    @Override
    protected final String getToken(Context parameter) throws SecurityException {
        final Message message = parameter.get(AmqpTags.MESSAGE);
        if (message == null) {
            throw new java.lang.SecurityException("Message is absent in context");
        } else {
            return (String) message.getProperties().get("Authorization");
        }
    }

    @Override
    public final Class<? extends EndpointProfile> forProfile() {
        return AmqpReceiveProfile.class;
    }
}
