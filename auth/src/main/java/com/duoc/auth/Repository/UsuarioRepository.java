package com.duoc.auth.Repository;

import com.duoc.auth.Model.UsuarioModel;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<UsuarioModel, Long> {

    // Spring Data JPA crea la consulta SQL automáticamente solo leyendo el nombre del método
    Optional<UsuarioModel> findByUsername(String username);
}