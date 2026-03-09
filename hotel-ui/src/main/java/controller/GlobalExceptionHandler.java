package controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import Exception.HotelException;
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(HotelException.class)
    public ResponseEntity<String> HandlerHotelExeption(HotelException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .header("Content-Type", "text/plain; charset=UTF-8")
                .body("Ошибка операции: " + ex.getMessage());
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .header("Content-Type", "text/plain; charset=UTF-8")
                .body(" Ошибка ввода: переданы некорректные данные. Проверьте параметры запроса.");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .header("Content-Type", "text/plain; charset=UTF-8")
                .body(" Внутренняя ошибка сервера: " + ex.getMessage());
    }

}
