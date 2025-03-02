package cl.pfequipo1.proyecto_final.controller;

import cl.pfequipo1.proyecto_final.entity.Sensor;
import cl.pfequipo1.proyecto_final.repository.SensorRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


import cl.pfequipo1.proyecto_final.entity.Location;
import cl.pfequipo1.proyecto_final.entity.Sensor;
import cl.pfequipo1.proyecto_final.entity.Company;
import cl.pfequipo1.proyecto_final.repository.CompanyRepository;
import cl.pfequipo1.proyecto_final.repository.LocationRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/sensors")
public class SensorController {

    @Autowired
    private SensorRepository sensorRepository;
    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private CompanyRepository companyRepository;

    // GET /api/v1/sensors?companyApiKey=XXXX
    @GetMapping
    public List<Sensor> getAllSensors(@RequestParam String companyApiKey) {
        Company company = companyRepository.findByCompanyApiKey(companyApiKey);
        if (company == null) {
            throw new RuntimeException("companyApiKey inválida");
        }
        // Retornar todos o filtrar
        return sensorRepository.findAll();
    }

    // GET /api/v1/sensors/{id}?companyApiKey=XXXX
    @GetMapping("/{id}")
    public ResponseEntity<Sensor> getSensorById(@PathVariable Integer id, @RequestParam String companyApiKey) {
        Company company = companyRepository.findByCompanyApiKey(companyApiKey);
        if (company == null) {
            return ResponseEntity.badRequest().build();
        }
        return sensorRepository.findById(id)
                .map(sensor -> ResponseEntity.ok(sensor))
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /api/v1/sensors?companyApiKey=XXXX&locationId=#
    @PostMapping
    public ResponseEntity<Sensor> createSensor(@RequestParam String companyApiKey,
                                              @RequestParam Integer locationId,
                                              @RequestBody Sensor sensorRequest) {
        // Validar companyApiKey
        Company company = companyRepository.findByCompanyApiKey(companyApiKey);
        if (company == null) {
            return ResponseEntity.badRequest().build();
        }
        // Buscar location
        Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new RuntimeException("Location no encontrada"));

        // Generar sensorApiKey si no está
        if (sensorRequest.getSensorApiKey() == null || sensorRequest.getSensorApiKey().isEmpty()) {
            sensorRequest.setSensorApiKey(UUID.randomUUID().toString().replace("-", ""));
        }

        // Asociar sensor con la location
        sensorRequest.setLocation(location);
        Sensor saved = sensorRepository.save(sensorRequest);
        return ResponseEntity.ok(saved);
    }

    // PUT /api/v1/sensors/{id}?companyApiKey=XXXX
    @PutMapping("/{id}")
    public ResponseEntity<Sensor> updateSensor(@PathVariable Integer id,
                                              @RequestParam String companyApiKey,
                                              @RequestBody Sensor sensorDetails) {
        Company company = companyRepository.findByCompanyApiKey(companyApiKey);
        if (company == null) {
            return ResponseEntity.badRequest().build();
        }
        return sensorRepository.findById(id)
                .map(sensor -> {
                    sensor.setSensorName(sensorDetails.getSensorName());
                    sensor.setSensorCategory(sensorDetails.getSensorCategory());
                    sensor.setSensorMeta(sensorDetails.getSensorMeta());
                    // Podrías permitir cambiar location u otras lógicas
                    Sensor updated = sensorRepository.save(sensor);
                    return ResponseEntity.ok(updated);
                })
                .orElse(ResponseEntity.notFound().build());
    }


}

