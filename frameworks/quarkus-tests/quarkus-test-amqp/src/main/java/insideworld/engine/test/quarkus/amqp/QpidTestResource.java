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

package insideworld.engine.test.quarkus.amqp;

import com.rabbitmq.client.ConnectionFactory;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;
import org.apache.qpid.server.SystemLauncher;
import org.apache.qpid.server.model.SystemConfig;

/**
 * Test resource to create im-memory AMQP server based on QPID.
 * @since 0.14.0
 */
@QuarkusTestResource(QpidTestResource.class)
public class QpidTestResource implements QuarkusTestResourceLifecycleManager {

    private static final String DEFAULT_INITIAL_CONFIGURATION_LOCATION = "initial-config.json";

    private final SystemLauncher launcher;

    public QpidTestResource() {
        this.launcher = new SystemLauncher();
    }

    @Override
    public Map<String, String> start() {
        final Map<String, Object> attributes = new HashMap<>();
        final URL initialConfigUrl = QpidTestResource.class
            .getClassLoader()
            .getResource(DEFAULT_INITIAL_CONFIGURATION_LOCATION);
        attributes.put(SystemConfig.TYPE, "Memory");
        attributes.put(
            SystemConfig.INITIAL_CONFIGURATION_LOCATION,
            initialConfigUrl.toExternalForm()
        );
        attributes.put(SystemConfig.STARTUP_LOGGED_TO_SYSTEM_OUT, true);
        try {
            this.launcher.startup(attributes);
            this.createChannel();
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
        return null;
    }

    @Override
    public void stop() {
        this.launcher.shutdown();
    }

    /**
     * QPID instead of RabbitMQ can't create topics automatically.
     * So will create a topic manually using RabbitMQ libs.
     */
    private void createChannel()
        throws URISyntaxException, NoSuchAlgorithmException,
        KeyManagementException, IOException, TimeoutException
    {
        final ConnectionFactory factory = new ConnectionFactory();
        factory.setUri("amqp://qqq:qqq@localhost:12345");
        factory.setHost("localhost");
        factory.setPort(12345);
        factory.setUsername("qqq");
        factory.setPassword("qqq");
        factory.setVirtualHost("localhost");
        var connection = factory.newConnection();
        var channel = connection.createChannel();
        channel.queueDeclare("test", false, false, false, null);
        channel.close();
        connection.close();
    }
}
