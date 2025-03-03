package cl.pfequipo1.proyecto_final.service;

import cl.pfequipo1.proyecto_final.dto.LocationDTO;

import java.util.List;

public interface ILocationService {

    public LocationDTO create(LocationDTO locationDTO, String companyApiKey);
    public List<LocationDTO> findAll(String companyApiKey);
    public LocationDTO findById(Integer id, String companyApiKey);
}
