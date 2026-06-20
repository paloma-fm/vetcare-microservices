package com.duoc.mascota.Repository;

import com.duoc.mascota.Model.MascotaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MascotaRepository extends JpaRepository<MascotaModel, Long> {

    // Este reemplaza al tuyo: ahora el orquestador solo recibirá las mascotas vigentes del cliente
    List<MascotaModel> findByClienteIdAndActivoTrue(Long clienteId);

    // Este lo usaremos en el Service para verificar si la mascota existe y está activa antes de editarla o borrarla
    Optional<MascotaModel> findByIdAndActivoTrue(Long id);
}