package cl.pfequipo1.proyecto_final.service;

import cl.pfequipo1.proyecto_final.dto.SensorDataDTO;

import java.util.List;

public interface ISensorDataService {

    // Método para insertar datos de sensor usando sensor_api_key
    List<SensorDataDTO> saveSensorData(String sensorApiKey, List<SensorDataDTO> sensorDataList);

    // Método para consultar datos de sensor usando company_api_key
    List<SensorDataDTO> getSensorData(String companyApiKey, Integer fromTimeStamp, Integer toTimeStamp, List<Integer> sensorIds);
}
