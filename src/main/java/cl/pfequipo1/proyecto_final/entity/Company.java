package cl.pfequipo1.proyecto_final.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "company")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String companyName;

    @Column(nullable = false, unique = true)
    private String companyApiKey;

	public Object getCompanyName() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setCompanyName(Object companyName2) {
		// TODO Auto-generated method stub
		
	}

	public Object getCompanyApiKey() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setCompanyApiKey(Object companyApiKey2) {
		// TODO Auto-generated method stub
		
	}
}

