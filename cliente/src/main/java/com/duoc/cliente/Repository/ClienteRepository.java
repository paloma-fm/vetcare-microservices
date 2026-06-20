package com.duoc.cliente.Repository;


import com.duoc.cliente.Model.ClienteModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<ClienteModel, Long > {



Optional<ClienteModel> findByIdAndActivoTrue(Long id);

}
