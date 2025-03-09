package cl.pfequipo1.proyecto_final.repository;

import cl.pfequipo1.proyecto_final.entity.SensorData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SensorDataRepository extends JpaRepository<SensorData, Integer> {

    Optional<SensorData> findById(Integer integer);

}
