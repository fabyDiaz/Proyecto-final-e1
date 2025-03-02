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

   /* @PostMapping
    public Admin createAdmin(@RequestBody Admin admin) {
        return adminService.save(admin);
    }

    @GetMapping("/{username}")
    public ResponseEntity<Admin> getAdminById(@PathVariable String username) {
        return adminService.findById(username)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{username}")
    public ResponseEntity<Admin> updateAdmin(@PathVariable String username, @RequestBody Admin updatedAdmin) {
        return adminService.findById(username)
                .map(admin -> {
                    admin.setPassword(updatedAdmin.getPassword());
                    return ResponseEntity.ok(adminService.save(admin));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<Void> deleteAdmin(@PathVariable String username) {
        if (adminService.existsById(username)) {
            adminService.deleteById(username);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }*/
}

