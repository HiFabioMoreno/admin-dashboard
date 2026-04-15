package fabio.dev.Portfolio.DTOs;

public record ContactFilterRequest(
        Integer page,
        Integer size,
        String sortBy,
        String sortDirection,
        String name,
        String email
) {
}
