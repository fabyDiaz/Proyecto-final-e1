package cl.pfequipo1.proyecto_final.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "admin")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Admin {
    @Id
    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String role;

    @Column(nullable = false, unique = true)
    private String password;
}
