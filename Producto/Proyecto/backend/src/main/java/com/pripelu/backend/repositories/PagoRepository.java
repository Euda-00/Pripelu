package com.pripelu.backend.repositories;

import org.springframework.data.repository.CrudRepository;

import com.pripelu.backend.entities.Pago;

public interface PagoRepository extends CrudRepository <Pago, Long> {
    
}
