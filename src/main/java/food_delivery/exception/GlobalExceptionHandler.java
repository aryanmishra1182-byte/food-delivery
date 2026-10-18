package food_delivery.exception;

import food_delivery.dto.ErrorResponseDTO;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDTO> handleUserAlreadyExists(UserAlreadyExistsException exception, HttpServletRequest request) {
        ErrorResponseDTO error = new ErrorResponseDTO();
        error.setMessage(exception.getMessage());
        error.setPath(request.getRequestURI());
        error.setStatus(409);
        error.setTimestamp(LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleInvalidArguments(MethodArgumentNotValidException exception, HttpServletRequest request) {
        ErrorResponseDTO error = new ErrorResponseDTO();
        error.setTimestamp(LocalDateTime.now());
        error.setStatus(400);
        error.setMessage("Validation failed");
        error.setPath(request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleUserNotFound(UserNotFoundException exception, HttpServletRequest request) {
        ErrorResponseDTO error = new ErrorResponseDTO();
        error.setTimestamp(LocalDateTime.now());
        error.setMessage(exception.getMessage());
        error.setPath(request.getRequestURI());
        error.setStatus(404);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(RestaurantNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleRestaurantNotFound(RestaurantNotFoundException exception, HttpServletRequest request) {
        ErrorResponseDTO error = new ErrorResponseDTO();
        error.setTimestamp(LocalDateTime.now());
        error.setMessage(exception.getMessage());
        error.setPath(request.getRequestURI());
        error.setStatus(404);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(MenuCategoryAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDTO> handleMenuCategoryAlreadyExists(MenuCategoryAlreadyExistsException exception, HttpServletRequest request) {
        ErrorResponseDTO error = new ErrorResponseDTO();
        error.setTimestamp(LocalDateTime.now());
        error.setStatus(409);
        error.setPath(request.getRequestURI());
        error.setMessage(exception.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(MenuItemAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDTO> handleMenuItemAlreadyExists(MenuItemAlreadyExistsException exception, HttpServletRequest request) {
        ErrorResponseDTO error = new ErrorResponseDTO();
        error.setTimestamp(LocalDateTime.now());
        error.setStatus(409);
        error.setPath(request.getRequestURI());
        error.setMessage(exception.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }
    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleCategoryNotFound(CategoryNotFoundException exception, HttpServletRequest request) {
        ErrorResponseDTO error = new ErrorResponseDTO();
        error.setTimestamp(LocalDateTime.now());
        error.setMessage(exception.getMessage());
        error.setPath(request.getRequestURI());
        error.setStatus(404);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
    @ExceptionHandler(MenuItemNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleMenuItemNotFound(MenuItemNotFoundException exception, HttpServletRequest request) {
        ErrorResponseDTO error = new ErrorResponseDTO();
        error.setTimestamp(LocalDateTime.now());
        error.setMessage(exception.getMessage());
        error.setPath(request.getRequestURI());
        error.setStatus(404);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDTO> handleIllegalArgument(
            IllegalArgumentException exception,
            HttpServletRequest request) {

        ErrorResponseDTO error = new ErrorResponseDTO();
        error.setTimestamp(LocalDateTime.now());
        error.setStatus(400);
        error.setMessage(exception.getMessage());
        error.setPath(request.getRequestURI());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponseDTO> handleIllegalState(
            IllegalStateException exception,
            HttpServletRequest request) {

        ErrorResponseDTO error = new ErrorResponseDTO();
        error.setTimestamp(LocalDateTime.now());
        error.setStatus(409);
        error.setMessage(exception.getMessage());
        error.setPath(request.getRequestURI());

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(error);
    }
}