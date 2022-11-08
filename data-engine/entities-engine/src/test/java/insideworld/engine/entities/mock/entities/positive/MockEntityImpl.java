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

package insideworld.engine.entities.mock.entities.positive;

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
     * Value.
     */
    private String value;

    /**
     * Date.
     */
    private Date date;

    /**
     * Dates.
     */
    private Collection<Date> dates;

    /**
     * Values.
     */
    private Collection<Long> values;

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
    public final String getValue() {
        return this.value;
    }

    @Override
    public final void setValue(final String pvalue) {
        this.value = pvalue;
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
    public final Collection<Long> getValues() {
        return this.values;
    }

    @Override
    public final void setValues(final Collection<Long> pvalues) {
        this.values = pvalues;
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
