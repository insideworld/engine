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

import insideworld.engine.core.data.core.Entity;
import java.util.Collection;
import java.util.Date;

/**
 * Mock entity.
 * Using for tests.
 *
 * @since 0.14.0
 */
public interface MockEntity extends Entity {

    /**
     * Set id.
     *
     * @param id ID.
     */
    void setId(Long id);

    /**
     * Getter for string primitive.
     * @return String primitive.
     */
    String getStrprim();

    /**
     * Setter string primitive.
     * @param strprimitive Value.
     */
    void setStrprim(String strprimitive);

    /**
     * Getter for wrapped primitive.
     * @return Wrapped primitive.
     */
    Long getWrapprim();

    /**
     * Setter for wrapped primitive.
     * @param wrapprimitive Value.
     */
    void setWrapprim(Long wrapprimitive);

    /**
     * Getter for primitive.
     * @return Primitive.
     */
    long getPrim();

    /**
     * Setter for primitive.
     * @param primitive Value.
     */
    void setPrim(long primitive);

    /**
     * Getter for strings.
     * @return Strings.
     */
    Collection<String> getStrprims();

    /**
     * Setter strings.
     * @param pstrprims Strings.
     */
    void setStrprims(Collection<String> pstrprims);

    /**
     * Getter for wrapped primitives.
     * @return Primitives.
     */
    Collection<Long> getWrapprims();

    /**
     * Wrapper primitives.
     * @param pwrapprims Primitives.
     */
    void setWrapprims(Collection<Long> pwrapprims);

    /**
     * Get date.
     *
     * @return Date.
     */
    Date getDate();

    /**
     * Set date.
     *
     * @param date Date.
     */
    void setDate(Date date);

    /**
     * Get dates.
     *
     * @return Dates.
     */
    Collection<Date> getDates();

    /**
     * Set dates.
     *
     * @param dates Dates.
     */
    void setDates(Collection<Date> dates);

    /**
     * Get one.
     *
     * @return One.
     */
    MockOneEntity getOne();

    /**
     * Set one.
     *
     * @param one One.
     */
    void setOne(MockOneEntity one);

    /**
     * Get tows.
     *
     * @return Tows.
     */
    Collection<MockTwoEntity> getTwos();

    /**
     * Set twos.
     *
     * @param twos Twos.
     */
    void setTwos(Collection<MockTwoEntity> twos);

    /**
     * Using to test null objects.
     *
     * @return Null.
     */
    String getTestnull();

    /**
     * Using to test null objects.
     * @param ptestnull Null.
     */
    void setTestnull(String ptestnull);

}
