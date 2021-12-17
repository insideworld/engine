package insideworld.engine.properties;


public class PropertiesException extends Exception {

    private static final String EXCEPTION_MESSAGE = "Exception during execute action %s";

    public PropertiesException(final String message) {
        super(message);
    }

    public PropertiesException(final String message, final Exception exp) {
        super(message, exp);
    }



}
