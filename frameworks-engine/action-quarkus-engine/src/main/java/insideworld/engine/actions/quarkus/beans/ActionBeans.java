/*
 * Copyright (c) 2022 Anton Eliseev
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package insideworld.engine.actions.quarkus.beans;

import com.google.common.collect.ImmutableList;
import insideworld.engine.actions.Action;
import insideworld.engine.actions.PreExecutor;
import insideworld.engine.actions.chain.Link;
import insideworld.engine.actions.facade.ActionChanger;
import insideworld.engine.actions.facade.profiles.ExecuteProfile;
import insideworld.engine.actions.keeper.tags.MandatoryTag;
import insideworld.engine.quarkus.AbstractBeans;
import java.util.Collection;
import java.util.stream.Collectors;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;

public class ActionBeans extends AbstractBeans {

    @Produces
    public Collection<ExecuteProfile> executeProfiles(final Instance<ExecuteProfile> instance) {
        return this.fromInstance(instance);
    }

    @Produces
    public Collection<PreExecutor> executors(final Instance<PreExecutor> instance) {
        return this.fromInstance(instance);
    }

    @Produces
    public Collection<Link> links(final Instance<Link> instance) {
        return this.fromInstance(instance);
    }

    @Produces
    public Collection<Action> actions(final Instance<Action> instance) {
        return this.fromInstance(instance);
    }

    @Produces
    public Collection<ActionChanger> actionChangers(final Instance<ActionChanger> instance) {
        return this.fromInstance(instance);
    }

    @Produces
    public Collection<MandatoryTag> mandatoryTags(final Instance<MandatoryTag> instance) {
        return this.fromInstance(instance);
    }
}
