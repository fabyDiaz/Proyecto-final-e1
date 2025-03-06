package cl.pfequipo1.proyecto_final.service;

import cl.pfequipo1.proyecto_final.dto.LocationDTO;
import cl.pfequipo1.proyecto_final.entity.Company;
import cl.pfequipo1.proyecto_final.entity.Location;
import cl.pfequipo1.proyecto_final.repository.CompanyRepository;
import cl.pfequipo1.proyecto_final.repository.LocationRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public class LocationServiceImpl implements ILocationService{

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public LocationDTO create(LocationDTO locationDTO, String companyApiKey) {
        // Validar que la API key existe
        Company company = companyRepository.findByCompanyApiKey(companyApiKey)
                .orElseThrow(() -> new EntityNotFoundException("Invalid company API key"));

        // Crear la entidad Location
        Location location = Location.builder()
                .locationName(locationDTO.getLocationName())
                .locationCountry(locationDTO.getLocationCountry())
                .locationCity(locationDTO.getLocationCity())
                .locationMeta(locationDTO.getLocationMeta())
                .company(company) // Asignar la compañía encontrada
                .build();

        Location savedLocation = locationRepository.save(location);

        // Convertir a DTO y devolver
        return LocationDTO.builder()
                .locationId(savedLocation.getLocationId())
                .locationName(savedLocation.getLocationName())
                .locationCountry(savedLocation.getLocationCountry())
                .locationCity(savedLocation.getLocationCity())
                .locationMeta(savedLocation.getLocationMeta())
                .companyId(company.getId())
                .build();
    }
}
