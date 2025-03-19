package cl.pfequipo1.proyecto_final.controller;

import cl.pfequipo1.proyecto_final.dto.SensorDataDTO;
import cl.pfequipo1.proyecto_final.dto.SensorDataRequestDTO;
import cl.pfequipo1.proyecto_final.service.SensorDataServiceImpl;
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
@RequestMapping("/api/v1/sensor_data")
public class SensorDataController {
    @Autowired
    private SensorDataServiceImpl sensorDataService;

    @Operation(
            summary = "Guardar datos de sensores",
            description = "Almacena los datos proporcionados por los sensores",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Datos guardados correctamente",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = SensorDataDTO.class, type = "array"))),
                    @ApiResponse(responseCode = "400", description = "Datos inválidos")
            }
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<List<SensorDataDTO>> saveSensorData(@RequestBody SensorDataRequestDTO requestDTO) {
        List<SensorDataDTO> savedData = sensorDataService.processSensorDataRequest(requestDTO);
        return new ResponseEntity<>(savedData, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Obtener datos de sensores",
            description = "Retorna los datos de los sensores filtrados por compañía, rango de tiempo y ID de sensores",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Datos encontrados",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = SensorDataDTO.class, type = "array"))),
                    @ApiResponse(responseCode = "401", description = "API Key no proporcionada o inválida"),
                    @ApiResponse(responseCode = "404", description = "Datos no encontrados")
            }
    )
    @GetMapping
    public ResponseEntity<List<SensorDataDTO>> getSensorData(
            @RequestParam(required = false) String company_api_key,
            @RequestHeader(value = "Company-Api-Key", required = false) String headerApiKey,
            @RequestParam Integer from,
            @RequestParam Integer to,
            @RequestParam List<Integer> sensor_id) {

        // Usar la API key del header si está presente, de lo contrario usar la del parámetro
        String companyApiKey = headerApiKey != null ? headerApiKey : company_api_key;

        if (companyApiKey == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<SensorDataDTO> sensorData = sensorDataService.getSensorData(companyApiKey, from, to, sensor_id);
        return ResponseEntity.ok(sensorData);
    }
}
