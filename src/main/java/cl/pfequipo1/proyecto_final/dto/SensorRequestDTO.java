package cl.pfequipo1.proyecto_final.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SensorRequestDTO {
    private Integer locationId;
    private String sensorName;
    private String sensorCategory;
    private String sensorMeta;
    // No incluimos sensorApiKey ya que será generada automáticamente
}
