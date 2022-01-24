package insideworld.engine.data.generator.jpa.actions.delete.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.inject.Scope;

@Scope
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface GenerateDeleteActions {

    GenerateDeleteAction[] value();

}
