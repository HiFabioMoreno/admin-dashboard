package fabio.dev.Portfolio.Controllers;

import fabio.dev.Portfolio.ContactService;
import fabio.dev.Portfolio.DTOs.ContactDTO;
import fabio.dev.Portfolio.DTOs.ContactUpdateDTO;
import fabio.dev.Portfolio.Models.Contact;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/portfolio")
public class ContactController {

    private ContactService contactService;

    public ContactController(ContactService contactService){
        this.contactService = contactService;
    }

    @GetMapping
    public ResponseEntity<List<Contact>> findAll(){
        return ResponseEntity.status(HttpStatus.OK).body(contactService.findAllContacts());
    }

    @PostMapping("/contact")
    public ResponseEntity<Contact> save(@RequestBody @Valid ContactDTO dto, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return ResponseEntity.badRequest().body(new Contact());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(contactService.saveContact(dto));
    }

    @PatchMapping("/contact/{id}")
    public ResponseEntity<Contact> update(@PathVariable Integer id, @Valid ContactUpdateDTO dto, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return ResponseEntity.badRequest().body(new Contact());
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(contactService.updateContact(id,dto));
    }

    @PatchMapping("/contact/{id}")
    public ResponseEntity<String> update(@PathVariable Integer id, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return ResponseEntity.badRequest().body("Invalid ID");
        }
        contactService.deleteContact(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Contact deleted succesfully");
    }
}
