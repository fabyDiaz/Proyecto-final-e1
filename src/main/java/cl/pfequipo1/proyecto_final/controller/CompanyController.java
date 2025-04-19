package cl.pfequipo1.proyecto_final.controller;

import cl.pfequipo1.proyecto_final.dto.CompanyDTO;
import cl.pfequipo1.proyecto_final.dto.CompanyRequestDTO;
import cl.pfequipo1.proyecto_final.service.CompanyServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.util.List;

@RestController
@RequestMapping("/api/v1/companies")
public class CompanyController {

    Logger logger = LoggerFactory.getLogger(CompanyController.class);

    @Autowired
    private CompanyServiceImpl companyService;

    @Operation(
            summary = "Obtener Compañias",
            description = "Retorna las Compañias",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Compañias encontradas",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = CompanyDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Compañias no encontradas")
            }
    )

    @GetMapping
    public List<CompanyDTO> getAllCompanies() {
        logger.info("Getting all Companies From Company Controller");
        return companyService.findAll();
    }

    @Operation(
            summary = "Obtener Compañia segun su Id",
            description = "Retorna los detalles de una Compañia",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Compañia encontrada",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = CompanyDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Compañia no encontrada")
            }
    )

    @GetMapping("/{id}")
    public ResponseEntity<CompanyDTO> findById(@PathVariable Integer id) {
        logger.info("Getting All Companies for id {} From Company Controller", id);
        try{
            return ResponseEntity.status(HttpStatus.OK).body(companyService.findById(id));
        } catch (Exception e){
            CompanyDTO companyDTO = new CompanyDTO();
            companyDTO.setCompanyName("No existe compañía");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(companyDTO);
        }
    }

    @Operation(
            summary = "Crea una Compañia",
            description = "Crea una Compañia segun el rol del Usuario",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Compañia creada",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = CompanyDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Compañia no creada")
            }
    )

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CompanyDTO> create(@RequestBody CompanyRequestDTO companyRequestDTO) {
        logger.info("Posting All Companies for request {} From Company Controller", companyRequestDTO);
        CompanyDTO createdCompany = companyService.create(companyRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCompany);
    }

    @Operation(
            summary = "Actualiza una Compañía",
            description = "Actualiza una Compañía según el rol del Usuario",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Compañia Actualizada",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = CompanyDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Compañia no actualizada")
            }
    )

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CompanyDTO> updateCompany(@PathVariable Integer id, @RequestBody CompanyRequestDTO companyRequestDTO) {
        logger.info("Putting All Companies for request {} From Company Controller", companyRequestDTO);

        try{
            CompanyDTO updatedCompany = companyService.update(id, companyRequestDTO);
            return ResponseEntity.status(HttpStatus.OK).body(updatedCompany);
        } catch (Exception e){
            CompanyDTO companyDTO = new CompanyDTO();
            companyDTO.setCompanyName("No existe compañía a actualizar");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(companyDTO);
        }
    }

    @Operation(
            summary = "Elimina una Compañia",
            description = "Elimina una Compañia segun su Id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Compañia eliminada",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = CompanyDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Compañia no eliminada")
            }
    )

  /*  @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> delete(@PathVariable Integer id) {
        logger.info("Deleting all Companies for id {} From Company Controller", id);
        try{
            companyService.delete(id);
            return ResponseEntity.status(HttpStatus.OK).body("Se elimina la compañía con el ID: " + id);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No existe compañía a Eliminar");
        }
    }
*/
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteCompany(
            @PathVariable Integer id,
            @RequestHeader("company-api-key") String companyApiKey) {
        try {
            companyService.delete(id, companyApiKey);
            return ResponseEntity.ok("Compañía " + id + " eliminada correctamente");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar compañía "+ id);
        }
    }

}

