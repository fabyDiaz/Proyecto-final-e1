package cl.pfequipo1.proyecto_final.controller;

import cl.pfequipo1.proyecto_final.dto.CompanyDTO;
import cl.pfequipo1.proyecto_final.dto.CompanyRequestDTO;
import cl.pfequipo1.proyecto_final.service.CompanyServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/companies")
public class CompanyController {

    @Autowired
    private CompanyServiceImpl companyService;


    @GetMapping
    public List<CompanyDTO> getAllCompanies() {
        return companyService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompanyDTO> findById(@PathVariable Integer id) {
        CompanyDTO companyDTO = companyService.findById(id);
        return ResponseEntity.ok(companyDTO);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CompanyDTO> create(@RequestBody CompanyRequestDTO companyRequestDTO) {
        CompanyDTO createdCompany = companyService.create(companyRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCompany);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CompanyDTO> updateCompany(@PathVariable Integer id, @RequestBody CompanyRequestDTO companyRequestDTO) {

        CompanyDTO updatedCompany = companyService.update(id, companyRequestDTO);
        return ResponseEntity.ok(updatedCompany);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> delete(@PathVariable Integer id) {
        companyService.delete(id);
        return ResponseEntity.ok("Compañía eliminada"+id);
    }


}

