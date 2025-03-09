package cl.pfequipo1.proyecto_final.service;

import cl.pfequipo1.proyecto_final.entity.Sensor;
import cl.pfequipo1.proyecto_final.entity.SensorData;
import cl.pfequipo1.proyecto_final.repository.SensorDataRepository;
import cl.pfequipo1.proyecto_final.repository.SensorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SensorDataServiceImpl implements ISensorDataService{

    @Autowired
    SensorRepository sensorRepository;
    @Autowired
    SensorDataRepository sensorDataRepository;

    public SensorData create(String sensorApiKey, SensorData sensorData) {
        Sensor sensor = sensorRepository.findBySensorApiKey(sensorApiKey)
                .orElseThrow(() -> new RuntimeException("Invalid Sensor API Key"));

        sensorData.setSensor(sensor);
        return sensorDataRepository.save(sensorData);
    }

    public List<SensorData> findAll(String companyApiKey) {
        return null;
    }

    public SensorData findById(Integer id, String companyApiKey) {
        return sensorDataRepository.findById(id)
                .filter(data -> data.getSensor().getLocation().getCompany().getCompanyApiKey().equals(companyApiKey))
                .orElseThrow(() -> new RuntimeException("Sensor Data not found or unauthorized"));
    }

    public SensorData update(Integer id, String companyApiKey, SensorData updatedData) {
        SensorData existingData = findById(id, companyApiKey);

        existingData.setTimeStamp(updatedData.getTimeStamp());
        existingData.setTemperature(updatedData.getTemperature());
        existingData.setVoltage(updatedData.getVoltage());

        return sensorDataRepository.save(existingData);
    }

    public void delete(Integer id, String companyApiKey) {
        SensorData data = findById(id, companyApiKey);
        sensorDataRepository.delete(data);
    }
}
