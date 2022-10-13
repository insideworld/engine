/*
 * Copyright (c) 2022 Anton Eliseev
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package insideworld.engine.data.jpa.extractor;

import insideworld.engine.actions.keeper.Record;
import insideworld.engine.actions.keeper.context.Context;
import insideworld.engine.actions.keeper.output.Output;
import insideworld.engine.data.jpa.schema.QueriesStorage;
import insideworld.engine.data.jpa.schema.Queries;
import insideworld.engine.entities.extractor.Extractor;
import insideworld.engine.injection.ObjectFactory;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.Tuple;
import javax.persistence.TupleElement;

@Singleton
public class JpaExtractor implements Extractor {

    private final QueriesStorage storage;
    private final EntityManager sessions;
    private final ObjectFactory factory;

    @Inject
    public JpaExtractor(final QueriesStorage storage,
                        final EntityManager sessions,
                        final ObjectFactory factory) {
        this.storage = storage;
        this.sessions = sessions;
        this.factory = factory;
    }

    @Override
    public Output extract(final Map<String, ?> context, final String name) {
        final Output output = this.factory.createObject(Output.class);
        final List<Tuple> results = this.createQuery(context, name).getResultList();
        for (final Tuple tuple : results) {
            final Record record = output.createRecord();
            for (TupleElement<?> element : tuple.getElements()) {
                final String alias = element.getAlias();
                final Object value = tuple.get(alias);
                if (value != null) {
                    record.put(alias, value);
                }
            }
        }
        return output;
    }

    @Override
    public Output extract(final Context context, String schema) {
        return this.extract(context.values(), schema);
    }

    @Override
    public int execute(final Map<String, ?> context, final String schema) {
        return this.createQuery(context, schema).executeUpdate();
    }

    @Override
    public int execute(final Context context, final String schema) {
        return this.execute(context.values(), schema);
    }

    private Query createQuery(final Map<String, ?> context, final String schema) {
        final Queries queries = this.storage.getQuery(schema);
        final Query query = this.sessions.createNativeQuery(queries.getQuery(), Tuple.class);
        for (final String field : queries.getInput()) {
            query.setParameter(field, context.get(field));
        }
        return query;
    }
}
