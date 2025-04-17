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
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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
                    .reading(dataDTO.getReading())
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
                        .reading(data.getReading())
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

        ObjectMapper objectMapper = new ObjectMapper();

        for (Map<String, Object> jsonData : jsonDataList) {
            SensorDataDTO dataDTO = SensorDataDTO.builder()
                    .id(UUID.randomUUID().toString())
                    .sensorId(sensor.getSensorId())
                    .build();
            Integer timestamp = null;
            // Extraer valores de jsonData y mapearlos al DTO
            // Asumimos que los nombres de las propiedades pueden variar según el sensor
            if (jsonData.containsKey("timestamp")) {
                timestamp = convertToInteger(jsonData.get("timestamp"));
            } else if (jsonData.containsKey("time")) {
                timestamp = convertToInteger(jsonData.get("time"));
            }

            try {
                // Convertir el mapa completo a una cadena JSON
                String jsonReading = objectMapper.writeValueAsString(jsonData);

                // Crear el DTO con el JSON completo
                SensorDataDTO dataDTO1 = SensorDataDTO.builder()
                        .id(UUID.randomUUID().toString())
                        .sensorId(sensor.getSensorId())
                        .timeStamp(timestamp)
                        .reading(jsonReading)  // Guardar el JSON completo
                        .build();

                sensorDataList.add(dataDTO1);
            } catch (Exception e) {
                // Manejar excepciones al convertir a JSON
                throw new RuntimeException("Error processing sensor data: " + e.getMessage(), e);
            }
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
    public List<Map<String, Object>> getSensorData(String companyApiKey, Integer fromTimeStamp, Integer toTimeStamp, List<Integer> sensorIds) {
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

        ObjectMapper objectMapper = new ObjectMapper();



        // Convertir los datos a DTOs, procesando el contenido del JSON
        return sensorData.stream()
                .map(data -> {
                    Map<String, Object> resultMap = new HashMap<>();
                    resultMap.put("id", data.getId());
                    resultMap.put("sensorId", data.getSensor().getSensorId());

                    // Procesar el JSON para extraer los datos individuales
                    try {
                        if (data.getReading() != null && !data.getReading().isEmpty()) {
                            JsonNode jsonNode = objectMapper.readTree(data.getReading());

                            // Crear un mapa de datos para almacenar los valores extraídos del JSON
                            Map<String, Object> sensorValues = new HashMap<>();

                            // Procesar todos los campos disponibles en el JSON
                            jsonNode.fields().forEachRemaining(entry -> {
                                sensorValues.put(entry.getKey(), parseJsonValue(entry.getValue()));
                            });

                            // Añadir este mapa como una propiedad adicional en el DTO
                            resultMap.put("sensorValues", sensorValues);
                        }
                    } catch (Exception e) {
                        // Manejar posibles errores al procesar el JSON
                        System.err.println("Error al procesar JSON de lectura: " + e.getMessage());
                        resultMap.put("sensorValues", new HashMap<>());
                    }

                    return resultMap;
                })
                .toList();

    }

    @Override
    @Transactional
    public Map<String, Object> updateSensorData(String sensorApiKey, String dataId, Map<String, Object> updatedData) {
        // Validar que el sensor existe con la API key proporcionada
        Sensor sensor = sensorRepository.findBySensorApiKey(sensorApiKey)
                .orElseThrow(() -> new RuntimeException("Invalid Sensor API Key"));

        // Buscar el dato a actualizar
        SensorData sensorData = sensorDataRepository.findById(dataId)
                .orElseThrow(() -> new RuntimeException("Sensor data not found"));

        // Verificar que el dato pertenece al sensor correcto
        if (!sensorData.getSensor().getSensorId().equals(sensor.getSensorId())) {
            throw new RuntimeException("Sensor data does not belong to the specified sensor");
        }

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // Actualizamos el tiempo si viene en los datos actualizados
            if (updatedData.containsKey("timestamp")) {
                sensorData.setTimeStamp(convertToInteger(updatedData.get("timestamp")));
            } else if (updatedData.containsKey("time")) {
                sensorData.setTimeStamp(convertToInteger(updatedData.get("time")));
            }

            // Convertimos el mapa a JSON y lo guardamos en reading
            String jsonReading = objectMapper.writeValueAsString(updatedData);
            sensorData.setReading(jsonReading);

            // Guardamos los cambios
            SensorData updatedSensorData = sensorDataRepository.save(sensorData);

            // Creamos el mapa de respuesta
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("id", updatedSensorData.getId());
            resultMap.put("sensorId", updatedSensorData.getSensor().getSensorId());

            // Extraemos y añadimos los valores del sensor
            JsonNode jsonNode = objectMapper.readTree(updatedSensorData.getReading());
            Map<String, Object> sensorValues = new HashMap<>();
            jsonNode.fields().forEachRemaining(entry -> {
                sensorValues.put(entry.getKey(), parseJsonValue(entry.getValue()));
            });
            resultMap.put("sensorValues", sensorValues);

            return resultMap;
        } catch (Exception e) {
            throw new RuntimeException("Error updating sensor data: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public void deleteSensorData(String sensorApiKey, String dataId) {
        // Validar que el sensor existe con la API key proporcionada
        Sensor sensor = sensorRepository.findBySensorApiKey(sensorApiKey)
                .orElseThrow(() -> new RuntimeException("Invalid Sensor API Key"));

        // Buscar el dato a eliminar
        SensorData sensorData = sensorDataRepository.findById(dataId)
                .orElseThrow(() -> new RuntimeException("Sensor data not found"));

        // Verificar que el dato pertenece al sensor correcto
        if (!sensorData.getSensor().getSensorId().equals(sensor.getSensorId())) {
            throw new RuntimeException("Sensor data does not belong to the specified sensor");
        }

        // Eliminar el dato
        sensorDataRepository.delete(sensorData);
    }


    // Método auxiliar para convertir valores JsonNode a tipos Java apropiados
    private Object parseJsonValue(JsonNode node) {
        if (node.isNull()) {
            return null;
        } else if (node.isTextual()) {
            return node.asText();
        } else if (node.isInt()) {
            return node.asInt();
        } else if (node.isLong()) {
            return node.asLong();
        } else if (node.isDouble() || node.isFloat()) {
            return node.asDouble();
        } else if (node.isBoolean()) {
            return node.asBoolean();
        } else if (node.isObject() || node.isArray()) {
            return node.toString();
        } else {
            return node.asText();
        }
    }

}
