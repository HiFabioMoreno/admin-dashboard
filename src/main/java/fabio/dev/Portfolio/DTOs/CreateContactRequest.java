package fabio.dev.Portfolio.DTOs;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreateContactRequest(
        @NotBlank
        String name,
        @Email
        String email,
        @NotBlank
        String message
) {
}
