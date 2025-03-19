package cl.pfequipo1.proyecto_final.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Modelo de Compañia con información completa para Admin")
public class CompanyAdminViewDTO {
    @Schema(description = "Id de Compañia")
    private Integer id;

    @Schema(description = "Nombre de la Compañia")
    private String companyName;

    @Schema(description = "API Key de la Compañia")
    private String companyApiKey;
}
