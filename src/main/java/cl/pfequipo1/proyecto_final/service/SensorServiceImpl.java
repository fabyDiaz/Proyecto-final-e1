package cl.pfequipo1.proyecto_final.service;

import cl.pfequipo1.proyecto_final.dto.LocationDTO;
import cl.pfequipo1.proyecto_final.dto.SensorDTO;
import cl.pfequipo1.proyecto_final.dto.SensorRequestDTO;
import cl.pfequipo1.proyecto_final.entity.Company;
import cl.pfequipo1.proyecto_final.entity.Location;
import cl.pfequipo1.proyecto_final.entity.Sensor;
import cl.pfequipo1.proyecto_final.repository.AdminRepository;
import cl.pfequipo1.proyecto_final.repository.CompanyRepository;
import cl.pfequipo1.proyecto_final.repository.LocationRepository;
import cl.pfequipo1.proyecto_final.repository.SensorRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class SensorServiceImpl implements ISensorService{

    @Autowired
    SensorRepository sensorRepository;
    @Autowired
    LocationRepository locationRepository;
    @Autowired
    CompanyRepository companyRepository;
    @Autowired
    AdminRepository adminRepository;

    @Override
    public List<SensorDTO> findAll(String companyApiKey) {
        // Validar que la API key existe
        Company company = companyRepository.findByCompanyApiKey(companyApiKey)
                .orElseThrow(() -> new RuntimeException("Invalid API Key"));

        // Buscar todas las localidades de esta compañía
        List<Location> locations = locationRepository.findByCompany(company);

        // Buscar todos los sensores de estas localidades
        List<SensorDTO> sensors = new ArrayList<>();
        for (Location location : locations) {
            sensors.stream()
                    .map(sensor -> SensorDTO.builder()
                            .sensorId(sensor.getSensorId())
                            .locationId(location.getLocationId())
                            .sensorName(sensor.getSensorName())
                            .sensorCategory(sensor.getSensorCategory())
                            .sensorMeta(sensor.getSensorMeta())
                            .build())
                    .toList();;
        }
        return sensors;
    }

    @Override
    public List<SensorDTO> findByLocation(Integer locationId, String companyApiKey) {
        return List.of();
    }

    @Override
    public SensorDTO findById(Integer sensorId, String companyApiKey) {
        return null;
    }

    @Override
    public SensorDTO create(SensorRequestDTO sensorRequestDTO, String companyApiKey, String adminUsername, String adminPassword) {

        // Validar que la API key existe
        Company company = companyRepository.findByCompanyApiKey(companyApiKey)
                .orElseThrow(() -> new EntityNotFoundException("Invalid company API key"));

        Location location = locationRepository.findById(sensorRequestDTO.getLocationId())
                .orElseThrow(() -> new RuntimeException("Location not found"));

        // Generar una API key única
        String apiKey = generateUniqueApiKey();

        Sensor sensor = Sensor.builder()
                .location(location)
                .sensorName(sensorRequestDTO.getSensorName())
                .sensorCategory(sensorRequestDTO.getSensorCategory())
                .sensorMeta(sensorRequestDTO.getSensorMeta())
                .sensorApiKey(apiKey)
                .build();

        Sensor savedSensor = sensorRepository.save(sensor);

        return SensorDTO.builder()
                .sensorId(savedSensor.getSensorId())
                .locationId(location.getLocationId())
                .sensorName(savedSensor.getSensorName())
                .sensorCategory(savedSensor.getSensorCategory())
                .sensorMeta(savedSensor.getSensorMeta())
                .build();
    }

    private String generateUniqueApiKey() {
        // Generar una API key alfanumérica aleatoria de 32 caracteres
        String apiKey = UUID.randomUUID().toString().replace("-", "");

        // Verificar que no exista ya una compañía con esta API key
        while (sensorRepository.findBySensorApiKey(apiKey).isPresent()) {
            apiKey = UUID.randomUUID().toString().replace("-", "");
        }

        return apiKey;
    }

    @Override
    public SensorDTO update(Integer sensorId, SensorRequestDTO sensorRequestDTO, String companyApiKey, String adminUsername, String adminPassword) {
        return null;
    }

    @Override
    public void delete(Integer sensorId, String companyApiKey, String adminUsername, String adminPassword) {

    }
}
