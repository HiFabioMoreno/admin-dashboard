package fabio.dev.Portfolio;

import fabio.dev.Portfolio.DTOs.ContactDTO;
import fabio.dev.Portfolio.DTOs.ContactUpdateDTO;
import fabio.dev.Portfolio.Models.Contact;
import fabio.dev.Portfolio.Repositorys.ContactRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactService {

    private ContactRepository contactRepository;

    public ContactService(ContactRepository contactRepository){
        this.contactRepository = contactRepository;
    }

    @Transactional
    public Contact saveContact(ContactDTO dto){
        Contact contact = new Contact();
        contact.setName(dto.getName());
        contact.setEmail(dto.getEmail());
        contact.setMessage(dto.getMessage());

        return contactRepository.save(contact);
    }

    public List<Contact> findAllContacts(){
        return contactRepository.findAll();
    }

    @Transactional
    public Contact updateContact(Integer id,ContactUpdateDTO dto){

        Contact contact =  contactRepository.findById(id).orElseThrow(() -> new RuntimeException(""));

        if (dto.name() != null){contact.setName(dto.name());}
        if (dto.email() != null){contact.setEmail(dto.email());}
        if (dto.message() != null){contact.setMessage(dto.message());}

        return contactRepository.save(contact);
    }

    @Transactional
    public void deleteContact(Integer id){
        if(!contactRepository.existsById(id)){
            throw new Error("");
        }
        contactRepository.deleteById(id);
    }

}
