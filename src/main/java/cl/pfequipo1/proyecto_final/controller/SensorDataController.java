package cl.pfequipo1.proyecto_final.controller;

import cl.pfequipo1.proyecto_final.entity.Sensor;
import cl.pfequipo1.proyecto_final.entity.SensorData;
import cl.pfequipo1.proyecto_final.repository.SensorDataRepository;
import cl.pfequipo1.proyecto_final.repository.SensorRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;


@RestController
@RequestMapping("/api/v1/sensor_data")
public class SensorDataController {

    @Autowired
    private SensorDataRepository sensorDataRepository;
    @Autowired
    private SensorRepository sensorRepository;

    // POST /api/v1/sensor_data	
    // Se usa sensor_api_key en el body
    @PostMapping
    public ResponseEntity<?> insertSensorData(@RequestBody SensorDataInsertRequest request) {
        // 1) Validar sensor_api_key
        String sensorApiKey = request.getApi_key();
        Sensor sensor = sensorRepository.findBySensorApiKey(sensorApiKey);
        if (sensor == null) {
            return ResponseEntity.badRequest().body("sensor_api_key inválida o sensor no encontrado.");
        }

        // 2) Recorrer json_data
        List<Map<String, Object>> dataList = request.getJson_data();
        if (dataList == null || dataList.isEmpty()) {
            return ResponseEntity.badRequest().body("No se recibió json_data o está vacío.");
        }

        List<SensorData> savedRecords = new ArrayList<>();
        for (Map<String, Object> item : dataList) {

            // Parsear valores
            Double temperature = parseDouble(item.get("temperature"));
            Double humidity = parseDouble(item.get("humidity"));
            Double speed = parseDouble(item.get("speed"));
            String position = item.containsKey("position") ? item.get("position").toString() : null;
            Double voltage = parseDouble(item.get("voltage"));
            Double currentVal = parseDouble(item.get("current"));

            // createdAt (si no viene, se usa now())
            LocalDateTime createdAt = LocalDateTime.now();
            if (item.get("timestamp_epoch") != null) {
                long epoch = Long.parseLong(item.get("timestamp_epoch").toString());
                createdAt = LocalDateTime.ofInstant(Instant.ofEpochSecond(epoch), ZoneOffset.UTC);
            }

            SensorData sd = new SensorData(temperature, humidity, speed, position, voltage, currentVal, createdAt);
            sd.setSensor(sensor);
            SensorData saved = sensorDataRepository.save(sd);
            savedRecords.add(saved);
        }

        // 201 Created
        return ResponseEntity.status(201).body(savedRecords);
    }

    // GET /api/v1/sensor_data?companyApiKey=XXXX&from=ep&to=ep&sensor_id=1,2,3
    @GetMapping
    public ResponseEntity<List<SensorData>> getSensorData(
        @RequestParam("companyApiKey") String companyApiKey,
        @RequestParam("from") Long fromEpoch,
        @RequestParam("to") Long toEpoch,
        @RequestParam("sensor_id") List<Integer> sensorIds
    ) {
        // Normalmente, validas companyApiKey en CompanyRepository, etc.

        // Convertir epoch
        LocalDateTime fromTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(fromEpoch), ZoneOffset.UTC);
        LocalDateTime toTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(toEpoch), ZoneOffset.UTC);

        // Filtrar manual o con query custom
        List<SensorData> all = sensorDataRepository.findAll();
        List<SensorData> filtered = new ArrayList<>();
        for (SensorData sd : all) {
            if (sensorIds.contains(sd.getSensor().getSensorId())) {
                LocalDateTime createdAt = sd.getCreatedAt();
                if (!createdAt.isBefore(fromTime) && !createdAt.isAfter(toTime)) {
                    filtered.add(sd);
                }
            }
        }
        return ResponseEntity.ok(filtered);
    }

    // CRUD opcional para sensor_data
    @GetMapping("/all")
    public List<SensorData> getAll() {
        return sensorDataRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SensorData> getOneData(@PathVariable Integer id) {
        return sensorDataRepository.findById(id)
                .map(d -> ResponseEntity.ok(d))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<SensorData> updateData(@PathVariable Integer id, @RequestBody SensorData data) {
        return sensorDataRepository.findById(id)
                .map(existing -> {
                    existing.setTemperature(data.getTemperature());
                    existing.setHumidity(data.getHumidity());
                    existing.setSpeed(data.getSpeed());
                    existing.setPosition(data.getPosition());
                    existing.setVoltage(data.getVoltage());
                    existing.setCurrentValue(data.getCurrentValue());
                    // usualmente no cambiarías sensor o createdAt, depende de tu negocio
                    SensorData updated = sensorDataRepository.save(existing);
                    return ResponseEntity.ok(updated);
                })
                .orElse(ResponseEntity.notFound().build());
    }



    // Helper: parseDouble
    private Double parseDouble(Object o) {
        if (o == null) return null;
        try {
            return Double.parseDouble(o.toString());
        } catch (NumberFormatException e) {
            return null; // o lanza una excepción
        }
    }

    // DTO para la inserción de data
    public static class SensorDataInsertRequest {
        private String api_key;
        private List<Map<String, Object>> json_data;

        public String getApi_key() {
            return api_key;
        }
        public void setApi_key(String api_key) {
            this.api_key = api_key;
        }
        public List<Map<String, Object>> getJson_data() {
            return json_data;
        }
        public void setJson_data(List<Map<String, Object>> json_data) {
            this.json_data = json_data;
        }
    }
}


