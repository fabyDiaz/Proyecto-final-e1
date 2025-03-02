package cl.pfequipo1.proyecto_final.repository;

import cl.pfequipo1.proyecto_final.entity.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SensorRepository extends JpaRepository<Sensor, Integer> {
    Sensor findBySensorApiKey(String apiKey);
}
