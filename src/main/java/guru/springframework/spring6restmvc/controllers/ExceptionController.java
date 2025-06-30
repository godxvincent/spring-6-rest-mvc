package guru.springframework.spring6restmvc.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

// I am commenting this decorator so it's not consider the class to handle the exceptions anymore
// @ControllerAdvice
public class ExceptionController {
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity handleNotFoundException(){
        return new ResponseEntity(HttpStatus.NOT_FOUND).notFound().build();
    }
}
