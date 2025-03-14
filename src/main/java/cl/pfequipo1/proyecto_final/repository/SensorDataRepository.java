package cl.pfequipo1.proyecto_final.repository;

import cl.pfequipo1.proyecto_final.entity.Sensor;
import cl.pfequipo1.proyecto_final.entity.SensorData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SensorDataRepository extends JpaRepository<SensorData, String> {
    List<SensorData> findBySensorAndTimeStampBetween(Sensor sensor, Integer fromTimeStamp, Integer toTimeStamp);

    @Query("SELECT sd FROM SensorData sd WHERE sd.sensor.sensorId IN :sensorIds AND sd.timeStamp BETWEEN :fromTimeStamp AND :toTimeStamp")
    List<SensorData> findBySensorIdsAndTimeStampBetween(
            @Param("sensorIds") List<Integer> sensorIds,
            @Param("fromTimeStamp") Integer fromTimeStamp,
            @Param("toTimeStamp") Integer toTimeStamp);
}