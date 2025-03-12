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
@Schema(description = "Modelo de Data de Sensor")
public class SensorDataDTO {

    @Schema(description = "Id de Data")
    private String id;

    @Schema(description = "Marca de tiempo de Data")
    private Integer timeStamp;

    @Schema(description = "Temperatura de Data")
    private Float temperature;

    @Schema(description = "Voltage de Data")
    private Float voltage;

    @Schema(description = "Id de Sensor de Data")
    private Integer sensorId;
}
