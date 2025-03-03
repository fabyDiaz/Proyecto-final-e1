package cl.pfequipo1.proyecto_final.service;

import cl.pfequipo1.proyecto_final.dto.CompanyDTO;
import cl.pfequipo1.proyecto_final.dto.LocationDTO;
import cl.pfequipo1.proyecto_final.entity.Company;
import cl.pfequipo1.proyecto_final.entity.Location;
import cl.pfequipo1.proyecto_final.repository.CompanyRepository;
import cl.pfequipo1.proyecto_final.repository.LocationRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Override
    public List<LocationDTO> findAll(String companyApiKey) {
        // Validar que la API key existe
        Company company = companyRepository.findByCompanyApiKey(companyApiKey)
                .orElseThrow(() -> new RuntimeException("Invalid API Key"));

        List<Location> locations = locationRepository.findByCompany(company);

        return locations.stream()
                .map(location -> LocationDTO.builder()
                        .locationId(location.getLocationId())
                        .locationName(location.getLocationName())
                        .locationCountry(location.getLocationCountry())
                        .locationCity(location.getLocationCity())
                        .locationMeta(location.getLocationMeta())
                        .build())
                .toList();
    }

    @Override
    public LocationDTO findById(Integer id, String companyApiKey) {
        // Buscar la compañía por ID o lanzar excepción si no existe
        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Location not found with ID: " + id));

        Company company = companyRepository.findByCompanyApiKey(companyApiKey)
                .orElseThrow(() -> new EntityNotFoundException("Invalid company API key"));

        // Convertir la entidad a DTO
        return LocationDTO.builder()
                .locationId(location.getLocationId())
                .locationName(location.getLocationName())
                .locationCountry(location.getLocationCountry())
                .locationCity(location.getLocationCity())
                .locationMeta(location.getLocationMeta())
                .companyId(company.getId())
                .build();
    }
}
