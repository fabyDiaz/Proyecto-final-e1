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
@Schema(description = "Modelo de Administrador")
public class AdminDTO {
    @Schema(description = "Nombre de Usuario del Administrador")
    private String username;

    @Schema(description = "Rol del Administrador")
    private String role;
}
