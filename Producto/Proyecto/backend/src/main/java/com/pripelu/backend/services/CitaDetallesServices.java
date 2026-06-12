package com.pripelu.backend.services;

import java.util.List;

import com.pripelu.backend.entities.CitaDetalles;

public interface CitaDetallesServices {
    
    CitaDetalles crear(CitaDetalles citaDetalles);
    CitaDetalles obtenerPorId(Long id);
    List<CitaDetalles> obtenerTodos();
}
