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

package insideworld.engine.actions.chain.execute;

import insideworld.engine.actions.chain.Link;
import insideworld.engine.actions.keeper.tags.Tag;

/**
 * Execute action link.
 * Using to execute specific action with specific key.
 * @param <T> Key type for execute an action.
 * @since 0.1.0
 */
public interface ExecuteActionLink<T> extends Link {
    /**
     * Set tags for copy from parent context to child.
     * If not set all tags will copy to child context exclude system specific.
     * @param tags Tags array to propagate.
     * @return The same instance.
     */
    ExecuteActionLink<T> setTags(Tag<?>... tags);

    /**
     * Set action which need to execute.
     * @param key Action key.
     * @return The same instance.
     */
    ExecuteActionLink<T> setKey(T key);

    /**
     * Add pre execute functions.
     * This code call before execute an action.
     * Typically use for prepare some input data for execute action.
     * If one of PreExecute return false - action won't execute.
     * @param execute PreExecute instances.
     * @return The same instance.
     */
    ExecuteActionLink<T> addPreExecute(PreExecute... execute);

    /**
     * The same as addPreExecute but using DI or another mechanism to take object by class.
     * @param execute Classes of PreExecute instance.
     * @return The same instance.
     */
    ExecuteActionLink<T> addPreExecute(Class<? extends PreExecute>... execute);

    /**
     * Add post execute functions.
     * The code call after execute an action.
     * Typically using for copy some data from child to parent output.
     * In case if no one PostExecute not defined will merge whole parent and child output.
     * @param execute PostExecute instances.
     * @return The same instance.
     */
    ExecuteActionLink<T> addPostExecute(PostExecute... execute);

    /**
     * The same as addPreExecute but using DI or another mechanism to take object by class.
     * @param execute Classes of PostExecute instance.
     * @return The same instance.
     */
    ExecuteActionLink<T> addPostExecute(Class<? extends PostExecute>... execute);

    /**
     * Use the same transaction in child action.
     * Will execute in separate transaction by default.
     * @return The same instance.
     */
    ExecuteActionLink<T> useSameTx();
}
