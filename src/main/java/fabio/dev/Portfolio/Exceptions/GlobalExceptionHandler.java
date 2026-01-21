package fabio.dev.Portfolio.Exceptions;

import fabio.dev.Portfolio.Models.ErrorRes;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorRes> handleValidationError(MethodArgumentNotValidException ex){
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + " : " + error.getDefaultMessage())
                .collect(Collectors.toList());

        ErrorRes errorResponse = new ErrorRes(
                HttpStatus.BAD_REQUEST.value(),
                "Validation error",
                LocalDateTime.now(),
                errors
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorRes> handleEntityNotFound(
            EntityNotFoundException ex) {

        ErrorRes errorResponse = new ErrorRes(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorRes> handleTypeMismatch(
            MethodArgumentTypeMismatchException ex) {

        String error = String.format("Parameter '%s' must be of type %s",
                ex.getName(),
                ex.getRequiredType().getSimpleName());

        ErrorRes errorResponse = new ErrorRes(
                HttpStatus.BAD_REQUEST.value(),
                error,
                LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorRes> handleGeneralException(Exception ex) {

        ErrorRes errorResponse = new ErrorRes(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "An unexpected error occurred",
                LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorResponse);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorRes> handleConstraintViolation(
            ConstraintViolationException ex) {

        List<String> errors = ex.getConstraintViolations()
                .stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .collect(Collectors.toList());

        ErrorRes errorResponse = new ErrorRes(
                HttpStatus.BAD_REQUEST.value(),
                "Constraint violation",
                LocalDateTime.now(),
                errors
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }


    @ExceptionHandler(NoEntityException.class)
    public ResponseEntity<String> handleEntidadNoExistenteException(NoEntityException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

}