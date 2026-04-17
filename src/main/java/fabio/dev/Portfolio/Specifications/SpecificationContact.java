package fabio.dev.Portfolio.Specifications;

import fabio.dev.Portfolio.DTOs.ContactResponseDTO;
import fabio.dev.Portfolio.Models.Contact;
import org.springframework.data.jpa.domain.Specification;

public class SpecificationContact {

    public static Specification<Contact> findByName(String name){
        return (root, query, cb)
                -> cb.like(root.get("name"), name);
    }

    public static Specification<Contact> findByEmail(String email){
        return ( root, query, cb) ->
                cb.like(root.get("email"), email);
    }

}

