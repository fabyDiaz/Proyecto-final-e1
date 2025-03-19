package cl.pfequipo1.proyecto_final.controller;

import cl.pfequipo1.proyecto_final.dto.SensorDTO;
import cl.pfequipo1.proyecto_final.service.SensorServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/sensor")
public class SensorController {

    @Autowired
    private SensorServiceImpl sensorService;

    @Operation(
            summary = "Crea un Sensor",
            description = "Crea un Sensor",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Sensor creado",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = SensorDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Sensor no creado")
            }
    )

    @PostMapping
    public ResponseEntity<SensorDTO> createSensor(@RequestBody SensorDTO sensor, @RequestHeader("company-api-key") String companyApiKey) {

        SensorDTO createdSensor = sensorService.create(sensor, companyApiKey);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSensor);
    }

    @Operation(
            summary = "Obtener Sensores",
            description = "Retorna las Sensores",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Sensores encontrados",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = SensorDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Sensores no encontrados")
            }
    )

    @GetMapping
    public ResponseEntity<List<SensorDTO>> getAllSensor(@RequestHeader("company-api-key") String companyApiKey) {

        List<SensorDTO> sensors = sensorService.findAll(companyApiKey);
        return ResponseEntity.ok(sensors);
    }

    @Operation(
            summary = "Sensor según su Id",
            description = "Muestra un Sensor según su Id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Sensor encontrado",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = SensorDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Sensor no encontrado")
            }
    )

    @GetMapping("/{sensorId}")
    public ResponseEntity<SensorDTO> getSensorById(@PathVariable Integer sensorId, @RequestHeader("company-api-key") String companyApiKey) {
        SensorDTO sensor = sensorService.findById(sensorId, companyApiKey);
        return ResponseEntity.ok(sensor);
    }

    @Operation(
            summary = "Sensor según su Locacion",
            description = "Muestra un Sensor según su Locacion",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Sensor encontrado",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = SensorDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Sensor no encontrado")
            }
    )

    @GetMapping("/location/{locationId}")
    public ResponseEntity<List<SensorDTO>> getSensorsByLocation(@PathVariable Integer locationId, @RequestHeader("company-api-key") String companyApiKey) {
        List<SensorDTO> sensors = sensorService.findByLocation(locationId, companyApiKey);
        return ResponseEntity.ok(sensors);
    }

    @Operation(
            summary = "Actualiza un Sensor segun id",
            description = "Actualiza un Sensor según su id siendo administrador",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Sensor actualizado",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = SensorDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Sensor no actualizado")
            }
    )

    @PutMapping("/{sensorId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SensorDTO> updateSensor(@PathVariable Integer sensorId, @RequestBody SensorDTO sensorDTO,
                                                  @RequestHeader("company-api-key") String companyApiKey,
                                                  @RequestHeader("admin-username") String adminUsername,
                                                  @RequestHeader("admin-password") String adminPassword) {
        SensorDTO updatedSensor = sensorService.update(sensorId, sensorDTO, companyApiKey, adminUsername, adminPassword);
        return ResponseEntity.ok(updatedSensor);
    }

    @Operation(
            summary = "Elimina Sensor según su Id",
            description = "Elimina un Sensor según su Id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Sensor eliminada",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = SensorDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Sensor no eliminada")
            }
    )
    
    @DeleteMapping("/{sensorId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteSensor(@PathVariable Integer sensorId,
                                             @RequestHeader("company-api-key") String companyApiKey,
                                             @RequestHeader("admin-username") String adminUsername,
                                             @RequestHeader("admin-password") String adminPassword) {
        sensorService.delete(sensorId, companyApiKey, adminUsername, adminPassword);
        return ResponseEntity.ok().build();
    }

}
