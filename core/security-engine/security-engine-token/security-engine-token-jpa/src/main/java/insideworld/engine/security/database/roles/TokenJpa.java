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

package insideworld.engine.security.database.roles;

import insideworld.engine.security.core.data.User;
import insideworld.engine.security.jpa.data.UserJpa;
import insideworld.engine.security.token.base.data.Token;
import java.io.Serializable;
import javax.enterprise.context.Dependent;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(schema = "security", name = "token")
@Dependent
@Cacheable
public class TokenJpa implements Token, Serializable {

    @Id
    @OneToOne()
    @JoinColumn(name = "user_id")
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private UserJpa user;

    @Column(name = "token", unique = true, nullable = false)
    private String token;

    @Override
    public String getToken() {
        return this.token;
    }

    @Override
    public void setToken(final String token) {
        this.token = token;
    }

    @Override
    public User getUser() {
        return this.user;
    }

    @Override
    public void setUser(final User user) {
        this.user = (UserJpa) user;
    }

    @Override
    public final int hashCode() {
        return this.user.hashCode();
    }

    @Override
    public final boolean equals(final Object obj) {
        return obj != null
               && this.getClass().equals(obj.getClass())
               && this.user.getId() == ((TokenJpa) obj).getUser().getId();
    }
}
