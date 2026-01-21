package fabio.dev.Portfolio.Models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorRes {
    private int status;
    private String message;
    private LocalDateTime timestamp;
    private List<String> errors;

    public ErrorRes(int status, String message, LocalDateTime timestamp) {
        this.status = status;
        this.message = message;
        this.timestamp = timestamp;
        this.errors = new ArrayList<>();
    }
}
