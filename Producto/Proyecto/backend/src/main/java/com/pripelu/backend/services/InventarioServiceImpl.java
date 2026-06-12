package com.pripelu.backend.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pripelu.backend.entities.Inventario;
import com.pripelu.backend.repositories.InventarioRepository;

import org.springframework.transaction.annotation.Transactional;

@Service
public class InventarioServiceImpl implements InventarioServices {

    @Autowired
    private InventarioRepository inventarioRepo;

    @Override
    @Transactional(readOnly = true)
    public List<Inventario> obtenerTodos() {
        return (List<Inventario>) inventarioRepo.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Inventario obtenerPorId(Long id) {
        return inventarioRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado en inventario"));
    }

    @Override
    @Transactional
    public Inventario crear(Inventario inventario) {
        return inventarioRepo.save(inventario);
    }

    @Override
    @Transactional
    public Inventario actualizar(Long id, Inventario actualizado) {
        Inventario existente = obtenerPorId(id);
        existente.setNombre(actualizado.getNombre());
        existente.setStockActual(actualizado.getStockActual());
        return inventarioRepo.save(existente);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        Inventario inventario = obtenerPorId(id); 
        inventarioRepo.delete(inventario);
    }
}