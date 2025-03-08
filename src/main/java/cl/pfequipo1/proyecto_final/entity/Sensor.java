package cl.pfequipo1.proyecto_final.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "sensor")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Sensor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer sensorId;

    @ManyToOne
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    @Column(nullable = false)
    private String sensorName;

    @Column(nullable = false)
    private String sensorCategory;

    private String sensorMeta;

    @Column(nullable = false, unique = true)
    private String sensorApiKey;
}
