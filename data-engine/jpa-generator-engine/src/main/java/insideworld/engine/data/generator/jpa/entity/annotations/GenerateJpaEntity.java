package insideworld.engine.data.generator.jpa.entity.annotations;

import insideworld.engine.entities.Entity;

import java.lang.annotation.Repeatable;
import javax.inject.Scope;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Scope
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(GenerateJpaEntities.class)
public @interface GenerateJpaEntity {

    Class<? extends Entity> entity();

    String schema();

    String table();


}
