package com.pripelu.backend.services;

import java.util.List;

import com.pripelu.backend.entities.Servicio;

public interface ServicioServices {
    
    Servicio crear(Servicio servicio);
    Servicio obtenerPorId(Long id);
    List<Servicio> obtenerTodos();
    void eliminar(Long id);
    Servicio actualizar(Long id, Servicio servicioActualizado);

}
