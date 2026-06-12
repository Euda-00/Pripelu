package com.pripelu.backend.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.pripelu.backend.entities.CitaDetalles;

public interface CitaDetallesRepository extends CrudRepository <CitaDetalles, Long>{
    List<CitaDetalles> findByCitaId(Long citaId);
}
