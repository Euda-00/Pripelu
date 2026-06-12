package com.pripelu.backend.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.pripelu.backend.entities.InsumosUtilizados;

public interface InsumosUtiRepository extends CrudRepository <InsumosUtilizados, Long>{
    List<InsumosUtilizados> findByServicioId(Long servicioId);
}
