package cl.pfequipo1.proyecto_final.service;

import cl.pfequipo1.proyecto_final.dto.SensorDataDTO;
import cl.pfequipo1.proyecto_final.dto.SensorDataRequestDTO;
import cl.pfequipo1.proyecto_final.entity.Company;
import cl.pfequipo1.proyecto_final.entity.Location;
import cl.pfequipo1.proyecto_final.entity.Sensor;
import cl.pfequipo1.proyecto_final.entity.SensorData;
import cl.pfequipo1.proyecto_final.repository.CompanyRepository;
import cl.pfequipo1.proyecto_final.repository.LocationRepository;
import cl.pfequipo1.proyecto_final.repository.SensorDataRepository;
import cl.pfequipo1.proyecto_final.repository.SensorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class SensorDataServiceImpl implements ISensorDataService{

    @Autowired
    private SensorDataRepository sensorDataRepository;

    @Autowired
    private SensorRepository sensorRepository;

    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    LocationRepository locationRepository;


    @Override
    @Transactional
    public List<SensorDataDTO> saveSensorData(String sensorApiKey, List<SensorDataDTO> sensorDataList) {
        // Validar que el sensor existe con la API key proporcionada
        Sensor sensor = sensorRepository.findBySensorApiKey(sensorApiKey)
                .orElseThrow(() -> new RuntimeException("Invalid Sensor API Key"));

        List<SensorData> dataToSave = new ArrayList<>();

        for (SensorDataDTO dataDTO : sensorDataList) {
            // Generar ID único si no se proporciona
            if (dataDTO.getId() == null || dataDTO.getId().isEmpty()) {
                dataDTO.setId(UUID.randomUUID().toString());
            }

            SensorData sensorData = SensorData.builder()
                    .id(dataDTO.getId())
                    .timeStamp(dataDTO.getTimeStamp())
                    .temperature(dataDTO.getTemperature())
                    .voltage(dataDTO.getVoltage())
                    .sensor(sensor)
                    .build();

            dataToSave.add(sensorData);
        }

        // Guardar todos los datos en una sola transacción
        List<SensorData> savedData = sensorDataRepository.saveAll(dataToSave);

        // Convertir los datos guardados a DTOs
        return savedData.stream()
                .map(data -> SensorDataDTO.builder()
                        .id(data.getId())
                        .timeStamp(data.getTimeStamp())
                        .temperature(data.getTemperature())
                        .voltage(data.getVoltage())
                        .sensorId(data.getSensor().getSensorId())
                        .build())
                .toList();
    }

    // Método auxiliar para procesar el formato de JSON enviado por los sensores
    public List<SensorDataDTO> processSensorDataRequest(SensorDataRequestDTO requestDTO) {
        String sensorApiKey = requestDTO.getApi_key();
        List<Map<String, Object>> jsonDataList = requestDTO.getJson_data();

        Sensor sensor = sensorRepository.findBySensorApiKey(sensorApiKey)
                .orElseThrow(() -> new RuntimeException("Invalid Sensor API Key"));

        List<SensorDataDTO> sensorDataList = new ArrayList<>();

        for (Map<String, Object> jsonData : jsonDataList) {
            SensorDataDTO dataDTO = SensorDataDTO.builder()
                    .id(UUID.randomUUID().toString())
                    .sensorId(sensor.getSensorId())
                    .build();

            // Extraer valores de jsonData y mapearlos al DTO
            // Asumimos que los nombres de las propiedades pueden variar según el sensor
            if (jsonData.containsKey("timestamp")) {
                dataDTO.setTimeStamp(convertToInteger(jsonData.get("timestamp")));
            } else if (jsonData.containsKey("time")) {
                dataDTO.setTimeStamp(convertToInteger(jsonData.get("time")));
            }

            if (jsonData.containsKey("temperature") || jsonData.containsKey("temp")) {
                dataDTO.setTemperature(convertToFloat(
                        jsonData.containsKey("temperature") ?
                                jsonData.get("temperature") : jsonData.get("temp")));
            }

            if (jsonData.containsKey("voltage") || jsonData.containsKey("volt")) {
                dataDTO.setVoltage(convertToFloat(
                        jsonData.containsKey("voltage") ?
                                jsonData.get("voltage") : jsonData.get("volt")));
            }

            sensorDataList.add(dataDTO);
        }

        return saveSensorData(sensorApiKey, sensorDataList);
    }

    // Conversiones seguras
    private Integer convertToInteger(Object value) {
        if (value == null) return null;
        if (value instanceof Integer) return (Integer) value;
        if (value instanceof Long) return ((Long) value).intValue();
        if (value instanceof String) return Integer.parseInt((String) value);
        return null;
    }

    private Float convertToFloat(Object value) {
        if (value == null) return null;
        if (value instanceof Float) return (Float) value;
        if (value instanceof Double) return ((Double) value).floatValue();
        if (value instanceof Integer) return ((Integer) value).floatValue();
        if (value instanceof String) return Float.parseFloat((String) value);
        return null;
    }




    @Override
    public List<SensorDataDTO> getSensorData(String companyApiKey, Integer fromTimeStamp, Integer toTimeStamp, List<Integer> sensorIds) {
        // Validar que la compañía existe con la API key proporcionada
        Company company = companyRepository.findByCompanyApiKey(companyApiKey)
                .orElseThrow(() -> new RuntimeException("Invalid Company API Key"));

        // Verificar que los sensores pertenecen a esta compañía
        List<Sensor> companySensors = new ArrayList<>();
        List<Location> locations = locationRepository.findByCompany(company);

        for (Location location : locations) {
            companySensors.addAll(sensorRepository.findByLocation(location));
        }

        // Obtener los IDs de los sensores de la compañía
        List<Integer> companySensorIds = companySensors.stream()
                .map(Sensor::getSensorId)
                .toList();

        // Filtrar para asegurar que solo se consultan sensores que pertenecen a la compañía
        List<Integer> validSensorIds = sensorIds.stream()
                .filter(companySensorIds::contains)
                .toList();

        if (validSensorIds.isEmpty()) {
            return new ArrayList<>();
        }

        // Buscar los datos de los sensores en el rango de tiempo especificado
        List<SensorData> sensorData = sensorDataRepository.findBySensorIdsAndTimeStampBetween(
                validSensorIds, fromTimeStamp, toTimeStamp);

        // Convertir los datos a DTOs
        return sensorData.stream()
                .map(data -> SensorDataDTO.builder()
                        .id(data.getId())
                        .timeStamp(data.getTimeStamp())
                        .temperature(data.getTemperature())
                        .voltage(data.getVoltage())
                        .sensorId(data.getSensor().getSensorId())
                        .build())
                .toList();
    }
}
