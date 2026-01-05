package guru.springframework.spring6restmvc.controllers;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.validation.ConstraintViolationException;

// Remember controlleradvice is a decorator that intercept the output request and allow us to custome the responses based on some errors.
@ControllerAdvice
public class CustomErrorController {

    @ExceptionHandler
    ResponseEntity handleJPAViolations(TransactionSystemException exception) {

        ResponseEntity.BodyBuilder responseEntity = ResponseEntity.badRequest();

        if (exception.getCause().getCause() instanceof ConstraintViolationException) {
            ConstraintViolationException constraintViolationException = (ConstraintViolationException) exception.getCause().getCause();
            List listOfErrors = constraintViolationException.getConstraintViolations().stream().map((violation) -> {
                Map<String, String> errorsMap = new HashMap<>();
                errorsMap.put(violation.getPropertyPath().toString(), violation.getMessage());
                return errorsMap;
            }).toList();
            return responseEntity.body(listOfErrors);
        }
        return ResponseEntity.badRequest().body(exception.getRootCause().getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity handleBindErrors(MethodArgumentNotValidException exception){

        List errors = exception.getBindingResult().getFieldErrors().stream().map((fieldError) -> {
            Map<String, String> errorsMap = new HashMap<>();
            errorsMap.put(fieldError.getField(), fieldError.getDefaultMessage());
            return errorsMap;
        }).toList();      

        return ResponseEntity.badRequest().body(errors);
    }

}
