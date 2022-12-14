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

package insideworld.engine.security.web.auth;

import insideworld.engine.actions.keeper.context.Context;
import insideworld.engine.datatransfer.endpoint.actions.EndpointProfile;
import insideworld.engine.security.token.base.AbstractTokenAuth;
import insideworld.engine.security.token.base.data.TokenStorage;
import insideworld.engine.web.RestProfile;
import insideworld.engine.web.tags.RestTags;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.core.HttpHeaders;

@Singleton
public class RestReceiveAuth extends AbstractTokenAuth {

    @Inject
    public RestReceiveAuth(final TokenStorage storage) {
        super(storage);
    }

    @Override
    protected String getToken(final Context parameter) {
        final HttpHeaders headers = parameter.get(RestTags.HTTP_HEADERS);
        if (headers == null) {
            throw new SecurityException("HTTP header is absent in context");
        } else {
            return headers.getHeaderString("Authorization");
        }
    }

    @Override
    public Class<? extends EndpointProfile> forProfile() {
        return RestProfile.class;
    }
}
