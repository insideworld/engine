package insideworld.engine.entities.converter.jackson;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import insideworld.engine.entities.Entity;
import java.io.IOException;
import javax.inject.Inject;
import javax.inject.Singleton;

//@Singleton
@Deprecated
public class EntityDeserializer extends StdDeserializer<Entity> {

    @Inject
    public EntityDeserializer() {
        super(Entity.class);
    }

    @Override
    public Entity deserialize(JsonParser p, DeserializationContext ctxt)
        throws IOException, JacksonException {
        return null;
    }
}
