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

package insideworld.engine.core.data.core.mock.entities.positive;

import java.util.Collection;
import java.util.Date;
import javax.enterprise.context.Dependent;

/**
 * Mock entity implementation.
 * Using for success cases.
 * @since 0.14.0
 */
@Dependent
public class MockEntityImpl implements MockEntity {

    /**
     * Id.
     */
    private long id;

    /**
     * String primitive.
     */
    private String strprim;

    /**
     * String primitive.
     */
    private Long wrapprim;

    /**
     * String primitive.
     */
    private long prim;

    /**
     * Collections of string primitives.
     */
    private Collection<String> strprims;

    /**
     * Wrapped primitives.
     */
    private Collection<Long> wrapprims;

    /**
     * Date.
     */
    private Date date;

    /**
     * Dates.
     */
    private Collection<Date> dates;

    /**
     * One.
     */
    private MockOneEntity one;

    /**
     * Twos.
     */
    private Collection<MockTwoEntity> twos;

    /**
     * Using to test on null.
     */
    private String testnull;

    @Override
    public final long getId() {
        return this.id;
    }

    @Override
    public final void setId(final Long pid) {
        if (pid != null) {
            this.id = pid;
        }
    }

    @Override
    public final String getStrprim() {
        return this.strprim;
    }

    @Override
    public final void setStrprim(final String pstrprimitive) {
        this.strprim = pstrprimitive;
    }

    @Override
    public final Long getWrapprim() {
        return this.wrapprim;
    }

    @Override
    public final void setWrapprim(final Long pwrapprimitive) {
        this.wrapprim = pwrapprimitive;
    }

    @Override
    public final long getPrim() {
        return this.prim;
    }

    @Override
    public final void setPrim(final long pprimitive) {
        this.prim = pprimitive;
    }

    @Override
    public final Collection<String> getStrprims() {
        return this.strprims;
    }

    @Override
    public final void setStrprims(final Collection<String> pstrprims) {
        this.strprims = pstrprims;
    }

    @Override
    public final Collection<Long> getWrapprims() {
        return this.wrapprims;
    }

    @Override
    public final void setWrapprims(final Collection<Long> pwrapprims) {
        this.wrapprims = pwrapprims;
    }

    @Override
    public final Date getDate() {
        return this.date;
    }

    @Override
    public final void setDate(final Date pdate) {
        this.date = pdate;
    }

    @Override
    public final Collection<Date> getDates() {
        return this.dates;
    }

    @Override
    public final void setDates(final Collection<Date> pdates) {
        this.dates = pdates;
    }

    @Override
    public final MockOneEntity getOne() {
        return this.one;
    }

    @Override
    public final void setOne(final MockOneEntity pone) {
        this.one = pone;
    }

    @Override
    public final Collection<MockTwoEntity> getTwos() {
        return this.twos;
    }

    @Override
    public final void setTwos(final Collection<MockTwoEntity> ptwos) {
        this.twos = ptwos;
    }

    @Override
    public final String getTestnull() {
        return this.testnull;
    }

    @Override
    public final void setTestnull(final String ptestnull) {
        this.testnull = ptestnull;
    }

}
