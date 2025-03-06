package cl.pfequipo1.proyecto_final.service;

import cl.pfequipo1.proyecto_final.dto.AdminDTO;

import java.util.List;

public interface IAdminService {
    public List<AdminDTO> findAll();
   // public AdminDTO findById(Integer idCurso);
    public AdminDTO create(AdminDTO adminDTO);
    public AdminDTO update(AdminDTO adminDTO);
    //public AdminDTO deleteById(Integer idCurso);
}
