package food_delivery.exception;

public class MenuItemAlreadyExistsException extends RuntimeException {
    public MenuItemAlreadyExistsException(String message) {
        super(message);
    }
}
