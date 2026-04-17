package fabio.dev.Portfolio.Repositorys;

import fabio.dev.Portfolio.DTOs.ContactResponseDTO;
import fabio.dev.Portfolio.Models.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ContactRepository extends JpaRepository<Contact, Integer>, JpaSpecificationExecutor<Contact> { }
