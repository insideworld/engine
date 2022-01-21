package insideworld.engine.data.generator.jpa.actions.read.annotations;

import insideworld.engine.actions.keeper.tags.Tag;
import insideworld.engine.entities.Entity;
import insideworld.engine.entities.tags.EntityTag;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.math.BigInteger;
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

}
