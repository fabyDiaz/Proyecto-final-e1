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
@Schema(description = "Modelo de Compañia")
public class CompanyDTO {
    @Schema(description = "Id de Compañia")
    private Integer id;

    @Schema(description = "Nombre de la Compañia")
    private String companyName;
}
