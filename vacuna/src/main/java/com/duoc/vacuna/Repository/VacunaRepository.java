package com.duoc.vacuna.Repository;

import com.duoc.vacuna.Model.VacunaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VacunaRepository extends JpaRepository<VacunaModel, Long> {

    List<VacunaModel> findByIdMascota(Long idMascota);

    List<VacunaModel> findByEstado(String estado);
}