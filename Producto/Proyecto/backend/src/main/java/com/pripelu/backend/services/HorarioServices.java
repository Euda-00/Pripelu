package com.pripelu.backend.services;

import java.util.List;

import com.pripelu.backend.entities.Horario;

public interface HorarioServices {
    
    Horario crear(Horario horario);
    Horario obtenerPorId(Long id);
    List<Horario> obtenerTodos();
    void eliminar(Long id);
    Horario actualizar(Long id, Horario horarioActualizado);
}
