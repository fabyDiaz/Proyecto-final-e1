package cl.pfequipo1.proyecto_final.controller;

import cl.pfequipo1.proyecto_final.entity.Company;
import cl.pfequipo1.proyecto_final.entity.Location;
import cl.pfequipo1.proyecto_final.repository.CompanyRepository;
import cl.pfequipo1.proyecto_final.repository.LocationRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/locations")
public class LocationController {

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private CompanyRepository companyRepository;

    // GET /api/v1/locations?companyApiKey=XXXX
    @GetMapping
    public List<Location> getAllLocations(@RequestParam String companyApiKey) {
        // 1) Validar la existencia de Company con esa API key
        Company company = companyRepository.findByCompanyApiKey(companyApiKey);
        if (company == null) {
            // Podrías retornar 401, 403 o 400, según tu preferencia
            throw new RuntimeException("Company API key inválida");
        }
        // 2) Retornar las locations que quieras: en este ejemplo, devolvemos todas
        //    o podrías filtrar las que pertenezcan a 'company'
        return locationRepository.findAll();
    }

    // GET /api/v1/locations/{id}?companyApiKey=XXXX
    @GetMapping("/{id}")
    public ResponseEntity<Location> getLocationById(@PathVariable Integer id, @RequestParam String companyApiKey) {
        // Validar companyApiKey
        Company company = companyRepository.findByCompanyApiKey(companyApiKey);
        if (company == null) {
            return ResponseEntity.badRequest().build();
        }
        return locationRepository.findById(id)
                .map(location -> ResponseEntity.ok(location))
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /api/v1/locations?companyApiKey=XXXX
    @PostMapping
    public ResponseEntity<Location> createLocation(@RequestParam String companyApiKey, @RequestBody Location location) {
        // 1) Encontrar la Company
        Company company = companyRepository.findByCompanyApiKey(companyApiKey);
        if (company == null) {
            return ResponseEntity.badRequest().build();
        }
        // 2) Asociar la location a la company
        location.setCompany(company);
        Location saved = locationRepository.save(location);
        return ResponseEntity.ok(saved);
    }

    // PUT /api/v1/locations/{id}?companyApiKey=XXXX
    @PutMapping("/{id}")
    public ResponseEntity<Location> updateLocation(@PathVariable Integer id,
                                                  @RequestParam String companyApiKey,
                                                  @RequestBody Location locationDetails) {
        Company company = companyRepository.findByCompanyApiKey(companyApiKey);
        if (company == null) {
            return ResponseEntity.badRequest().build();
        }
        return locationRepository.findById(id)
                .map(location -> {
                    location.setLocationName(locationDetails.getLocationName());
                    location.setLocationCountry(locationDetails.getLocationCountry());
                    location.setLocationCity(locationDetails.getLocationCity());
                    location.setLocationMeta(locationDetails.getLocationMeta());
                    // if you want to allow changing company, do it carefully
                    Location updated = locationRepository.save(location);
                    return ResponseEntity.ok(updated);
                })
                .orElse(ResponseEntity.notFound().build());
    }

 
}


