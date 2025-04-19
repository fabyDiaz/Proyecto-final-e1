package cl.pfequipo1.proyecto_final.service;

import cl.pfequipo1.proyecto_final.dto.SensorDataDTO;
import cl.pfequipo1.proyecto_final.dto.SensorDataRequestDTO;

import java.util.List;
import java.util.Map;

public interface ISensorDataService {

    // Método para insertar datos de sensor usando sensor_api_key
    List<SensorDataDTO> saveSensorData(String sensorApiKey, List<SensorDataDTO> sensorDataList);

    // Método para consultar datos de sensor usando company_api_key
    List<Map<String, Object>> getSensorData(String companyApiKey, Integer fromTimeStamp, Integer toTimeStamp, List<Integer> sensorIds);

    List<SensorDataDTO> processSensorDataRequest(SensorDataRequestDTO requestDTO);

    Map<String, Object> updateSensorData(String sensorApiKey, String dataId, Map<String, Object> updatedData);

    void deleteSensorData(String sensorApiKey, String dataId);
}
