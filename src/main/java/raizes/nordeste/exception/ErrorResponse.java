package raizes.nordeste.exception;

import java.time.LocalDateTime;
import java.util.List;

public record ErrorResponse (
    LocalDateTime timestamp,
    int status,
    String error,
    String message,
    String path,
    List<FieldValidationError> fieldErrors
) {}
