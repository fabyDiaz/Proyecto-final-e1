package cl.pfequipo1.proyecto_final.repository;

import cl.pfequipo1.proyecto_final.entity.Location;
import cl.pfequipo1.proyecto_final.entity.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SensorRepository  extends JpaRepository<Sensor, Integer> {
    List<Sensor> findByLocation(Location location);
    Optional<Sensor> findBySensorApiKey(String apiKey);
}
