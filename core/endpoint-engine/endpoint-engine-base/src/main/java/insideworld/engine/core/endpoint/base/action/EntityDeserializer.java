package insideworld.engine.core.endpoint.base.action;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.ResolvableDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import java.io.IOException;

public class EntityDeserializer<T extends AbstractEntity> extends StdDeserializer<T> implements ResolvableDeserializer
{

    private final JsonDeserializer<?> defaultDeserializer;


    public EntityDeserializer(JsonDeserializer<?> defaultDeserializer, Class<?> clazz) {
        super(clazz);
        this.defaultDeserializer = defaultDeserializer;
    }

    @Override
    public T deserialize(JsonParser jp, DeserializationContext ctxt)
        throws IOException, JsonProcessingException
    {
        if (jp.getCurrentToken().isStructStart()) {
            return (T) defaultDeserializer.deserialize(jp, ctxt);
        }
        final JsonNode treeNode = jp.getCodec().readTree(jp);
        final int i = treeNode.intValue();
        // Special logic

        return (T) new NestedEntityImpl();
    }

    // for some reason you have to implement ResolvableDeserializer when modifying BeanDeserializer
    // otherwise deserializing throws JsonMappingException??
    @Override public void resolve(DeserializationContext ctxt) throws JsonMappingException
    {
        ((ResolvableDeserializer) defaultDeserializer).resolve(ctxt);
    }
}