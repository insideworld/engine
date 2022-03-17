package insideworld.engine.integration.resources.database;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.h2.H2DatabaseTestResource;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Map;
import org.h2.tools.Server;

@QuarkusTestResource(DatabaseResource.class)
public class DatabaseResource extends H2DatabaseTestResource {

    private Server tcpServer;

    public Map<String, String> start() {
        try {
            this.tcpServer = Server.createTcpServer("-tcpAllowOthers", "-ifNotExists");
            this.tcpServer.start();
            System.out.println("[INFO] H2 database started in TCP server mode; server status: " + this.tcpServer.getStatus());
            DriverManager.getConnection("jdbc:h2:tcp://localhost/mem:test;" +
                    "DB_CLOSE_DELAY=-1;DB_CLOSE_DELAY=-1;MODE=PostgreSQL;" +
                    "INIT="+
                    "RUNSCRIPT FROM 'classpath:engine/database/engine.sql'\\;" +
                    "RUNSCRIPT FROM 'classpath:engine/database/users.sql'\\;" +
                    "RUNSCRIPT FROM 'classpath:engine/database/integration/users.sql'\\;" +
                    "RUNSCRIPT FROM 'classpath:engine/database/integration/entities.sql'\\;" +
                    "RUNSCRIPT FROM 'classpath:engine/database/integration/engine.sql'\\;"
                    ,"","");
        } catch (SQLException var2) {
            throw new RuntimeException(var2);
        }

        return Collections.emptyMap();
    }

    public synchronized void stop() {
        if (this.tcpServer != null) {
            this.tcpServer.stop();
            System.out.println("[INFO] H2 database was shut down; server status: " + this.tcpServer.getStatus());
            this.tcpServer = null;
        }

    }
}
