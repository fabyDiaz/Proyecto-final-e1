package cl.pfequipo1.proyecto_final.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Modelo de Data de sensor")
public class SensorDTO {

    @Schema(description = "Id de Sensor")
    private Integer sensorId;

    @Schema(description = "Id de Locacion de Sensor")
    private Integer locationId;

    @Schema(description = "Nombre de Sensor")
    private String sensorName;

    @Schema(description = "Categoria de Sensor")
    private String sensorCategory;

    @Schema(description = "Meta de Sensor")
    private String sensorMeta;
    // No incluimos sensorApiKey por seguridad
}
