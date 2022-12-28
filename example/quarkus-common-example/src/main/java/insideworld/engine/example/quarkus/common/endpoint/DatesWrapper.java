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

package insideworld.engine.example.quarkus.common.endpoint;

import insideworld.engine.core.action.ActionException;
import insideworld.engine.core.action.executor.profiles.ExecuteProfile;
import insideworld.engine.core.action.executor.profiles.wrapper.AbstractExecuteWrapper;
import insideworld.engine.core.action.executor.profiles.wrapper.WrapperContext;
import insideworld.engine.core.action.keeper.context.Context;
import insideworld.engine.core.common.exception.CommonException;
import insideworld.engine.core.endpoint.rest.RestProfile;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.List;
import javax.inject.Singleton;

@Singleton
public class DatesWrapper extends AbstractExecuteWrapper {

    private final ThreadLocal<SimpleDateFormat> formatter = ThreadLocal.withInitial(
        () -> new SimpleDateFormat("yyyy-MM-dd")
    );

    @Override
    public final void execute(final WrapperContext context)
        throws CommonException {
        try {
            this.convertDate("date", context.context());
        } catch (final ParseException exp) {
            throw new ActionException(context.action(), "Can't parse dates", exp);
        }
        super.execute(context);
    }

    @Override
    public final Collection<Class<? extends ExecuteProfile>> forProfile() {
        return List.of(RestProfile.class);
    }

    @Override
    public final long wrapperOrder() {
        return 2_000_000;
    }

    private void convertDate(final String tag, final Context input) throws ParseException {
        if (!input.contains(tag)) {
            return;
        }
        input.put(tag, this.formatter.get().parse(input.get(tag)), true);
    }
}
