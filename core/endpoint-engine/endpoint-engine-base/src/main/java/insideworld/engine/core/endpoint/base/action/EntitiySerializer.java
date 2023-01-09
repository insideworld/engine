package insideworld.engine.core.endpoint.base.action;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ResolvableSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;

public class EntitiySerializer<T extends AbstractEntity> extends StdSerializer<T> implements ResolvableSerializer {

    private final JsonSerializer<T> def;

    private static boolean qwe = false;
    public EntitiySerializer(JsonSerializer<?> def, Class<T> clazz) {
        super(clazz);
        this.def = (JsonSerializer<T>) def;
    }

    @Override
    public void resolve(final SerializerProvider provider) throws JsonMappingException {
        ((ResolvableSerializer) def).resolve(provider);
    }

    @Override
    public void serialize(final T value, final JsonGenerator gen, final SerializerProvider provider) throws IOException {
//        gen.writeNumberField("id", value.getId());
//        if (qwe) {
//            gen.writeArray();
//        }
        if (gen.getCurrentValue() == null) {
            def.serialize(value, gen, provider);
        } else {
            gen.writeNumber(value.getId());
        }
    }
}
