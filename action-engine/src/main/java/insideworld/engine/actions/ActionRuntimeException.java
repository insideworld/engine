package insideworld.engine.actions;

public class ActionRuntimeException extends RuntimeException {

    public ActionRuntimeException(final ActionException exp) {
        super(exp);
    }

}
