package insideworld.engine.data.generator.jpa.entity.annotations;

import insideworld.engine.entities.Entity;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Target;
import javax.inject.Scope;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Scope
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Repeatable(GenerateJpaEntities.class)
public @interface GenerateJpaEntity {

    Class<? extends Entity> entity();

    String schema();

    String table();


}
