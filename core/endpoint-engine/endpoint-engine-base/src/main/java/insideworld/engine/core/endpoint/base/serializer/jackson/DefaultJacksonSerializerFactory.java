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

package insideworld.engine.core.endpoint.base.serializer.jackson;


import com.fasterxml.jackson.databind.module.SimpleModule;
import insideworld.engine.core.common.injection.ObjectFactory;
import insideworld.engine.core.endpoint.base.serializer.jackson.adaptors.JacksonTypeAdaptor;
import insideworld.engine.core.endpoint.base.serializer.types.Type;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Serializer factory for all types which not bound to another factories based on Jackson.
 *
 * @since 2.0.0
 */
@Singleton
public class DefaultJacksonSerializerFactory extends AbstractJacksonSerializerFactory {

    @Inject
    public DefaultJacksonSerializerFactory(
        final List<JacksonTypeAdaptor> adaptors
    ) {
        super(adaptors);
    }

    @Override
    public long order() {
        return 0;
    }

    @Override
    public boolean can(final Type type) {
        return true;
    }

    @Override
    protected void modifyModule(final SimpleModule module) {
        //Nothing to do.
    }
}
