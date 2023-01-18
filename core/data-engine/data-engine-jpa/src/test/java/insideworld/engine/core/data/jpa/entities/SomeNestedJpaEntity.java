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

package insideworld.engine.core.data.jpa.entities;

import insideworld.engine.core.data.jpa.AbstractJpaIdEntity;
import javax.enterprise.context.Dependent;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * JPA implementation of nested entity.
 * @since 0.14.0
 */
@Dependent
@Entity
@Table(name = "some_nested_entity", schema = "transactions")
public class SomeNestedJpaEntity extends AbstractJpaIdEntity implements SomeNestedEntity {

    /**
     * One.
     */
    @Column(name = "one")
    private long one;

    /**
     * Two.
     */
    @Column(name = "two")
    private long two;

    @Override
    public Long getOne() {
        return this.one;
    }

    @Override
    public void setOne(final Long pone) {
        this.one = pone;
    }

    @Override
    public Long getTwo() {
        return this.two;
    }

    @Override
    public void setTwo(final Long ptwo) {
        this.two = ptwo;
    }
}
