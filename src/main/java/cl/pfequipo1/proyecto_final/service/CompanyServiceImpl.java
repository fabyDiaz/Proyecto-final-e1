package cl.pfequipo1.proyecto_final.service;

import cl.pfequipo1.proyecto_final.dto.AdminDTO;
import cl.pfequipo1.proyecto_final.dto.CompanyDTO;
import cl.pfequipo1.proyecto_final.dto.CompanyRequestDTO;
import cl.pfequipo1.proyecto_final.entity.Admin;
import cl.pfequipo1.proyecto_final.entity.Company;
import cl.pfequipo1.proyecto_final.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyServiceImpl implements ICompanyService {
    @Autowired
    CompanyRepository companyRepository;

    @Override
    public List<CompanyDTO> findAll() {
        return companyRepository.findAll().stream().map((Company company)->{
            return new CompanyDTO().builder()
                    .id(company.getId())
                    .companyName(company.getCompanyName())
                    .build();
        }).toList();
    }


    @Override
    public CompanyDTO create(CompanyRequestDTO companyRequestDTO) {
        // Convertir request DTO a entidad
        Company company = Company.builder()
                .companyName(companyRequestDTO.getCompanyName())
                .companyApiKey(companyRequestDTO.getCompanyApiKey())
                .build();

        // Guardar la entidad
        Company savedCompany = companyRepository.save(company);

        // Convertir entidad guardada a DTO
        return CompanyDTO.builder()
                .id(savedCompany.getId())
                .companyName(savedCompany.getCompanyName())
                .build();
    }
}
