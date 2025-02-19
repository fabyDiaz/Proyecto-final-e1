package cl.pfequipo1.proyecto_final;

import jakarta.persistence.*;

import lombok.*;

@Table(name = "admin")
@Data // Genera getters, setters, toString, equals y hashCode
@NoArgsConstructor // Constructor sin parámetros
@AllArgsConstructor // Constructor con todos los parámetros
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;
}