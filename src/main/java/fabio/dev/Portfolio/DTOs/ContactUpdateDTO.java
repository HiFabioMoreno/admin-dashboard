package fabio.dev.Portfolio.DTOs;

import jakarta.validation.constraints.Size;

public record ContactUpdateDTO(

        @Size(min = 3, max = 100)
        String name,
        String email,
        String message
) {
}
