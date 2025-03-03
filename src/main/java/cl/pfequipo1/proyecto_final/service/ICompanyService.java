package cl.pfequipo1.proyecto_final.service;

import cl.pfequipo1.proyecto_final.dto.AdminDTO;
import cl.pfequipo1.proyecto_final.dto.CompanyDTO;
import cl.pfequipo1.proyecto_final.dto.CompanyRequestDTO;

import java.util.List;

public interface ICompanyService {

    public List<CompanyDTO> findAll();
    public CompanyDTO create(CompanyRequestDTO companyRequestDTO);
    public CompanyDTO update(Integer id, CompanyRequestDTO companyRequestDTO);
    public void delete(Integer id);
    public CompanyDTO findById(Integer id);
}
