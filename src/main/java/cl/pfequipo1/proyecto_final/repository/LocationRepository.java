package cl.pfequipo1.proyecto_final.repository;

import cl.pfequipo1.proyecto_final.entity.Company;
import cl.pfequipo1.proyecto_final.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LocationRepository extends JpaRepository<Location, Integer> {
    List<Location> findByCompany(Company company);
    boolean existsByLocationNameAndCompanyId(String locationName, Integer companyId);
    Optional<Location> findByLocationIdAndCompany(Integer locationId, Company company);
}
