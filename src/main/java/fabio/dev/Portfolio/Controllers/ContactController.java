package fabio.dev.Portfolio.Controllers;

import fabio.dev.Portfolio.DTOs.ContactResponseDTO;
import fabio.dev.Portfolio.Mapper.contactToResponse;
import fabio.dev.Portfolio.Services.ContactService;
import fabio.dev.Portfolio.DTOs.ContactDTO;
import fabio.dev.Portfolio.DTOs.ContactUpdateDTO;
import fabio.dev.Portfolio.Models.Contact;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public class ContactController implements contactToResponse {

    private final ContactService contactService;

    // metodo para mappear una entidad Contact a ContactResponseDto
    @Override
    public ContactResponseDTO mapToResponse(Contact contact) {
        return new ContactResponseDTO(
                contact.getId(),
                contact.getName(),
                contact.getEmail(),
                contact.getMessage()
        );
    }

    public ContactController(ContactService contactService){
        this.contactService = contactService;
    }

    @PostMapping("/contact")
    public ResponseEntity<?> save(@RequestBody @Valid ContactDTO dto){
        Contact saved = contactService.saveContact(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapToResponse(saved));
    }

    @GetMapping("/admin/dashboard")
    public ResponseEntity<Page<Contact>> findAll(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "2") Integer size,
            @RequestParam(name = "sortBy", defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ){

        return ResponseEntity.ok(contactService.findAllContacts(page, size,sortBy,direction));
    }

    @PatchMapping("/admin/dashboard/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id,@RequestBody @Valid ContactUpdateDTO dto){
        Contact updated = contactService.updateContact(id, dto);
        return ResponseEntity.ok(mapToResponse(updated));
    }

    @DeleteMapping("/admin/dashboard/{id}")
    public ResponseEntity<Void> update(@PathVariable Integer id){
        contactService.deleteContact(id);
        return ResponseEntity.noContent().build();
    }

}