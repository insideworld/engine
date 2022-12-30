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
//package insideworld.engine.core.data.core.action.links;
//
//import insideworld.engine.core.action.chain.Link;
//import insideworld.engine.core.action.chain.LinkException;
//import insideworld.engine.core.common.exception.CommonException;
//import insideworld.engine.core.data.core.Entity;
//import insideworld.engine.core.data.core.StorageException;
//import insideworld.engine.core.data.core.storages.Storage;
//import insideworld.engine.core.data.core.storages.keeper.StorageKeeper;
//import java.util.Collection;
//import java.util.function.BiConsumer;
//import java.util.function.Function;
//import javax.enterprise.context.Dependent;
//import javax.inject.Inject;
//import org.apache.commons.collections4.CollectionUtils;
//import org.apache.commons.lang3.tuple.Pair;
//
///**
// * Read an entity by presents tags.
// * This is link required to init.
// * Need to set one or both tags - for multiple and single entity. Also need to set a type.
// * In case if ID won't present in context:
// * - For single tag just skip filling.
// * - For multiple tag will read all entities.
// * All results will put in context.
// *
// * @param <T> Entity type.
// * @since 0.0.1
// */
//@Dependent
//public class ReadEntityLink<T extends Entity, I> implements Link<I> {
//
//    /**
//     * Storage keeper.
//     */
//    private final StorageKeeper storages;
//
//    /**
//     * Storage for provided type.
//     */
//    private Storage<T> storage;
//
//    private Pair<Function<I, Long>, BiConsumer<T, I>> single;
//
//    private Pair<Function<I, Collection<Long>>, BiConsumer<Collection<T>, I>> multiple;
//
//    /**
//     * Constructor.
//     *
//     * @param storages Storage keeper instance.
//     */
//    @Inject
//    public ReadEntityLink(final StorageKeeper storages) {
//        this.storages = storages;
//    }
//
//    @Override
//    public boolean process(final I input) throws CommonException {
//        if (this.single != null) {
//            final Long id = this.single.getLeft().apply(input);
//            if (id != null) {
//                this.single.getRight().accept(
//                    this.storage.read(id),
//                    input
//                );
//            }
//        }
//        if (this.multiple != null) {
//            final Collection<Long> ids = this.multiple.getLeft().apply(input);
//            if (CollectionUtils.isEmpty(ids)) {
//                this.multiple.getRight().accept(this.storage.readAll(), input);
//            } else {
//                this.multiple.getRight().accept(this.storage.read(ids), input);
//            }
//        }
//        return true;
//    }
//
//    /**
//     * Set type of entity.
//     *
//     * @param type Entity type.
//     * @return The same instance
//     * @throws StorageException Can't find storage.
//     */
//    public ReadEntityLink<T, I> setType(final Class<T> type) throws StorageException {
//        this.storage = this.storages.getStorage(type);
//        return this;
//    }
//
//    public ReadEntityLink<T, I> setSingle(
//        final Function<I, Long> ids, final BiConsumer<T, I> result
//    ) throws LinkException {
//        if (ids == null || result == null) {
//            throw new LinkException(this, "One or both arguments is null");
//        }
//        this.single = Pair.of(ids, result);
//        return this;
//    }
//
//    public ReadEntityLink<T, I> setMultiple(
//        final Function<I, Collection<Long>> ids,
//        final BiConsumer<Collection<T>, I> results
//    ) throws LinkException {
//        if (ids == null || results == null) {
//            throw new LinkException(this, "One or both arguments is null");
//        }
//        this.multiple = Pair.of(ids, results);
//        return this;
//    }
//}
