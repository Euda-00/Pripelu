package com.pripelu.backend.repositories;

import org.springframework.data.repository.CrudRepository;

import com.pripelu.backend.entities.Empleado;

public interface EmpleadoRepository extends CrudRepository <Empleado, Long> {
    
}
