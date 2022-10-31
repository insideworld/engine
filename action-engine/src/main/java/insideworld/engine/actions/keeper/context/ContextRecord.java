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

package insideworld.engine.actions.keeper.context;

import insideworld.engine.actions.keeper.MapRecord;
import insideworld.engine.actions.keeper.Record;
import insideworld.engine.actions.keeper.tags.Tag;
import insideworld.engine.injection.ObjectFactory;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

/**
 * Hash map based implementation of context based on MapRecord.
 * @since 0.0.1
 */
@Dependent
public class ContextRecord extends MapRecord implements Context {

    /**
     * Object factory.
     */
    private final ObjectFactory factory;

    /**
     * Mandatory tags to clone.
     */
    private final Collection<MandatoryTag> mandatory;

    /**
     * System tags to won't clone.
     */
    private final Collection<SystemTag> systems;

    /**
     * Default constructor.
     * @param factory Object factory.
     * @param mandatory Collection of mandatory tags for clone.
     * @param systems Collection of system tags to exclude from clone.
     */
    @Inject
    public ContextRecord(
        final ObjectFactory factory,
        final Collection<MandatoryTag> mandatory,
        final Collection<SystemTag> systems) {
        this.factory = factory;
        this.mandatory = mandatory;
        this.systems = systems;
    }

    @Override
    public final Context cloneContext(final Tag<?>... includes) {
        final Context clone = this.factory.createObject(this.clazz());
        for (final Tag tag : includes) {
            clone.put(tag, this.get(tag));
        }
        this.fillMandatory(clone);
        this.removeSystem(clone);
        return clone;
    }

    @Override
    public final Context cloneContext() {
        final Context clone = this.factory.createObject(this.clazz());
        this.values().forEach(clone::put);
        this.removeSystem(clone);
        return clone;
    }

    @Override
    public final Context cloneContext(final Record record) {
        final Context clone = this.factory.createObject(this.clazz());
        record.values().forEach(clone::put);
        this.fillMandatory(clone);
        this.removeSystem(clone);
        return clone;
    }

    /**
     * Class of new clone context.
     * Using for inheritance.
     * @return Type of context which need create using clone.
     * @checkstyle NonStaticMethodCheck (2 lines)
     */
    protected Class<? extends Context> clazz() {
        return ContextRecord.class;
    }

    /**
     * Fill mandatory tags to cloned context.
     * @param clone Cloned context.
     */
    private void fillMandatory(final Context clone) {
        final List<? extends Tag<?>> tags =
            this.mandatory.stream().map(MandatoryTag::get).collect(Collectors.toList());
        for (final Tag tag : tags) {
            clone.put(tag, this.get(tag));
        }
    }

    /**
     * Remove system tags from cloned context.
     * @param clone Cloned context.
     */
    private void removeSystem(final Context clone) {
        final Map<String, ? super Object> values = clone.values();
        for (final SystemTag system : this.systems) {
            values.remove(system.get().getTag());
        }
    }
}
