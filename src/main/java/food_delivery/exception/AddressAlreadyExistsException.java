package food_delivery.exception;

public class AddressAlreadyExistsException extends RuntimeException{
    public AddressAlreadyExistsException(String message) {
        super(message);
    }
}
