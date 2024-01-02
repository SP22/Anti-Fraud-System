package antifraud.exceptions;

public class UnsupportedRoleException extends RuntimeException {
    public UnsupportedRoleException(String role) {
        super(role + " is not supported");
    }
}
