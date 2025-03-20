package cl.pfequipo1.proyecto_final.service;

import cl.pfequipo1.proyecto_final.dto.CompanyAdminViewDTO;
import cl.pfequipo1.proyecto_final.dto.CompanyDTO;
import cl.pfequipo1.proyecto_final.dto.CompanyRequestDTO;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

public interface ICompanyService {

    List<CompanyDTO> findAll();
    CompanyDTO create(CompanyRequestDTO companyRequestDTO);
    CompanyDTO update(Integer id, CompanyRequestDTO companyRequestDTO);
    void delete(Integer id);
    CompanyDTO findById(Integer id);

    @PreAuthorize("hasRole('ADMIN')")
    List<CompanyAdminViewDTO> getAllCompaniesForAdmin();

    @PreAuthorize("hasRole('ADMIN')")
    CompanyAdminViewDTO getCompanyByIdForAdmin(Integer id);
}
