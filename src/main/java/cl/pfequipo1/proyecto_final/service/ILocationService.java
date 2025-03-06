package cl.pfequipo1.proyecto_final.service;

import cl.pfequipo1.proyecto_final.dto.LocationDTO;

public interface ILocationService {

    public LocationDTO create(LocationDTO locationDTO, String companyApiKey);
}
