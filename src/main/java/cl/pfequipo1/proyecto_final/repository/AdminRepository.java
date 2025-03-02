package cl.pfequipo1.proyecto_final.repository;

import cl.pfequipo1.proyecto_final.entity.Admin;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;




@Repository

public interface AdminRepository extends JpaRepository<Admin, Integer> {

	Optional<Admin> findById(String username);

	boolean existsById(String username);

	void deleteById(String username);
}


