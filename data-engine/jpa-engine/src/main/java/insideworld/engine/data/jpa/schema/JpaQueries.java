/*
 * Copyright (c) 2022 Anton Eliseev
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package insideworld.engine.data.jpa.schema;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.enterprise.context.Dependent;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(schema = "engine", name = "sql_queries")
@Dependent
@Cacheable
public class JpaQueries implements Queries {

    @Id
    @Column(name = "id", unique = true, nullable = false)
    public String name;

    @Column(name = "query")
    public String query;

    @Column(name = "input")
    public String input;

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getQuery() {
        return this.query;
    }

    @Override
    public List<String> getInput() {
        final List<String> result;
        if (this.input == null) {
            result = Collections.emptyList();
        } else {
            result = Arrays.asList(this.input.split(","));
        }
        return result;
    }
}
