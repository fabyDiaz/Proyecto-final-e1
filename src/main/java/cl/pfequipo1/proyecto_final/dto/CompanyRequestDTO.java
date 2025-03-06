package cl.pfequipo1.proyecto_final.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyRequestDTO {
    private String companyName;
    // No incluimos companyApiKey aquí, ya que será generada automáticamente
    //private String companyApiKey;
}
