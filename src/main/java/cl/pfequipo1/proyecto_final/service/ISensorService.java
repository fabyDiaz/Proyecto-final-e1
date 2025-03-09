package cl.pfequipo1.proyecto_final.service;

import cl.pfequipo1.proyecto_final.dto.SensorDTO;

import java.util.List;

public interface ISensorService {
    List<SensorDTO> findAll(String companyApiKey);
    List<SensorDTO> findByLocation(Integer locationId, String companyApiKey);
    SensorDTO findById(Integer sensorId, String companyApiKey);
    SensorDTO create(SensorDTO sensorDTO, String companyApiKey);
    SensorDTO update(Integer sensorId, SensorDTO sensorDTO, String companyApiKey, String adminUsername, String adminPassword);
    void delete(Integer sensorId, String companyApiKey, String adminUsername, String adminPassword);

}
