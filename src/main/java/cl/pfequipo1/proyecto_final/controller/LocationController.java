package cl.pfequipo1.proyecto_final.controller;

import cl.pfequipo1.proyecto_final.dto.CompanyDTO;
import cl.pfequipo1.proyecto_final.dto.LocationDTO;
import cl.pfequipo1.proyecto_final.entity.Location;
import cl.pfequipo1.proyecto_final.repository.LocationRepository;
import cl.pfequipo1.proyecto_final.service.LocationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/locations")
public class LocationController {

    @Autowired
    private LocationServiceImpl locationService;

    @PostMapping
    public ResponseEntity<LocationDTO> createLocation(@RequestBody LocationDTO locationDTO, @RequestHeader("company-api-key") String companyApiKey) {

        LocationDTO createdLocation = locationService.create(locationDTO, companyApiKey);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdLocation);
    }

    @GetMapping
    public ResponseEntity<List<LocationDTO>> getAllLocations(@RequestHeader("company-api-key") String companyApiKey) {

        List<LocationDTO> locations = locationService.findAll(companyApiKey);
        return ResponseEntity.ok(locations);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LocationDTO> findById(@PathVariable Integer id, @RequestHeader("company-api-key") String companyApiKey) {
        LocationDTO locationDTO = locationService.findById(id, companyApiKey);
        return ResponseEntity.ok(locationDTO);
    }
}

