package fabio.dev.Portfolio.DTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ContactUpdateDTO(

        @NotBlank
        @Size(min = 3, max = 100)
        String name,
        @NotBlank
        String email,
        @NotBlank
        String message
) {
}
