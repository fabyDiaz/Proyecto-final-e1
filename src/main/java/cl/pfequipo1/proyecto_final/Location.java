package cl.pfequipo1.proyecto_final;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "location")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer locationId;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @Column(nullable = false)
    private String locationName;

    private String locationCountry;
    private String locationCity;
    private String locationMeta;
}
