package insideworld.engine.data.generator.jpa.actions.write.annotations;

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
@Repeatable(GenerateWriteActions.class)
public @interface GenerateWriteAction {

    Class<? extends Entity> entity();

    String tag();

    String key();

    Class<?>[] interfaces() default {};

}
