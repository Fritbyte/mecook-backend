package com.mecook.mecookbackend.dish.exception;

import com.mecook.mecookbackend.dish.controller.DishController;
import com.mecook.mecookbackend.user.controller.UserController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(basePackageClasses = {DishController.class, UserController.class})
public class DishExceptionHandler {
    @ExceptionHandler(DishNotFoundException.class)
    public ResponseEntity<String> handleException(DishNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(DishAlreadyExistsException.class)
    public ResponseEntity<String> handleException(DishAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An error has occurred: " + ex.getMessage());
    }
}
