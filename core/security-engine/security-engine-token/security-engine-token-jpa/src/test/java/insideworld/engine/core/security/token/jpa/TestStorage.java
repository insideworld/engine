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

package insideworld.engine.core.security.token.jpa;

import insideworld.engine.core.data.core.StorageException;
import insideworld.engine.core.common.injection.ObjectFactory;
import insideworld.engine.core.security.core.data.UserStorage;
import insideworld.engine.core.security.token.base.Token;
import insideworld.engine.core.security.token.base.TokenStorage;
import insideworld.engine.frameworks.quarkus.test.database.DatabaseResource;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import javax.transaction.Transactional;
import org.junit.jupiter.api.Test;

@QuarkusTest
@QuarkusTestResource(DatabaseResource.class)
public class TestStorage {

    private final UserStorage users;
    private final TokenStorage tokens;
    private final ObjectFactory factory;

    TestStorage(final UserStorage users,
                final TokenStorage tokens,
                final ObjectFactory factory) {
        this.users = users;
        this.tokens = tokens;
        this.factory = factory;
    }

    @Test
    @Transactional
    final void test() throws StorageException {
//        final User system = this.users.getByName("system").get();
//        final Token token = this.factory.createObject(Token.class);
//        token.setUser(system);
//        token.setToken("systemtoken");
//        this.tokens.write(token);
        Token systemtoken = this.tokens.getByToken("qwerty").get();
        systemtoken.setToken("qwe");
        this.tokens.write(systemtoken);
        final Token token = this.factory.createObject(Token.class);
        token.setToken("rrrrrr");
        token.setUser(this.users.getByName("qqqqq").get());
//        System.out.println("qwe");
//        this.users.getByName("qwe");
        System.out.println("qwe");
    }

}
