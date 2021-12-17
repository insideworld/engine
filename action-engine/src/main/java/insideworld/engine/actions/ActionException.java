package insideworld.engine.actions;

import insideworld.engine.actions.keeper.context.Context;

public class ActionException extends Exception {

    private static final String EXCEPTION_MESSAGE = "Exception during execute action %s";

    public ActionException(final String message) {
        super(message);
    }

    public ActionException(final String message, final Exception exp) {
        super(message, exp);
    }

    public ActionException(final Context input, final Exception exp) {
        super(String.format(EXCEPTION_MESSAGE, input.get(ActionsTags.ACTION)), exp);
    }

}
