package food_delivery.exception;

public class MenuCategoryAlreadyExistsException extends RuntimeException {
    public MenuCategoryAlreadyExistsException(String message) {
        super(message);
    }
}
