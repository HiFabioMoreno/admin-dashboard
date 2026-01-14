package fabio.dev.Portfolio.Models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Entity
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    private String name;

    @NotNull
    @Column(unique = true)
    private String email;

    @NotNull
    private String message;

    @Column(name = "registrationDate", nullable = false, updatable = false)
    private LocalDateTime registrationDate;

    @PrePersist
    void onCreate() {
        this.registrationDate = LocalDateTime.now();
    }

    public Contact(){};

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

}
