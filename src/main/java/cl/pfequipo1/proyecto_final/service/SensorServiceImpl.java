package cl.pfequipo1.proyecto_final.service;

import cl.pfequipo1.proyecto_final.dto.SensorDTO;
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

        // Lista para almacenar todos los sensores
        List<SensorDTO> allSensors = new ArrayList<>();

        for (Location location : locations) {
            List<Sensor> locationSensors = sensorRepository.findByLocation(location);
            System.out.println("Localidad encontrada: " + location.getLocationName());

            locationSensors.stream()
                    .map(sensor -> SensorDTO.builder()
                            .sensorId(sensor.getSensorId())
                            .locationId(location.getLocationId())
                            .sensorName(sensor.getSensorName())
                            .sensorCategory(sensor.getSensorCategory())
                            .sensorMeta(sensor.getSensorMeta())
                            .build())
                    .forEach(sensorDTO -> {
                        System.out.println("Sensor encontrado: " + sensorDTO.getSensorName());
                        allSensors.add(sensorDTO);
                    });

        }
        return allSensors;
    }

    @Override
    public List<SensorDTO> findByLocation(Integer locationId, String companyApiKey) {
        Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new EntityNotFoundException("Location not found"));

        if (!location.getCompany().getCompanyApiKey().equals(companyApiKey)) {
            throw new RuntimeException("Unauthorized access");
        }

        List<Sensor> sensors = sensorRepository.findByLocation(location);
        List<SensorDTO> sensorDTOs = new ArrayList<>();

        for (Sensor sensor : sensors) {
            sensorDTOs.add(SensorDTO.builder()
                    .sensorId(sensor.getSensorId())
                    .locationId(sensor.getLocation().getLocationId())
                    .sensorName(sensor.getSensorName())
                    .sensorCategory(sensor.getSensorCategory())
                    .sensorMeta(sensor.getSensorMeta())
                    .build());
        }

        return sensorDTOs;
    }

    @Override
    public SensorDTO findById(Integer sensorId, String companyApiKey) {
        Sensor sensor = sensorRepository.findById(sensorId)
                .orElseThrow(() -> new EntityNotFoundException("Sensor not found"));

        if (!sensor.getLocation().getCompany().getCompanyApiKey().equals(companyApiKey)) {
            throw new RuntimeException("Unauthorized access");
        }

        return SensorDTO.builder()
                .sensorId(sensor.getSensorId())
                .locationId(sensor.getLocation().getLocationId())
                .sensorName(sensor.getSensorName())
                .sensorCategory(sensor.getSensorCategory())
                .sensorMeta(sensor.getSensorMeta())
                .build();
    }

    @Override
    public SensorDTO create(SensorDTO sensorDTO, String companyApiKey) {

        // Validar que la API key existe
        Company company = companyRepository.findByCompanyApiKey(companyApiKey)
                .orElseThrow(() -> new EntityNotFoundException("Invalid company API key"));

        Location location = locationRepository.findById(sensorDTO.getLocationId())
                .orElseThrow(() -> new RuntimeException("Location not found"));

        // Generar una API key única
        String apiKey = generateUniqueApiKey();

        Sensor sensor = Sensor.builder()
                .location(location)
                .sensorName(sensorDTO.getSensorName())
                .sensorCategory(sensorDTO.getSensorCategory())
                .sensorMeta(sensorDTO.getSensorMeta())
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
    public SensorDTO update(Integer sensorId, SensorDTO sensorDTO, String companyApiKey, String adminUsername, String adminPassword) {
        Sensor sensor = sensorRepository.findById(sensorId)
                .orElseThrow(() -> new EntityNotFoundException("Sensor not found"));

        if (!sensor.getLocation().getCompany().getCompanyApiKey().equals(companyApiKey)) {
            throw new RuntimeException("Unauthorized access");
        }

        sensor.setSensorName(sensorDTO.getSensorName());
        sensor.setSensorCategory(sensorDTO.getSensorCategory());
        sensor.setSensorMeta(sensorDTO.getSensorMeta());
        Sensor updatedSensor = sensorRepository.save(sensor);

        return SensorDTO.builder()
                .sensorId(updatedSensor.getSensorId())
                .locationId(updatedSensor.getLocation().getLocationId())
                .sensorName(updatedSensor.getSensorName())
                .sensorCategory(updatedSensor.getSensorCategory())
                .sensorMeta(updatedSensor.getSensorMeta())
                .build();
    }

    @Override
    public void delete(Integer sensorId, String companyApiKey, String adminUsername, String adminPassword) {
        Sensor sensor = sensorRepository.findById(sensorId)
                .orElseThrow(() -> new EntityNotFoundException("Sensor not found"));

        if (!sensor.getLocation().getCompany().getCompanyApiKey().equals(companyApiKey)) {
            throw new RuntimeException("Unauthorized access");
        }

        sensorRepository.delete(sensor);
    }
}
