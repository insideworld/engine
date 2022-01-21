package insideworld.engine.data.generator.jpa.actions.delete;

import insideworld.engine.entities.Entity;
import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import javax.inject.Scope;

@Scope
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(GenerateWriteActions.class)
public @interface GenerateWriteAction {

    Class<? extends Entity> entity();

    String schema();

    String table();


}
