package cl.pfequipo1.proyecto_final.controller;

import cl.pfequipo1.proyecto_final.dto.CompanyDTO;
import cl.pfequipo1.proyecto_final.dto.LocationDTO;
import cl.pfequipo1.proyecto_final.dto.SensorDTO;
import cl.pfequipo1.proyecto_final.service.LocationServiceImpl;
import cl.pfequipo1.proyecto_final.service.SensorServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

}
