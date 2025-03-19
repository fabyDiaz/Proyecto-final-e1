package cl.pfequipo1.proyecto_final.controller;

import cl.pfequipo1.proyecto_final.dto.AdminDTO;
import cl.pfequipo1.proyecto_final.dto.CompanyAdminViewDTO;
import cl.pfequipo1.proyecto_final.service.AdminServiceImpl;
import cl.pfequipo1.proyecto_final.service.CompanyServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admins")
public class AdminController {

    @Autowired
    private AdminServiceImpl adminService;

    @Autowired
    private CompanyServiceImpl companyService;

    @Operation(
            summary = "Obtener el Administrador",
            description = "Retorna los detalles del Administrador",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Administrador encontrado",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AdminDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Administrador no encontrado")
            }
    )

    @GetMapping
    public List<AdminDTO> findAll() {
        return adminService.findAll();
    }


    @Operation(
            summary = "Obtener todas las compañías con detalles completos",
            description = "Retorna las compañías incluyendo apiKey (solo para admin)",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Compañías encontradas",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = CompanyAdminViewDTO.class)))
            }
    )
    @GetMapping("/companies")
    @PreAuthorize("hasRole('ADMIN')") // Asegúrate de tener configuración de seguridad
    public ResponseEntity<List<CompanyAdminViewDTO>> getllCompaniesAdmin() {
        return ResponseEntity.ok(companyService.getAllCompaniesForAdmin());
    }

    @Operation(
            summary = "Obtener compañía por ID con detalles completos",
            description = "Retorna una compañía específica incluyendo apiKey (solo para admin)",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Compañía encontrada",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = CompanyAdminViewDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Compañía no encontrada")
            }
    )
    @GetMapping("/companies/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CompanyAdminViewDTO> findCompanyByIdAdmin(@PathVariable Integer id) {
        return ResponseEntity.ok(companyService.getCompanyByIdForAdmin(id));
    }

}

