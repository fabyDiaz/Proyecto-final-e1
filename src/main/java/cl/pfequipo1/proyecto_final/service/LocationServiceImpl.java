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

        // Validar que los campos obligatorios del DTO no sean nulos
        if (locationDTO.getLocationName() == null || locationDTO.getLocationName().isEmpty()) {
            throw new IllegalArgumentException("Location name is required");
        }

        // Crear la entidad Location
       Location location = Location.builder()
                .locationName(locationDTO.getLocationName())
                .locationCountry(locationDTO.getLocationCountry())
                .locationCity(locationDTO.getLocationCity())
                .locationMeta(locationDTO.getLocationMeta())
                .company(company) // Asignar la compañía encontrada
                .build();

        System.out.println(location.getLocationId()+ " - " + location.getLocationName());
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
                        .companyId(company.getId())
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

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(Integer id, String companyApiKey) {

        Company company = companyRepository.findByCompanyApiKey(companyApiKey)
                .orElseThrow(() -> new EntityNotFoundException("Invalid company API key"));
        // Verificar si la compañía existe
        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Company not found with ID: " + id));

        // Eliminar la compañía
        locationRepository.delete(location);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public LocationDTO update(Integer id, LocationDTO locationDTO, String companyApiKey) {

        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Location not found"));

        // Verificar API Key de la compañía
        if (!location.getCompany().getCompanyApiKey().equals(companyApiKey)) {
            throw new RuntimeException("Unauthorized: Invalid company API key");
        }

        // Actualizar campos
        location.setLocationName(locationDTO.getLocationName());
        location.setLocationCountry(locationDTO.getLocationCountry());
        location.setLocationCity(locationDTO.getLocationCity());
        location.setLocationMeta(locationDTO.getLocationMeta());

        // Guardar cambios
        Location updatedLocation = locationRepository.save(location);

        // Retornar DTO actualizado
        return new LocationDTO(
                updatedLocation.getLocationId(),
                updatedLocation.getLocationName(),
                updatedLocation.getLocationCountry(),
                updatedLocation.getLocationCity(),
                updatedLocation.getLocationMeta()
        );
    }
}
