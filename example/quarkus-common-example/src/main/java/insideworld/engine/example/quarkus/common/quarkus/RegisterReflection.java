package insideworld.engine.example.quarkus.common.quarkus;

import io.quarkus.runtime.annotations.RegisterForReflection;
//import org.apache.http.conn.ssl.NoopHostnameVerifier;

@RegisterForReflection(targets = {
//    NoopHostnameVerifier.class
})
public class RegisterReflection {
}
