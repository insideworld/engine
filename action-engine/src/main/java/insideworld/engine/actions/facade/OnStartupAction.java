package insideworld.engine.actions.facade;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.inject.Scope;

/**
 * Execute action after init of action engine.
 * @since 0.6.0
 */
@Scope
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface OnStartupAction {
}
