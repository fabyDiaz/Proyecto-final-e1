package cl.pfequipo1.proyecto_final.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SensorDTO {
    private Integer sensorId;
    private Integer locationId;
    private String sensorName;
    private String sensorCategory;
    private String sensorMeta;
    // No incluimos sensorApiKey por seguridad
}
