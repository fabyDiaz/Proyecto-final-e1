package cl.pfequipo1.proyecto_final.controller;

import cl.pfequipo1.proyecto_final.dto.AdminDTO;
import cl.pfequipo1.proyecto_final.service.AdminServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
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

}

