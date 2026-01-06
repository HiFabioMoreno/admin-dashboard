package fabio.dev.Portfolio;

import fabio.dev.Portfolio.DTOs.ContactDTO;
import fabio.dev.Portfolio.DTOs.ContactResponseDTO;
import fabio.dev.Portfolio.DTOs.ContactUpdateDTO;
import fabio.dev.Portfolio.Exceptions.NotEntity;
import fabio.dev.Portfolio.Models.Contact;
import fabio.dev.Portfolio.Repositorys.ContactRepository;
import fabio.dev.Portfolio.Services.ContactService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ContactServiceTests {

    @Mock
    private ContactRepository contactRepository;

    @InjectMocks
    private ContactService contactService;

    private Contact contact;
    private ContactDTO contactDTO;
    private ContactUpdateDTO contactUpdateDTO;
    private ContactResponseDTO contactResponseDTO;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);

        this.contactDTO = new ContactDTO();
        this.contactDTO.setEmail("ejemplo@gmail.com");
        this.contactDTO.setName("Fabio Moreno");
        this.contactDTO.setMessage("Hey, let's keep in touch");

        this.contact = new Contact();
        this.contact.setEmail(contactDTO.getEmail());
        this.contact.setName(contactDTO.getName());
        this.contact.setMessage(contactDTO.getMessage());

        this.contactResponseDTO = new ContactResponseDTO(1,"Fabio Moreno","ejemplo@gmail.com","Hey, let's keep in touch");
    }

    @Test
    @DisplayName("Should save contact successfully")
    public void saveContactSuccessfully(){

        when(contactRepository.save(any(Contact.class))).thenAnswer(
                invocation -> invocation.getArguments()[0]
        );

        Contact contact1 = contactService.saveContact(this.contactDTO);

        assertEquals("ejemplo@gmail.com", contact1.getEmail());
        assertEquals("Fabio Moreno", contact1.getName());
        assertEquals("Hey, let's keep in touch", contact1.getMessage());

        verify(contactRepository, times(1)).save(any(Contact.class));
    }

    @Test
    @DisplayName("Should show all contacts successfully")
    public void findAllContactsTest(){

        List<Contact> contacts = Arrays.asList(this.contact);
        when(contactRepository.findAll()).thenReturn(contacts);
        List<Contact> results = contactService.findAllContacts();
        assertEquals(1, results.size());
        assertEquals("Fabio Moreno", results.get(0).getName());

        verify(contactRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should update contact successfully")
    public void updateContact(){
        when(contactRepository.findById(1)).thenReturn(Optional.of(contact));
        when(contactRepository.save(any(Contact.class))).thenAnswer(i -> i.getArgument(0));

        contactService.updateContact(1,new ContactUpdateDTO("Raul Flores", null,null));

        assertEquals("Raul Flores", contact.getName());
        assertEquals("Hey, let's keep in touch", contact.getMessage());

        verify(contactRepository, times(1)).findById(1);
        verify(contactRepository, times(1)).save(contact);

    }

    @Test
    @DisplayName("Should fail update contact ")
    public void updateContactIdEqualsZero(){
        assertThrows(NotEntity.class,
                () -> contactService.updateContact(0,new ContactUpdateDTO("Raul Flores", null,null)
                ));
        verify(contactRepository,never()).findById(any());

    }

    @Test
    public void updateContactIdNegative(){
        when(contactRepository.findById(-10)).thenReturn(Optional.empty());
        assertThrows(NotEntity.class,
                () ->  contactService.updateContact(-10,new ContactUpdateDTO("Raul Flores", null,null))
        );
        verify(contactRepository, never()).findById(-10);
    }

    @Test
    public void updateContactIdNull(){
        assertThrows(NotEntity.class,
                () ->  contactService.updateContact(null,new ContactUpdateDTO("Raul Flores", null,null))
        );
        verify(contactRepository, never()).findById(any());
    }

    @Test
    public void deleteContactSuccessfully(){
        when(contactRepository.existsById(1)).thenReturn(true);

        contactService.deleteContact(1);

        verify(contactRepository, times(1)).existsById(1);
        verify(contactRepository, times(1)).deleteById(1);

    }

    @Test
    public void deleteContactIdNegativo(){
        assertThrows(NotEntity.class,
                ()-> contactService.deleteContact(-10)
        );
        verify(contactRepository,never()).existsById(-10);
        verify(contactRepository, never()).deleteById(-10);
    }

    @Test
    public void deleteContactIdNull(){
        assertThrows(NotEntity.class,
                ()-> contactService.deleteContact(null)
        );

        verify(contactRepository,never()).existsById(null);
        verify(contactRepository, never()).deleteById(null);
    }

    @Test
    public void deleteContactIdZero(){
        assertThrows(NotEntity.class,
                ()-> contactService.deleteContact(0)
        );

        verify(contactRepository,never()).existsById(0);
        verify(contactRepository, never()).deleteById(0);
    }

}
