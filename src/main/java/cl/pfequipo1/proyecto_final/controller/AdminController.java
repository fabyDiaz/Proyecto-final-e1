package cl.pfequipo1.proyecto_final.controller;

import cl.pfequipo1.proyecto_final.entity.Admin;
import cl.pfequipo1.proyecto_final.repository.AdminRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admins")
public class AdminController {

    private final AdminRepository adminRepository;

    public AdminController(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    @GetMapping
    public List<Admin> getAllAdmins() {
        return adminRepository.findAll();
    }

    @PostMapping
    public Admin createAdmin(@RequestBody Admin admin) {
        return adminRepository.save(admin);
    }

    @GetMapping("/{username}")
    public ResponseEntity<Admin> getAdminById(@PathVariable String username) {
        return adminRepository.findById(username)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{username}")
    public ResponseEntity<Admin> updateAdmin(@PathVariable String username, @RequestBody Admin updatedAdmin) {
        return adminRepository.findById(username)
                .map(admin -> {
                    admin.setPassword(updatedAdmin.getPassword());
                    return ResponseEntity.ok(adminRepository.save(admin));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<Void> deleteAdmin(@PathVariable String username) {
        if (adminRepository.existsById(username)) {
            adminRepository.deleteById(username);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}

