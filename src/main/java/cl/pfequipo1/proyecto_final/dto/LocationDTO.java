package cl.pfequipo1.proyecto_final.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LocationDTO {
    private Integer locationId;
    private String locationName;
    private String locationCountry;
    private String locationCity;
    private String locationMeta;
    private Integer companyId; // Para saber a qué compañía pertenece
}
