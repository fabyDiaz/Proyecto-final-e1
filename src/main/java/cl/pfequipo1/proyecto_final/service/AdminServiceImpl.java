package cl.pfequipo1.proyecto_final.service;

import cl.pfequipo1.proyecto_final.dto.AdminDTO;
import cl.pfequipo1.proyecto_final.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class AdminServiceImpl implements IAdminService {

    @Autowired
    AdminRepository adminRepository;

    @Override
    public List<AdminDTO> findAll() {
        return adminRepository.findAll()
                .stream()
                .map(admin -> AdminDTO.builder()
                        .username(admin.getUsername())
                        .role(admin.getRole())
                        .build())
                .toList();
    }

    @Override
    public AdminDTO create(AdminDTO adminDTO) {
       return null;
    }

    @Override
    public AdminDTO update(AdminDTO adminDTO) {
        return null;
    }
}
