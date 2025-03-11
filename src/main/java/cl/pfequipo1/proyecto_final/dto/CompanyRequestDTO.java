package cl.pfequipo1.proyecto_final.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Modelo de Compañia al ser requerida")
public class CompanyRequestDTO {
    @Schema(description = "Nombre de Compañia")
    private String companyName;
    // No incluimos companyApiKey aquí, ya que será generada automáticamente
    //private String companyApiKey;
}
