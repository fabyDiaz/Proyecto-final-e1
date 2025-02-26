package cl.pfequipo1.proyecto_final.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "admin")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Admin {
    @Column(nullable = false, unique = true)
    private String userName;

    @Column(nullable = false, unique = true)
    private String role;

    @Column(nullable = false, unique = true)
    private String password;
}
