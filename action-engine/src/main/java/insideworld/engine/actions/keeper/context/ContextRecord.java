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

import insideworld.engine.actions.ActionsTags;
import insideworld.engine.actions.keeper.MapRecord;
import insideworld.engine.actions.keeper.Record;
import insideworld.engine.actions.keeper.tags.MandatoryTag;
import insideworld.engine.actions.keeper.tags.Tag;
import insideworld.engine.injection.ObjectFactory;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;


@Dependent
public class ContextRecord extends MapRecord implements Context {

    private final ObjectFactory factory;
    private final Collection<MandatoryTag> mandatory;

    @Inject
    public ContextRecord(final ObjectFactory factory,
                         final Collection<MandatoryTag> mandatory) {
        this.factory = factory;
        this.mandatory = mandatory;
    }

    @Override
    public Context clone(final Tag<?>... includes) {
        final Context clone = this.factory.createObject(this.clazz());
        for (final Tag tag : includes) {
            clone.put(tag, this.get(tag));
        }
        this.fillMandatory(clone);
        this.removeSystem(clone);
        return clone;
    }

    @Override
    public Context clone() {
        final Context clone = this.factory.createObject(this.clazz());
        this.values().forEach(clone::put);
//        this.fillMandatory(clone);
        this.removeSystem(clone);
        return clone;
    }

    @Override
    public Context clone(final Record record) {
        final Context clone = this.factory.createObject(this.clazz());
        record.values().forEach(clone::put);
        this.fillMandatory(clone);
        this.removeSystem(clone);
        return clone;
    }

    private void fillMandatory(final Context clone) {
        final List<? extends Tag<?>> tags =
            this.mandatory.stream().map(MandatoryTag::get).collect(Collectors.toList());
        for (final Tag tag : tags) {
            clone.put(tag, this.get(tag));
        }
    }

    private void removeSystem(final Context clone) {
        clone.values().remove(ActionsTags.ACTION.getTag());
    }

    protected Class<? extends Context> clazz() {
        return ContextRecord.class;
    }
}
