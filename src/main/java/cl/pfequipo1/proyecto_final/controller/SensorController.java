package cl.pfequipo1.proyecto_final.controller;

import cl.pfequipo1.proyecto_final.dto.SensorDTO;
import cl.pfequipo1.proyecto_final.service.SensorServiceImpl;
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

    @PostMapping
    public ResponseEntity<SensorDTO> createSensor(@RequestBody SensorDTO sensor, @RequestHeader("company-api-key") String companyApiKey) {

        SensorDTO createdSensor = sensorService.create(sensor, companyApiKey);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSensor);
    }

    @GetMapping
    public ResponseEntity<List<SensorDTO>> getAllSensor(@RequestHeader("company-api-key") String companyApiKey) {

        List<SensorDTO> sensors = sensorService.findAll(companyApiKey);
        return ResponseEntity.ok(sensors);
    }

    @GetMapping("/{sensorId}")
    public ResponseEntity<SensorDTO> getSensorById(@PathVariable Integer sensorId, @RequestHeader("company-api-key") String companyApiKey) {
        SensorDTO sensor = sensorService.findById(sensorId, companyApiKey);
        return ResponseEntity.ok(sensor);
    }

    @GetMapping("/location/{locationId}")
    public ResponseEntity<List<SensorDTO>> getSensorsByLocation(@PathVariable Integer locationId, @RequestHeader("company-api-key") String companyApiKey) {
        List<SensorDTO> sensors = sensorService.findByLocation(locationId, companyApiKey);
        return ResponseEntity.ok(sensors);
    }

    @PutMapping("/{sensorId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SensorDTO> updateSensor(@PathVariable Integer sensorId, @RequestBody SensorDTO sensorDTO,
                                                  @RequestHeader("company-api-key") String companyApiKey,
                                                  @RequestHeader("admin-username") String adminUsername,
                                                  @RequestHeader("admin-password") String adminPassword) {
        SensorDTO updatedSensor = sensorService.update(sensorId, sensorDTO, companyApiKey, adminUsername, adminPassword);
        return ResponseEntity.ok(updatedSensor);
    }

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
