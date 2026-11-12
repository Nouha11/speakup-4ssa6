package tn.esprit.spring.contentservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Map;

/** Erreurs renvoyees en JSON court : statut + message, jamais de trace Java. */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, Object>> statut(ResponseStatusException e) {
        return corps(HttpStatus.valueOf(e.getStatusCode().value()), e.getReason());
    }

    /** 400 : valeur invalide, ex. niveau=Z2 ou id non numerique. */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, Object>> parametre(MethodArgumentTypeMismatchException e) {
        return corps(HttpStatus.BAD_REQUEST, "Valeur invalide pour le parametre " + e.getName());
    }

    private ResponseEntity<Map<String, Object>> corps(HttpStatus statut, String message) {
        return ResponseEntity.status(statut).body(Map.of(
                "timestamp", LocalDateTime.now().toString(),
                "status", statut.value(),
                "error", statut.getReasonPhrase(),
                "message", message == null ? "" : message));
    }
}
