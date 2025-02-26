package cl.pfequipo1.proyecto_final.controller;

import cl.pfequipo1.proyecto_final.entity.Sensor;
import cl.pfequipo1.proyecto_final.entity.SensorData;
import cl.pfequipo1.proyecto_final.repository.SensorDataRepository;
import cl.pfequipo1.proyecto_final.repository.SensorRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/sensor_data")
public class SensorDataController {

    private final SensorDataRepository sensorDataRepository;
    private final SensorRepository sensorRepository;

    public SensorDataController(SensorDataRepository sensorDataRepository, SensorRepository sensorRepository) {
        this.sensorDataRepository = sensorDataRepository;
        this.sensorRepository = sensorRepository;
    }

    @PostMapping
    public ResponseEntity<String> insertSensorData(@RequestBody SensorData sensorData) {
        Optional<Sensor> sensor = sensorRepository.findBySensorApiKey(sensorData.getApiKey());
        if (sensor.isPresent()) {
            sensorData.setSensor(sensor.get());
            sensorDataRepository.save(sensorData);
            return ResponseEntity.status(201).body("Datos insertados correctamente");
        }
        return ResponseEntity.badRequest().body("API Key inválida o sensor no encontrado");
    }

    @GetMapping
    public List<SensorData> getSensorData(
            @RequestParam String company_api_key,
            @RequestParam Long from,
            @RequestParam Long to,
            @RequestParam List<Integer> sensor_id) {
        return sensorDataRepository.findByTimestampBetweenAndSensorIdIn(from, to, sensor_id);
    }
}
