package springgradle.bankingproject.exception;

import lombok.*;

@NoArgsConstructor
@Getter
@Setter
@Builder
@AllArgsConstructor
public class ErrorResponse <T>{
    private int code;
    private T reason;
}
