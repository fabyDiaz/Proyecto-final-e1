package cl.pfequipo1.proyecto_final.controller;

import cl.pfequipo1.proyecto_final.dto.LocationDTO;
import cl.pfequipo1.proyecto_final.service.LocationServiceImpl;
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
@RequestMapping("/api/v1/locations")
public class LocationController {

    @Autowired
    private LocationServiceImpl locationService;

    @Operation(
            summary = "Crea una Locacion",
            description = "Crea una Locacion",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Locacion creada",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = LocationDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Locacion no creada")
            }
    )

    @PostMapping
    public ResponseEntity<LocationDTO> createLocation(@RequestBody LocationDTO locationDTO, @RequestHeader("company-api-key") String companyApiKey) {

        try{
            LocationDTO createdLocation = locationService.create(locationDTO, companyApiKey);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdLocation);
        } catch (Exception e) {
            LocationDTO location = new LocationDTO();
            location.setLocationName("No existe una Locacion");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(location);
        }
    }

    @Operation(
            summary = "Obtener Locaciones",
            description = "Retorna las Locaciones",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Locaciones encontradas",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = LocationDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Locaciones no encontradas")
            }
    )

    @GetMapping
    public ResponseEntity<List<LocationDTO>> getAllLocations(@RequestHeader("company-api-key") String companyApiKey) {

            List<LocationDTO> locations = locationService.findAll(companyApiKey);
            return ResponseEntity.status(HttpStatus.OK).body(locations);
    }

    @Operation(
            summary = "Obtener Locacion segun su Id",
            description = "Retorna los detalles de una Locacion",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Locacion encontrada",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = LocationDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Locacion no encontrada")
            }
    )

    @GetMapping("/{id}")
    public ResponseEntity<LocationDTO> findById(@PathVariable Integer id, @RequestHeader("company-api-key") String companyApiKey) {
        try{
            LocationDTO locationDTO = locationService.findById(id, companyApiKey);
            return ResponseEntity.status(HttpStatus.OK).body(locationDTO);
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new LocationDTO());
        }

    }

    @Operation(
            summary = "Elimina una Locacion",
            description = "Elimina una Locacion segun su Id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Locacion eliminada",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = LocationDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Locacion no eliminada")
            }
    )

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> delete(@PathVariable Integer id,@RequestHeader("company-api-key") String companyApiKey) {
        try{
            locationService.delete(id, companyApiKey);
            return ResponseEntity.status(HttpStatus.OK).body("Locacion eliminada");
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No existe Locacion");
        }

    }

    @Operation(
            summary = "Actualiza una Locacion",
            description = "Actualiza una Locacion segun el rol del Usuario",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Locacion Actualizada",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = LocationDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Locacion no actualizada")
            }
    )

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LocationDTO> updateLocation(@PathVariable Integer id, @RequestBody LocationDTO locationDTO, @RequestHeader("company-api-key") String companyApiKey) {

        try{
            LocationDTO updatedLocation = locationService.update(id, locationDTO, companyApiKey );
            return ResponseEntity.status(HttpStatus.OK).body(updatedLocation);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new LocationDTO());
        }

    }
}

