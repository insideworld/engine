package insideworld.engine.data.generator.jpa.storage.annotations;

import insideworld.engine.entities.Entity;
import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import javax.inject.Scope;

@Scope
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(GenerateCruds.class)
public @interface GenerateCrud {

    Class<? extends Entity> entity();

}
