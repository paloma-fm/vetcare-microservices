package com.duoc.veterinario.Repository;

import com.duoc.veterinario.Model.VeterinarioModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VeterinarioRepository extends JpaRepository<VeterinarioModel, Long> {

    Optional<VeterinarioModel> findByCorreo(String correo);
}