package insideworld.engine.entities.converter.jackson;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import insideworld.engine.entities.Entity;
import java.util.Map;

@Deprecated
public class ReplaceNamingStrategy extends PropertyNamingStrategy {

    @Override
    public String nameForField(MapperConfig<?> config, AnnotatedField field, String defaultName) {
        return defaultName;
    }

    @Override
    public String nameForGetterMethod(MapperConfig<?> config, AnnotatedMethod method, String defaultName) {
        final String name;
        if (Entity.class.isAssignableFrom(method.getRawReturnType())) {
            name = defaultName + "Id";
        } else {
            name = defaultName;
        }
        return name;
    }
}
