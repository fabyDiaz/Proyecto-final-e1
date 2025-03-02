package cl.pfequipo1.proyecto_final.repository;

import org.springframework.stereotype.Repository;
import cl.pfequipo1.proyecto_final.entity.SensorData;
import org.springframework.data.jpa.repository.JpaRepository;


@Repository
public interface SensorDataRepository extends JpaRepository<SensorData, Integer> {
}

