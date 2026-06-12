package com.pripelu.backend.repositories;

import org.springframework.data.repository.CrudRepository;

import com.pripelu.backend.entities.Inventario;

public interface InventarioRepository extends CrudRepository <Inventario, Long>{
    
}
