package co.xarx.trix.services.person;

public class PersonNotFoundException extends RuntimeException {
    private static final String MESSAGE = "Person not found";

    public PersonNotFoundException() {
        super(MESSAGE);
    }

    public PersonNotFoundException(String s) {
        super(MESSAGE + ": " + s);
    }

    public PersonNotFoundException(Throwable t) {
        super(MESSAGE, t);
    }
}
