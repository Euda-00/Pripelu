package com.pripelu.backend.services;

import java.util.List;

import com.pripelu.backend.entities.InsumosUtilizados;

public interface InsumosUtiServices {
    
    InsumosUtilizados crear(InsumosUtilizados insumo);
    InsumosUtilizados obtenerPorId(Long id);
    List<InsumosUtilizados> obtenerTodos();
    void eliminar(Long id);
    InsumosUtilizados actualizar(Long id, InsumosUtilizados insumoActualizado);
}
