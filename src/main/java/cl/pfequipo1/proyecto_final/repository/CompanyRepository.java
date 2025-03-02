package cl.pfequipo1.proyecto_final.repository;

import cl.pfequipo1.proyecto_final.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Integer> {
    Company findByCompanyApiKey(String companyApiKey);
}
