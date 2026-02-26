package fabio.dev.Portfolio.Services;

import fabio.dev.Portfolio.DTOs.ContactDTO;
import fabio.dev.Portfolio.DTOs.ContactUpdateDTO;
import fabio.dev.Portfolio.Exceptions.NoEntityException;
import fabio.dev.Portfolio.Models.Contact;
import fabio.dev.Portfolio.Repositorys.ContactRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class ContactService{

    private Logger logger = LoggerFactory.getLogger(ContactService.class);

    private ContactRepository contactRepository;

    public ContactService(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    @Transactional
    public Contact saveContact(ContactDTO dto){

        logger.info("saving new contact");

        Contact contact = new Contact();
        contact.setName(dto.getName());
        contact.setEmail(dto.getEmail());
        contact.setMessage(dto.getMessage());

        logger.info("saved contact with id {} successfuly", contact.getId());
        return contactRepository.save(contact);
    }

    public Page<Contact> findAllContacts(
            Integer page,
            Integer size,
            String sortBy,
            String sortDirection){

        logger.info("founded all contacts successfully");

        Sort sort = sortDirection.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() :
                Sort.by(sortBy).ascending();

        return contactRepository.findAll(PageRequest.of(page, size, sort));
    }

    @Transactional
    public Contact updateContact(Integer id, ContactUpdateDTO dto){

        logger.info("updating contact with id {}", id);

        if( id == null || id <= 0){
            throw new NoEntityException("Contact", id);
        }

        Contact contact = contactRepository.findById(id).orElseThrow(() ->  new NoEntityException("Contact",id));

        if (dto.name() != null){contact.setName(dto.name());}
        if (dto.email() != null){contact.setEmail(dto.email());}
        if (dto.message() != null){contact.setMessage(dto.message());}

        logger.info("updated contact with id {} successfully", id);
        return contactRepository.save(contact);
    }

    @Transactional
    public void deleteContact(Integer id){
        logger.info("deleting contact with id {}", id);

        if( id == null || id <= 0){
            throw new NoEntityException("Contact",id);
        }

        if(!contactRepository.existsById(id)){
            throw new NoEntityException("Contact",id);
        }
        logger.info("deleted contact with id {} successfully", id);
        contactRepository.deleteById(id);
    }

}