package pdl.backend;

public class BadArguments extends Exception {
    private static final long serialVersionUID = 1L;

    public BadArguments(String message) {
        super(message);
    }
}
