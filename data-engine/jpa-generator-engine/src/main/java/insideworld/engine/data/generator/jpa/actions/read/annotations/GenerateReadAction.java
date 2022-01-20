package insideworld.engine.data.generator.jpa.actions.read.annotations;

import insideworld.engine.actions.keeper.tags.Tag;
import insideworld.engine.entities.Entity;
import insideworld.engine.entities.tags.EntityTag;
import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.math.BigInteger;
import javax.inject.Scope;

@Scope
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(GenerateReadActions.class)
public @interface GenerateReadAction{

    EntityTag<?> qwe();



}
