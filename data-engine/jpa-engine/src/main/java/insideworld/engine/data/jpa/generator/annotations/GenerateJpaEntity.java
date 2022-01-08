package insideworld.engine.data.jpa.generator.annotations;

import insideworld.engine.entities.Entity;

import javax.inject.Scope;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Scope
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface GenerateJpaEntity {

    Class<? extends Entity> entity();

    String schema();

    String table();


}
