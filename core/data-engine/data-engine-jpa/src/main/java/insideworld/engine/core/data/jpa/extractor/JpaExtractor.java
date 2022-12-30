///*
// * Copyright (c) 2022 Anton Eliseev
// *
// * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
// * associated documentation files (the "Software"), to deal in the Software without restriction,
// * including without limitation the rights to use, copy, modify, merge, publish, distribute,
// * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software
// * is furnished to do so, subject to the following conditions:
// *
// * The above copyright notice and this permission notice shall be included in all copies or
// * substantial portions of the Software.
// *
// * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
// * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
// * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
// * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
// */
//
//package insideworld.engine.core.data.jpa.extractor;
//
//import insideworld.engine.core.action.keeper.Record;
//import insideworld.engine.core.action.keeper.output.Output;
//import insideworld.engine.core.data.jpa.extractor.schema.Queries;
//import insideworld.engine.core.data.jpa.extractor.schema.QueriesStorage;
//import insideworld.engine.core.data.core.extractor.Extractor;
//import insideworld.engine.core.common.injection.ObjectFactory;
//import java.util.List;
//import java.util.Map;
//import javax.inject.Inject;
//import javax.inject.Singleton;
//import javax.persistence.EntityManager;
//import javax.persistence.Query;
//import javax.persistence.Tuple;
//import javax.persistence.TupleElement;
//
///**
// * Implementation of extractor to work with relational database.
// *
// * @since 0.0.1
// */
//@Singleton
//public class JpaExtractor implements Extractor {
//
//    /**
//     * Query storage.
//     */
//    private final QueriesStorage storage;
//
//    /**
//     * Entity manager.
//     */
//    private final EntityManager sessions;
//
//    /**
//     * Object factory.
//     */
//    private final ObjectFactory factory;
//
//    /**
//     * Default constructor.
//     *
//     * @param storage Query storage.
//     * @param sessions Entity manager.
//     * @param factory Object factory.
//     */
//    @Inject
//    public JpaExtractor(
//        final QueriesStorage storage,
//        final EntityManager sessions,
//        final ObjectFactory factory
//    ) {
//        this.storage = storage;
//        this.sessions = sessions;
//        this.factory = factory;
//    }
//
//    @Override
//    public final Output extract(final Map<String, ?> context, final String name) {
//        final Output output = this.factory.createObject(Output.class);
//        final List<Tuple> results = this.createQuery(context, name).getResultList();
//        for (final Tuple tuple : results) {
//            final Record record = output.createRecord();
//            for (final TupleElement<?> element : tuple.getElements()) {
//                final String alias = element.getAlias();
//                final Object value = tuple.get(alias);
//                if (value != null) {
//                    record.put(alias, value);
//                }
//            }
//        }
//        return output;
//    }
//
//    @Override
//    public final Output extract(final Record record, final String schema) {
//        return this.extract(record.values(), schema);
//    }
//
//    @Override
//    public final int execute(final Map<String, ?> context, final String schema) {
//        return this.createQuery(context, schema).executeUpdate();
//    }
//
//    @Override
//    public final int execute(final Record record, final String schema) {
//        return this.execute(record.values(), schema);
//    }
//
//    /**
//     * Create query instance based on input parameters and schema.
//     *
//     * @param context Input parameters.
//     * @param schema Schema name.
//     * @return Query instance.
//     */
//    private Query createQuery(final Map<String, ?> context, final String schema) {
//        final Queries queries = this.storage.getQuery(schema);
//        final Query query = this.sessions.createNativeQuery(queries.getQuery(), Tuple.class);
//        for (final String field : queries.getInput()) {
//            query.setParameter(field, context.get(field));
//        }
//        return query;
//    }
//}
