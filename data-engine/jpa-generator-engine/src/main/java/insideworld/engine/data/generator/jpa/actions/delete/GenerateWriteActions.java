package insideworld.engine.data.generator.jpa.actions.delete;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import javax.inject.Scope;

@Scope
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface GenerateWriteActions {

    GenerateWriteAction[] value();

}
