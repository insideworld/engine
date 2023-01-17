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

import insideworld.engine.core.data.jpa.AbstractJpaEntity;
import javax.enterprise.context.Dependent;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.Any;
import org.hibernate.annotations.MetaValue;
import org.hibernate.annotations.Target;

/**
 * JPA entity implementation for tests.
 *
 * @since 0.14.0
 */
@Dependent
@Entity
@Table(name = "some_entity", schema = "transactions")
public class SomeJpaEntity extends AbstractJpaEntity implements SomeEntity {

    /**
     * Value.
     */
    @Column(name = "value")
    private String value;

    /**
     * Nested entity.
     */
    @ManyToOne()
    @JoinColumn(name = "some_nested_entity_id")
    @Target(SomeNestedJpaEntity.class)
    private SomeNestedEntity entity;

    @Override
    public String getValue() {
        return this.value;
    }

    @Override
    public void setValue(final String pvalue) {
        this.value = pvalue;
    }

    @Override
    public SomeNestedEntity getNestedEntity() {
        return this.entity;
    }

    @Override
    public void setNestedEntity(final SomeNestedEntity pentity) {
        this.entity = pentity;
    }
}
