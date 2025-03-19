package cl.pfequipo1.proyecto_final.controller;

import cl.pfequipo1.proyecto_final.dto.CompanyAdminViewDTO;
import cl.pfequipo1.proyecto_final.dto.CompanyDTO;
import cl.pfequipo1.proyecto_final.dto.CompanyRequestDTO;
import cl.pfequipo1.proyecto_final.service.CompanyServiceImpl;
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
        CompanyDTO companyDTO = companyService.findById(id);
        return ResponseEntity.ok(companyDTO);
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
        CompanyDTO createdCompany = companyService.create(companyRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCompany);
    }

    @Operation(
            summary = "Actualiza una Compañia",
            description = "Actualiza una Compañia segun el rol del Usuario",
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

        CompanyDTO updatedCompany = companyService.update(id, companyRequestDTO);
        return ResponseEntity.ok(updatedCompany);
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

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> delete(@PathVariable Integer id) {
        companyService.delete(id);
        return ResponseEntity.ok("Compañía eliminada "+id);
    }

}

