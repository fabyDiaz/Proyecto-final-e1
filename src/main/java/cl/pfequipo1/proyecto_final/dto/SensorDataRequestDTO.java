package cl.pfequipo1.proyecto_final.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Modelo para recibir datos del sensor")
public class SensorDataRequestDTO {
    @Schema(description = "API Key del sensor")
    private String api_key;

    @Schema(description = "Lista de datos del sensor")
    private List<Map<String, Object>> json_data;
}
