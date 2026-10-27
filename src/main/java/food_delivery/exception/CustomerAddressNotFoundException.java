package food_delivery.exception;

public class CustomerAddressNotFoundException extends RuntimeException {
    public CustomerAddressNotFoundException(String message) {
        super(message);
    }
}
