//package com.netcracker.msdcl.whatdidyoudo.engine.database.schema;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.google.common.collect.Maps;
//import com.netcracker.msdcl.whatdidyoudo.engine.entities.Schema;
//import java.io.File;
//import java.io.IOException;
//import java.util.Map;
//import org.springframework.beans.factory.annotation.Value;
//
//
//@Deprecated
//public class FileSchemaStorage implements com.netcracker.msdcl.whatdidyoudo.engine.storages.schema.SchemaStorage {
//
//    private final Map<String, Schema> schemas;
//
//    public FileSchemaStorage(@Value("${extractor.path}") String path) throws IOException {
//        final File[] files = new File(path).listFiles();
//        this.schemas = Maps.newHashMapWithExpectedSize(files.length);
//        final ObjectMapper mapper = new ObjectMapper();
//        for (final File file : files) {
//            final Schema schema = mapper.readValue(file, Schema.class);
//            this.schemas.put(schema.getName(), schema);
//        }
//    }
//
//    @Override
//    public final Schema schema(final String name) {
//        return this.schemas.get(name);
//    }
//}
