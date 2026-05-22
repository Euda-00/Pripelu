package com.pripelu.backend.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pripelu.backend.entities.InsumosUtilizados;
import com.pripelu.backend.entities.Inventario;
import com.pripelu.backend.entities.Servicio;
import com.pripelu.backend.repositories.InsumosUtiRepository;
import com.pripelu.backend.repositories.InventarioRepository;
import com.pripelu.backend.repositories.ServicioRepository;

import org.springframework.transaction.annotation.Transactional;

@Service
public class InsumosUtiServiceImpl implements InsumosUtiServices {

    @Autowired
    private InsumosUtiRepository insumosUtiRepo;

    @Autowired
    private ServicioRepository servicioRepo; // Necesario para validar el servicio antes de asignar insumos

    @Autowired
    private InventarioRepository inventarioRepo; // Necesario para validar el inventario antes de asignar insumos

    @Override
    @Transactional
    public InsumosUtilizados crear(InsumosUtilizados insumosUtilizados) {
        if (insumosUtilizados.getInventario() != null && insumosUtilizados.getInventario().getId() != null) {
            Long invId = insumosUtilizados.getInventario().getId();
            Inventario invReal = inventarioRepo.findById(invId)
                    .orElseThrow(() -> new RuntimeException("Inventario no encontrado con ID: " + invId));
            insumosUtilizados.setInventario(invReal);
        }

        if (insumosUtilizados.getServicio() != null && insumosUtilizados.getServicio().getId() != null) {
            Long serId = insumosUtilizados.getServicio().getId();
            Servicio serReal = servicioRepo.findById(serId)
                    .orElseThrow(() -> new RuntimeException("Servicio no encontrado con ID: " + serId));
            insumosUtilizados.setServicio(serReal);
        }

        return insumosUtiRepo.save(insumosUtilizados);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InsumosUtilizados> obtenerTodos() {
        return (List<InsumosUtilizados>) insumosUtiRepo.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public InsumosUtilizados obtenerPorId(Long id) {
        return insumosUtiRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Registro de insumo no encontrado con ID: " + id));
    }

    @Override
    @Transactional
    public InsumosUtilizados actualizar(Long id, InsumosUtilizados actualizado) {
        InsumosUtilizados existente = obtenerPorId(id);

        existente.setCantidad(actualizado.getCantidad());

        if (actualizado.getInventario() != null && actualizado.getInventario().getId() != null) {
            Long invId = actualizado.getInventario().getId();
            Inventario invReal = inventarioRepo.findById(invId)
                    .orElseThrow(() -> new RuntimeException("Inventario no encontrado con ID: " + invId));
            existente.setInventario(invReal);
        }

        if (actualizado.getServicio() != null && actualizado.getServicio().getId() != null) {
            Long serId = actualizado.getServicio().getId();
            Servicio serReal = servicioRepo.findById(serId)
                    .orElseThrow(() -> new RuntimeException("Servicio no encontrado con ID: " + serId));
            existente.setServicio(serReal);
        }

        return insumosUtiRepo.save(existente);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        InsumosUtilizados registro = obtenerPorId(id);
        insumosUtiRepo.delete(registro);
    }

    @Transactional(readOnly = true)
    public List<InsumosUtilizados> obtenerPorServicio(Long servicioId) {
        return insumosUtiRepo.findByServicioId(servicioId);
        // Esto permitira ver todos los insumos asociados a un servicio específico, lo que puede ser util para le font
    }
}
