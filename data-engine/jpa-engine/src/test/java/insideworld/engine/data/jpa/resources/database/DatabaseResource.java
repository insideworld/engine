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

package insideworld.engine.data.jpa.resources.database;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.h2.H2DatabaseTestResource;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Map;
import org.h2.tools.Server;

/**
 * Database resources based on H2 in-memory.
 *
 * @since 0.14.0
 */
@QuarkusTestResource(DatabaseResource.class)
public class DatabaseResource extends H2DatabaseTestResource {

    /**
     * Connection parameters.
     *
     * @checkstyle LineLengthCheck (10 lines)
     * @checkstyle StringLiteralsConcatenationCheck (10 lines)
     */
    private static final String CONNECTION_PARAM =
        "jdbc:h2:tcp://localhost/mem:test;"
        + "DB_CLOSE_DELAY=-1;DB_CLOSE_DELAY=-1;MODE=PostgreSQL;INIT="
        + "RUNSCRIPT FROM 'classpath:insideworld/engine/data/jpa/resources/database/engine.sql'\\;"
        + "RUNSCRIPT FROM 'classpath:insideworld/engine/data/jpa/resources/database/transactions/entities.sql'\\;";

    /**
     * TCP server instance.
     */
    private final Server server;

    /**
     * Constructor.
     *
     * @throws SQLException Can't create sql server.
     */
    public DatabaseResource() throws SQLException {
        this.server = Server.createTcpServer("-tcpAllowOthers", "-ifNotExists");
    }

    @Override
    public final Map<String, String> start() {
        try {
            this.server.start();
            DriverManager.getConnection(DatabaseResource.CONNECTION_PARAM, "", "");
        } catch (final SQLException exp) {
            throw new IllegalStateException(exp);
        }
        return Collections.emptyMap();
    }

    @Override
    public final void stop() {
        this.server.stop();
    }
}
