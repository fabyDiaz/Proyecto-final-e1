package cl.pfequipo1.proyecto_final.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "sensor_data")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SensorData {

    @Id
    private String id; // Identificador único (varchar)

    @Column(nullable = false)
    private Integer timeStamp; // Marca de tiempo (Epoch)

    @Column(nullable = false)
    private String reading;

    @ManyToOne
    @JoinColumn(name = "sensor_id", nullable = false)
    private Sensor sensor; // Relación con Sensor
}
