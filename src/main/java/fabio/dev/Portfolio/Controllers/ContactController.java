package fabio.dev.Portfolio.Controllers;

import fabio.dev.Portfolio.DTOs.*;
import fabio.dev.Portfolio.Services.ContactService;
import fabio.dev.Portfolio.Models.Contact;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ContactController{
    private final ContactService contactService;
    public ContactController(ContactService contactService){
        this.contactService = contactService;
    }

    @PostMapping("/contact")
    public ResponseEntity<ContactResponseDTO> save(@RequestBody @Valid CreateContactRequest contactRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(contactService.saveContact(contactRequest));
    }

    @GetMapping("/admin/dashboard")
    public ResponseEntity<Page<ContactResponseDTO>> findAll(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "2") Integer size,
            @RequestParam(name = "sortBy", defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ){

        return ResponseEntity.ok(contactService.findAllContacts(page, size,sortBy,direction));
    }

    @PatchMapping("/admin/dashboard/{id}")
    public ResponseEntity<ContactResponseDTO> update(@PathVariable Integer id, @RequestBody @Valid ContactUpdateDTO dto){
        return ResponseEntity.ok(contactService.updateContact(id, dto));
    }

    @DeleteMapping("/admin/dashboard/{id}")
    public ResponseEntity<Void> update(@PathVariable Integer id){
        contactService.deleteContact(id);
        return ResponseEntity.noContent().build();
    }

}