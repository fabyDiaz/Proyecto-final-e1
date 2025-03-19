package cl.pfequipo1.proyecto_final.service;

import cl.pfequipo1.proyecto_final.dto.CompanyAdminViewDTO;
import cl.pfequipo1.proyecto_final.dto.CompanyDTO;
import cl.pfequipo1.proyecto_final.dto.CompanyRequestDTO;
import cl.pfequipo1.proyecto_final.entity.Company;
import cl.pfequipo1.proyecto_final.repository.CompanyRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
    public CompanyDTO findById(Integer id) {
        // Buscar la compañía por ID o lanzar excepción si no existe
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Company not found with ID: " + id));

        // Convertir la entidad a DTO
        return CompanyDTO.builder()
                .id(company.getId())
                .companyName(company.getCompanyName())
                .build();
    }


    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public CompanyDTO create(CompanyRequestDTO companyRequestDTO) {

        // Generar una API key única
        String apiKey = generateUniqueApiKey();

        // Convertir request DTO a entidad
        Company company = Company.builder()
                .companyName(companyRequestDTO.getCompanyName())
                .companyApiKey(apiKey)
                .build();

        // Guardar la entidad
        Company savedCompany = companyRepository.save(company);

        // Convertir entidad guardada a DTO
        return CompanyDTO.builder()
                .id(savedCompany.getId())
                .companyName(savedCompany.getCompanyName())
                .build();
    }

    private String generateUniqueApiKey() {
        // Generar una API key alfanumérica aleatoria de 32 caracteres
        String apiKey = UUID.randomUUID().toString().replace("-", "");

        // Verificar que no exista ya una compañía con esta API key
        while (companyRepository.findByCompanyApiKey(apiKey).isPresent()) {
            apiKey = UUID.randomUUID().toString().replace("-", "");
        }

        return apiKey;
    }

    /**
     * Actualiza solo el nombre de la compañía
     * @param id
     * @param companyRequestDTO
     * @return
     */
    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public CompanyDTO update(Integer id, CompanyRequestDTO companyRequestDTO) {
        // Buscar la compañía por ID
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Company not found with ID: " + id));

        // Actualizar el nombre si se envió
        if (companyRequestDTO.getCompanyName() != null && !companyRequestDTO.getCompanyName().isEmpty()) {
            company.setCompanyName(companyRequestDTO.getCompanyName());
        }

        // Guardar cambios en la base de datos
        Company updatedCompany = companyRepository.save(company);

        // Convertir la entidad actualizada a DTO
        return CompanyDTO.builder()
                .id(updatedCompany.getId())
                .companyName(updatedCompany.getCompanyName())
                .build();
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(Integer id) {
        // Verificar si la compañía existe
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Company not found with ID: " + id));

        // Eliminar la compañía
        companyRepository.delete(company);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public List<CompanyAdminViewDTO> getAllCompaniesForAdmin() {
        List<Company> companies = companyRepository.findAll();
        return companies.stream()
                .map(company -> CompanyAdminViewDTO.builder()
                        .id(company.getId())
                        .companyName(company.getCompanyName())
                        .companyApiKey(company.getCompanyApiKey())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public CompanyAdminViewDTO getCompanyByIdForAdmin(Integer id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Company not found with ID: " + id));

        return CompanyAdminViewDTO.builder()
                .id(company.getId())
                .companyName(company.getCompanyName())
                .companyApiKey(company.getCompanyApiKey())
                .build();
    }
}
