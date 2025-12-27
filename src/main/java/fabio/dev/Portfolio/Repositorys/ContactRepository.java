package fabio.dev.Portfolio.Repositorys;

import fabio.dev.Portfolio.Models.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactRepository extends JpaRepository<Contact, Integer> { }
