package cl.pfequipo1.proyecto_final.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "admin")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Admin {
    @Id
    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String role;

    @Column(nullable = false, unique = true)
    private String password;

	public Object getPassword() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setPassword(Object password2) {
		// TODO Auto-generated method stub
		
	}
}
