package com.pripelu.backend.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.pripelu.backend.entities.Horario;

public interface HorarioRepository extends CrudRepository <Horario, Long>{
    List<Horario> findByEmpleadoId(Long empleadoId);
}
