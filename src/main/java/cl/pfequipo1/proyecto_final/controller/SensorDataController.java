package cl.pfequipo1.proyecto_final.controller;

import cl.pfequipo1.proyecto_final.dto.SensorDataDTO;
import cl.pfequipo1.proyecto_final.dto.SensorDataRequestDTO;
import cl.pfequipo1.proyecto_final.service.SensorDataServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
    public ResponseEntity<?> saveSensorData(@RequestBody SensorDataRequestDTO requestDTO) {
        try {
            List<SensorDataDTO> savedData = sensorDataService.processSensorDataRequest(requestDTO);
            return new ResponseEntity<>(savedData, HttpStatus.CREATED);
        } catch (RuntimeException ex) {
            System.err.println("Error al guardar sensor data: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", ex.getMessage()));
        }
    }

    @Operation(
            summary = "Obtener datos de sensores",
            description = "Retorna los datos de los sensores filtrados por compañía, rango de tiempo y ID de sensores",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Datos encontrados",
                            content = @Content(mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(
                                            type = "object",
                                            example = """
                                    {
                                      "id": "43ea0bf1-df2a-4f6f-adce-46b00da19985",
                                      "sensorId": 8,
                                      "sensorValues": {
                                        "temp": 25,
                                        "humidity": 0.4,
                                        "timestamp": 1744760456
                                      }
                                    }
                                    """
                                    )))),
                    @ApiResponse(responseCode = "401", description = "API Key no proporcionada o inválida"),
                    @ApiResponse(responseCode = "404", description = "Datos no encontrados")
            }
    )
    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getSensorData(
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

        try{
            List<Map<String, Object>> sensorData = sensorDataService.getSensorData(companyApiKey, from, to, sensor_id);
            return ResponseEntity.status(HttpStatus.OK).body(sensorData);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }


    @Operation(
            summary = "Actualizar datos de un sensor",
            description = "Actualiza los datos de un sensor existente",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Datos actualizados correctamente"),
                    @ApiResponse(responseCode = "400", description = "Datos inválidos"),
                    @ApiResponse(responseCode = "404", description = "Datos no encontrados")
            }
    )
    @PutMapping("/{dataId}")
    public ResponseEntity<?> updateSensorData(
            @RequestParam String sensor_api_key,
            @PathVariable String dataId,
            @RequestBody Map<String, Object> updatedData) {
        try {
            Map<String, Object> result = sensorDataService.updateSensorData(sensor_api_key, dataId, updatedData);
            return ResponseEntity.ok(result);
        } catch (RuntimeException ex) {
            String errorMessage = ex.getMessage();
            HttpStatus status = errorMessage.contains("not found") ?
                    HttpStatus.NOT_FOUND : HttpStatus.BAD_REQUEST;
            return ResponseEntity.status(status).body(Map.of("error", errorMessage));
        }
    }

    @Operation(
            summary = "Eliminar datos de un sensor",
            description = "Elimina los datos de un sensor existente",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Datos eliminados correctamente"),
                    @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
                    @ApiResponse(responseCode = "404", description = "Datos no encontrados")
            }
    )
    @DeleteMapping("/{dataId}")
    public ResponseEntity<?> deleteSensorData(
            @RequestParam String sensor_api_key,
            @PathVariable String dataId) {
        try {
            sensorDataService.deleteSensorData(sensor_api_key, dataId);
            return ResponseEntity.status(HttpStatus.OK).body("Se elimina data del Sensor: " + dataId);
        } catch (RuntimeException ex) {
            String errorMessage = ex.getMessage();
            HttpStatus status = errorMessage.contains("not found") ?
                    HttpStatus.NOT_FOUND : HttpStatus.BAD_REQUEST;
            return ResponseEntity.status(status).body(Map.of("error", errorMessage));
        }
    }
}
