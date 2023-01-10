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

package insideworld.engine.security.common.action;

import insideworld.engine.core.action.ActionException;
import insideworld.engine.core.common.exception.CommonException;
import insideworld.engine.core.security.core.action.RoleAction;
import insideworld.engine.core.security.core.data.Role;
import insideworld.engine.security.common.stubs.Roles;
import java.util.Collection;
import java.util.Collections;
import javax.inject.Singleton;

@Singleton
public class TwoAction implements RoleAction<Object, Object> {

    @Override
    public Object execute(final Object input) throws CommonException {
        return null;
    }

    @Override
    public String key() {
        return "insideworld.engine.security.common.action.TwoAction";
    }

    @Override
    public Class<?> inputType() {
        return Object.class;
    }

    @Override
    public Class<?> outputType() {
        return Object.class;
    }

    @Override
    public Collection<Role> role() throws ActionException {
        return Collections.singleton(Roles.TWO);
    }
}
