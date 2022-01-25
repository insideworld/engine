package insideworld.engine.generator.actions.entities.read.annotations;

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
@Repeatable(GenerateReadActions.class)
public @interface GenerateReadAction{

//    Class<? extends ReadActionInfos> infos();
    Class<? extends Entity> entity();

    String tag();

    String tags();

    String key();

    Class<?>[] interfaces() default {};

}
