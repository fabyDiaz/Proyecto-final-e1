package cl.pfequipo1.proyecto_final.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Modelo de Data de Sensor")
public class SensorDataDTO {

    @Schema(description = "Id de Data")
    private String id;

    @Schema(description = "Marca de tiempo de Data")
    private Integer timeStamp;

    @Schema(description = "Jason data")
    private String reading; // JSON con los datos del sensor

    @Schema(description = "Id de Sensor de Data")
    private Integer sensorId;

    private Map<String, Object> sensorValues; // Valores extraídos del JSON
}
