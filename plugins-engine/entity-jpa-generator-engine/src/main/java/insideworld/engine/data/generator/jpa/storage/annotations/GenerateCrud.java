package insideworld.engine.data.generator.jpa.storage.annotations;

import insideworld.engine.entities.Entity;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.inject.Scope;

@Scope
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Repeatable(GenerateCruds.class)
public @interface GenerateCrud {

    Class<? extends Entity> entity();

    boolean override() default false;
}
