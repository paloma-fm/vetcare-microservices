package com.duoc.atencion.Repository;

import com.duoc.atencion.Model.AtencionModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AtencionRepository extends JpaRepository<AtencionModel, Long> {

    List<AtencionModel> findByIdMascota(Long idMascota);

    List<AtencionModel> findByIdVeterinario(Long idVeterinario);
}