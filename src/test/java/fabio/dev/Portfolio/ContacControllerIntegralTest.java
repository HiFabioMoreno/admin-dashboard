package fabio.dev.Portfolio;

import fabio.dev.Portfolio.DTOs.ContactDTO;
import fabio.dev.Portfolio.DTOs.ContactUpdateDTO;
import fabio.dev.Portfolio.Repositorys.ContactRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import tools.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@ActiveProfiles("test")

public class ContacControllerIntegralTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ContactRepository contactRepository;

    @BeforeEach
    public void setUp() {
        contactRepository.deleteAll();
    }

    @Test
    @DisplayName("Integration: Should create, retrieve, update and delete contact")
    void fullCrudFlow_ShouldWorkCorrectly() throws Exception {

        ContactDTO contactDTO = new ContactDTO();
        contactDTO.setName("Fabio");
        contactDTO.setEmail("fabiomore198@gmail.com");
        contactDTO.setMessage("Hey, let's keep in touch");

        String createResponse = mockMvc.perform(post("/contact")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(contactDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Fabio"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Integer contactId = objectMapper.readTree(createResponse).get("id").asInt();

        mockMvc.perform(get("/admin/dashboard").param("page", "0").param("size", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Fabio"));

        ContactUpdateDTO updateDto = new ContactUpdateDTO("Raul",null,null);

        mockMvc.perform(patch("/admin/dashboard/{id}", contactId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Raul"));

        mockMvc.perform(delete("/admin/dashboard/{id}", contactId))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/admin/dashboard").param("page", "1").param("size", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content").isEmpty());

    }
}
