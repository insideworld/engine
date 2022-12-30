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

package insideworld.engine.core.common.keeper.context;

import insideworld.engine.core.common.keeper.Record;
import com.google.common.collect.Sets;
import insideworld.engine.core.common.injection.ObjectFactory;
import insideworld.engine.core.common.keeper.MapRecord;
import insideworld.engine.core.common.keeper.tags.Tag;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import org.apache.commons.lang3.ArrayUtils;

/**
 * Hash map based implementation of context based on MapRecord.
 * This class is final so if you need you own record - create a new class.
 * @since 0.0.1
 */
@Dependent
public final class ContextRecord extends MapRecord implements Context {

    /**
     * Object factory.
     */
    private final ObjectFactory factory;

    /**
     * Mandatory tags to clone.
     */
    private final List<MandatoryTag> mandatory;

    /**
     * System tags to won't clone.
     */
    private final List<SystemTag> systems;

    /**
     * Default constructor.
     * @param factory Object factory.
     * @param mandatory Collection of mandatory tags for clone.
     * @param systems Collection of system tags to exclude from clone.
     */
    @Inject
    public ContextRecord(
        final ObjectFactory factory,
        final List<MandatoryTag> mandatory,
        final List<SystemTag> systems) {
        this.factory = factory;
        this.mandatory = mandatory;
        this.systems = systems;
    }

    @Override
    public void cloneToRecord(final Record record, final String... includes) {
        final Collection<String> tocopy = Sets.newHashSet();
        if (ArrayUtils.isEmpty(includes)) {
            tocopy.addAll(this.values().keySet());
        } else {
            tocopy.addAll(List.of(includes));
        }
        this.mandatory.forEach(tag -> tocopy.add(tag.get().getTag()));
        this.systems.forEach(tag -> tocopy.remove(tag.get().getTag()));
        tocopy.forEach(tag -> record.put(tag, this.get(tag)));
    }

    @Override
    public Context cloneContext(final Tag<?>... includes) {
        final Context clone = this.factory.createObject(clazz());
        this.cloneToRecord(clone, Arrays.stream(includes).map(Tag::getTag).toArray(String[]::new));
        return clone;
    }

    /**
     * Class of new clone context.
     * @return Type of context which need create using clone.
     */
    private static Class<? extends Context> clazz() {
        return ContextRecord.class;
    }
}
