package springgradle.bankingproject.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class ApiException {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    Map<?, ?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e){

        List<FieldError> fieldErrors = new ArrayList<>();
        e.getFieldErrors().
                forEach(fieldError -> fieldErrors // way 1
                        .add(FieldError.builder()
                                .field(fieldError.getField())
                                .detail(fieldError.getDefaultMessage())
                                .build()));

        ErrorResponse<?> errorResponse = ErrorResponse.builder()
                .code(e.getStatusCode().value())
                .reason(fieldErrors)
                .build();
        return Map.of("error",errorResponse);


//                forEach(fieldError -> { //way 2
//                    fieldErrors.add(new FieldError(fieldError.getField(), fieldError.getDefaultMessage()));
//                });
//
//        return Map.of("error", ErrorResponse.builder()
//                .code(HttpStatus.BAD_REQUEST.value())
//                        .reason(fieldErrors)
//                .build());

    }

    @ExceptionHandler(ResponseStatusException.class)// mark for handle exception
    ResponseEntity<?> handleResponseStatusException(ResponseStatusException e) {
        System.out.println("Bro get code ot mel tes"+ e.getMessage());
        ErrorResponse<String> errorResponse = ErrorResponse.<String>builder()
                .code(e.getStatusCode().value())
                .reason(e.getReason())
                .build();
        return ResponseEntity
                .status(e.getStatusCode())
//                .body(errorResponse);
                .body(Map.of("error", errorResponse));
    }
}
