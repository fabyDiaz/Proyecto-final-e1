package cl.pfequipo1.proyecto_final.entity;

import jakarta.persistence.*;
import lombok.*;

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

    private Float temperature; // Temperatura (float)

    private Float voltage; // Voltaje (float)

    @ManyToOne
    @JoinColumn(name = "sensor_id", nullable = false)
    private Sensor sensor; // Relación con Sensor
}
