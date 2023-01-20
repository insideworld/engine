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

package insideworld.engine.core.data.jpa.rw.entities;

import insideworld.engine.core.data.jpa.AbstractJpaEntity;
import java.util.Collection;
import javax.enterprise.context.Dependent;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Dependent
@Entity
@Table(schema = "entities", name = "primary")
public class PrimaryEntityImpl extends AbstractJpaEntity implements PrimaryEntity {

    @ManyToOne(targetEntity = ManyToOneEntityImpl.class)
    @JoinColumn(name = "many_to_one_id")
    private ManyToOneEntity manyToOne;

    @OneToMany(
        targetEntity = OneToManyEntityImpl.class,
        mappedBy = "primary"
    )
    private Collection<OneToManyEntity> oneToMany;

    private String value;

    @Override
    public ManyToOneEntity getManyToOne() {
        return this.manyToOne;
    }

    @Override
    public void setManyToOne(final ManyToOneEntity entity) {
        this.manyToOne = entity;
    }

    @Override
    public Collection<OneToManyEntity> getOneToMany() {
        return this.oneToMany;
    }

    @Override
    public void setOneToMany(final Collection<OneToManyEntity> entities) {
        this.oneToMany = entities;
    }

    @Override
    public String getValue() {
        return this.value;
    }

    @Override
    public void setValue(final String value) {
        this.value = value;
    }
}
