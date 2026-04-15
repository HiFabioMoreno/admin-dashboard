package fabio.dev.Portfolio.Services;

import fabio.dev.Portfolio.DTOs.ContactFilterRequest;
import fabio.dev.Portfolio.DTOs.ContactResponseDTO;
import fabio.dev.Portfolio.DTOs.ContactUpdateDTO;
import fabio.dev.Portfolio.DTOs.CreateContactRequest;
import fabio.dev.Portfolio.Exceptions.NoEntityException;
import fabio.dev.Portfolio.Models.Contact;
import fabio.dev.Portfolio.Repositorys.ContactRepository;
import fabio.dev.Portfolio.Specifications.SpecificationContact;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class ContactService{

    private Logger logger = LoggerFactory.getLogger(ContactService.class);

    private ContactRepository contactRepository;

    public ContactService(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    @Transactional
    public ContactResponseDTO saveContact(CreateContactRequest createContactRequest) {

        logger.info("saving new contact");

        Contact contact = new Contact();
        contact.setName(createContactRequest.name());
        contact.setEmail(createContactRequest.email());
        contact.setMessage(createContactRequest.message());

        logger.info("saved contact with id {} successfuly", contact.getId());

        contactRepository.save(contact);
        return new ContactResponseDTO(contact.getId(),contact.getName(), contact.getEmail(), contact.getMessage());
    }

    public Page<ContactResponseDTO> findAllContacts(ContactFilterRequest contactFilterRequest) {

        logger.info("all contacts found successfully");

        Sort sort = contactFilterRequest.sortDirection().equalsIgnoreCase("desc") ?
                Sort.by(contactFilterRequest.sortBy()).descending() :
                Sort.by(contactFilterRequest.sortBy()).ascending();

        Pageable pageable = PageRequest.of(contactFilterRequest.page(), contactFilterRequest.size(), sort);

        Specification<ContactResponseDTO> spec = (root, query, cb) -> cb.conjunction();

        if (contactFilterRequest.name() != null  || !contactFilterRequest.name().isEmpty()) {
            spec = spec.and(SpecificationContact.findByName(contactFilterRequest.name()));
        }

        if (contactFilterRequest.email() != null  || !contactFilterRequest.email().isEmpty()) {
            spec = spec.and(SpecificationContact.findByEmail(contactFilterRequest.email()));
        }

        Page<ContactResponseDTO> pagesContactDto = contactRepository.findAll(spec, pageable);

        return pagesContactDto;

    }

    @Transactional
    public ContactResponseDTO updateContact(Integer id, ContactUpdateDTO dto){

        logger.info("updating contact with id {}", id);

        if( id == null || id <= 0){
            throw new NoEntityException("Contact", id);
        }

        Contact contact = contactRepository.findById(id).orElseThrow(() ->  new NoEntityException("Contact",id));

        if (dto.name() != null){contact.setName(dto.name());}
        if (dto.email() != null){contact.setEmail(dto.email());}
        if (dto.message() != null){contact.setMessage(dto.message());}

        Contact updatedContact = contactRepository.save(contact);

        logger.info("updated contact with id {} successfully", id);

        return new ContactResponseDTO(id, updatedContact.getName(), updatedContact.getEmail(), updatedContact.getMessage());
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