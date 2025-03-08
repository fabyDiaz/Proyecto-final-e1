package cl.pfequipo1.proyecto_final.service;

import cl.pfequipo1.proyecto_final.dto.SensorDTO;
import cl.pfequipo1.proyecto_final.dto.SensorRequestDTO;
import org.springframework.stereotype.Service;

import java.util.List;

public interface ISensorService {
    List<SensorDTO> findAll(String companyApiKey);
    List<SensorDTO> findByLocation(Integer locationId, String companyApiKey);
    SensorDTO findById(Integer sensorId, String companyApiKey);
    SensorDTO create(SensorRequestDTO sensorRequestDTO, String companyApiKey, String adminUsername, String adminPassword);
    SensorDTO update(Integer sensorId, SensorRequestDTO sensorRequestDTO, String companyApiKey, String adminUsername, String adminPassword);
    void delete(Integer sensorId, String companyApiKey, String adminUsername, String adminPassword);

}
