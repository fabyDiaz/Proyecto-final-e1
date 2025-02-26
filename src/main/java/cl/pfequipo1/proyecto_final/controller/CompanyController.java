package cl.pfequipo1.proyecto_final.controller;

import cl.pfequipo1.proyecto_final.entity.Company;
import cl.pfequipo1.proyecto_final.repository.CompanyRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/companies")
public class CompanyController {

    private final CompanyRepository companyRepository;

    public CompanyController(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @GetMapping
    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    @PostMapping
    public Company createCompany(@RequestBody Company company) {
        return companyRepository.save(company);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Company> getCompanyById(@PathVariable Integer id) {
        return companyRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Company> updateCompany(@PathVariable Integer id, @RequestBody Company updatedCompany) {
        return companyRepository.findById(id)
                .map(company -> {
                    company.setCompanyName(updatedCompany.getCompanyName());
                    company.setCompanyApiKey(updatedCompany.getCompanyApiKey());
                    return ResponseEntity.ok(companyRepository.save(company));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompany(@PathVariable Integer id) {
        if (companyRepository.existsById(id)) {
            companyRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}

