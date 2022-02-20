package insideworld.engine.data.generator.inmemory.storage.annotations;

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
@Repeatable(GenerateInMemoryCruds.class)
public @interface GenerateInMemoryCrud {

    Class<? extends Entity> entity();

    boolean override() default false;
}
