package cl.pfequipo1.proyecto_final.controller;

import cl.pfequipo1.proyecto_final.dto.AdminDTO;
import cl.pfequipo1.proyecto_final.service.AdminServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admins")
public class AdminController {

    @Autowired
    private AdminServiceImpl adminService;

    @GetMapping
    public List<AdminDTO> findAll() {
        return adminService.findAll();
    }

}

