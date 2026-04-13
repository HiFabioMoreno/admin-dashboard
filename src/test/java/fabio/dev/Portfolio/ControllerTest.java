package fabio.dev.Portfolio;

import fabio.dev.Portfolio.Controllers.ContactController;
import fabio.dev.Portfolio.DTOs.ContactDTO;
import fabio.dev.Portfolio.DTOs.ContactResponseDTO;
import fabio.dev.Portfolio.DTOs.ContactUpdateDTO;
import fabio.dev.Portfolio.DTOs.CreateContactRequest;
import fabio.dev.Portfolio.Exceptions.NoEntityException;
import fabio.dev.Portfolio.Models.Contact;
import fabio.dev.Portfolio.Services.ContactService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;


import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ContactController.class)
public class ControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ContactService contactService;

    @Autowired
    private ObjectMapper objectMapper;

    private Contact contact;
    private ContactDTO contactDTO;
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
    void shouldReturnPagedContacts() throws Exception {

        Page<ContactResponseDTO> page = new PageImpl<>(List.of(new ContactResponseDTO(), new ContactResponseDTO()));

        when(contactService.findAllContacts(eq(0), eq(2), anyString(), anyString())).thenReturn(page);

        mockMvc.perform(
                        get("/admin/dashboard")
                                .param("page", "0")
                                .param("size", "2")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2));

    }

    @Test
    @DisplayName("Should return 400 when validation fails")
    void save_WithInvalidData_ShouldReturnBadRequest() throws Exception {

        ContactDTO invalidDto = new ContactDTO();

        mockMvc.perform(post("/contact")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation error"))
                .andDo(print());

        verify(contactService, never()).saveContact(any(CreateContactRequest.class));
    }

    @Test
    @DisplayName("Should update contact successfully")
    void update_WithValidData_ShouldUpdateContact() throws Exception {

        Integer contactId = 1;

        ContactResponseDTO responseDTO = new ContactResponseDTO(1, "Fabio Moreno", "ejemplo@gmail.com","Hey, let's keep in touch");

        when(contactService.updateContact(eq(contactId),any(ContactUpdateDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(patch("/admin/dashboard/{id}",contactId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(responseDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Fabio Moreno"))
                .andExpect(jsonPath("$.email").value("ejemplo@gmail.com"))
                .andDo(print());

        verify(contactService, times(1)).updateContact(eq(contactId),any(ContactUpdateDTO.class));
    }

    @Test
    @DisplayName("Should return 404 when updating non-existent contact")
    void update_WithNonExistentId_ShouldReturnNotFound() throws Exception {

        Integer contactId = 999;
        ContactUpdateDTO dto = new ContactUpdateDTO(null,null,null);

        when(contactService.updateContact(eq(contactId), any(ContactUpdateDTO.class)))
                .thenThrow(new NoEntityException("Contact", contactId));

        mockMvc.perform(patch("/admin/dashboard/{id}", contactId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @DisplayName("Should delete contact successfully")
    void delete_WithExistingId_ShouldDeleteContact() throws Exception {

        Integer contactId = 1;
        doNothing().when(contactService).deleteContact(contactId);

        mockMvc.perform(delete("/admin/dashboard/{id}", contactId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print());

        verify(contactService, times(1)).deleteContact(contactId);
    }

    @Test
    @DisplayName("Should return 404 when deleting non-existent contact")
    void delete_WithNonExistentId_ShouldReturnNotFound() throws Exception {

        Integer contactId = 999;
        doThrow(new NoEntityException("Contact", contactId))
                .when(contactService).deleteContact(contactId);


        mockMvc.perform(delete("/admin/dashboard/{id}", contactId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @DisplayName("Should return 400 when ID is invalid format")
    void update_WithInvalidIdFormat_ShouldReturnBadRequest() throws Exception {

        mockMvc.perform(patch("/admin/dashboard/{id}", "invalid-id")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"read\": true}"))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

}