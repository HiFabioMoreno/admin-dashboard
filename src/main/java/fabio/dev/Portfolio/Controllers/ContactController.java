package fabio.dev.Portfolio.Controllers;

import fabio.dev.Portfolio.DTOs.ContactResponseDTO;
import fabio.dev.Portfolio.Mapper.contactToResponse;
import fabio.dev.Portfolio.Services.ContactService;
import fabio.dev.Portfolio.DTOs.ContactDTO;
import fabio.dev.Portfolio.DTOs.ContactUpdateDTO;
import fabio.dev.Portfolio.Models.Contact;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/portfolio")
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

    @GetMapping("/admin/dashboard")
    public ResponseEntity<List<Contact>> findAll(){
        return ResponseEntity.ok(contactService.findAllContacts());
    }

    @PostMapping("/contact")
    public ResponseEntity<?> save(@RequestBody @Valid ContactDTO dto){
        Contact saved = contactService.saveContact(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapToResponse(saved));
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