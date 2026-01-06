package fabio.dev.Portfolio.DTOs;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;

public class ContactResponseDTO {

    private Integer id;

    @NotNull
    private String name;

    @NotNull
    @Column(unique = true)
    private String email;

    @NotNull
    private String message;

    public ContactResponseDTO() {}

    public ContactResponseDTO(Integer id, String name, String email,String message) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getId() {return id;}

    public void setId(Integer id) {}
}
