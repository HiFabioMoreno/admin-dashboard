package fabio.dev.Portfolio.Models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
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

    public Contact(){}

}
