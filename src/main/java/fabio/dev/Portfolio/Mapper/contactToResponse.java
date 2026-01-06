package fabio.dev.Portfolio.Mapper;

import fabio.dev.Portfolio.DTOs.ContactResponseDTO;
import fabio.dev.Portfolio.Models.Contact;

public interface contactToResponse {
    ContactResponseDTO mapToResponse(Contact contact);
}
