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

package insideworld.engine.plugins.generator.data.action.read.specific.annotations;

import insideworld.engine.core.action.executor.Input;
import insideworld.engine.core.data.core.storages.Storage;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.inject.Scope;

/**
 * Generate action which using specific input interface to generate.
 * How it's work:
 * Input type should has fields with the same name in storage.
 * After that will execute provided method with call of input getters.
 * @since 2.0.0
 */
@Scope
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Repeatable(GenerateSpecificReadActions.class)
public @interface GenerateSpecificReadAction {

    /**
     * Storage which is using to read and entity.
     * @return Type of storage,
     */
    Class<? extends Storage<?>> storage();

    /**
     * Input type for actions.
     * @return Input type.
     */
    Class<?> inputType();

    String key();

    Class<?>[] interfaces() default {};

    /**
     * Method which need to call from storage.
     * @return Method name.
     */
    String method();

    /**
     * Method parameters.
     * @return
     */
    String[] parameters();

}
