package cl.pfequipo1.proyecto_final.dto;

import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Modelo de Locacion")
public class LocationDTO {
    @Schema(description = "Id de Locacion")
    private Integer locationId;
    @Schema(description = "Nombre de Locacion")
    private String locationName;
    @Schema(description = "Pais de Locacion")
    private String locationCountry;
    @Schema(description = "Ciudad de Locacion")
    private String locationCity;
    @Schema(description = "Meta de Locacion")
    private String locationMeta;
    @Schema(description = "Id de Compañia")
    private Integer companyId; // Para saber a qué compañía pertenece

    public LocationDTO(Integer locationId, String locationName, String locationCountry, String locationCity, String locationMeta) {
        this.locationId = locationId;
        this.locationName = locationName;
        this.locationCountry = locationCountry;
        this.locationCity = locationCity;
        this.locationMeta = locationMeta;
    }
}
