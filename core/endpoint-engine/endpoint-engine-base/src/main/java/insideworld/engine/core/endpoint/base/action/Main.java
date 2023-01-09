package insideworld.engine.core.endpoint.base.action;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;

public class Main {

    public static void main(String[] args) throws JsonProcessingException {
        final ObjectMapper mapper = new ObjectMapper();
//        mapper.setPropertyNamingStrategy(new ReplaceNamingStrategy());
        SimpleModule module = new SimpleModule();
        module.setDeserializerModifier(new BeanDeserializerModifier() {
            @Override
            public JsonDeserializer<?> modifyDeserializer(DeserializationConfig config, BeanDescription beanDesc, JsonDeserializer<?> deserializer) {
                if (AbstractEntity.class.isAssignableFrom(beanDesc.getBeanClass())) {
                    return new EntityDeserializer<>(deserializer, beanDesc.getBeanClass());
                }
                return deserializer;
            }
        });
        module.setSerializerModifier(new BeanSerializerModifier() {

            @Override
            public JsonSerializer<?> modifySerializer(SerializationConfig config, BeanDescription beanDesc, JsonSerializer<?> serializer) {
                if (AbstractEntity.class.isAssignableFrom(beanDesc.getBeanClass())) {
                    return new EntitiySerializer<>(serializer, (Class<? extends AbstractEntity>) beanDesc.getBeanClass());
                }
                return serializer;
            }
        });
        mapper.registerModule(module);






        final ObjectReader reader = mapper.readerFor(Entity.class);
        final Object o = reader.readValue("""
            {
                "id": 123,
                "entityStr": "Nu tipo stroka",
                "nestedId": 1488,
                "nestedIds": [1,2,3]
            }
            """);

        final ObjectWriter writter = mapper.writerFor(Entity.class);
        final String s = writter.writeValueAsString(o);
        System.out.println("qwe");
    }

}
