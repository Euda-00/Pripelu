package com.pripelu.backend.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pripelu.backend.entities.Servicio;
import com.pripelu.backend.repositories.ServicioRepository;

import org.springframework.transaction.annotation.Transactional;

@Service
public class ServicioServiceImpl implements ServicioServices {

    @Autowired
    private ServicioRepository servicioRepo;

    @Override
    @Transactional(readOnly = true)
    public List<Servicio> obtenerTodos() {
        return (List<Servicio>) servicioRepo.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Servicio obtenerPorId(Long id) {
        return servicioRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado"));
    }

    @Override
    @Transactional
    public Servicio crear(Servicio servicio) {
        return servicioRepo.save(servicio);
    }

    @Override
    @Transactional
    public Servicio actualizar(Long id, Servicio actualizado) {
        Servicio existente = obtenerPorId(id);
        existente.setNombre(actualizado.getNombre());
        existente.setDescripcion(actualizado.getDescripcion());
        existente.setPrecio(actualizado.getPrecio());
        existente.setDuracionMinutos(actualizado.getDuracionMinutos());
        return servicioRepo.save(existente);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        Servicio servicio = obtenerPorId(id);
        servicioRepo.delete(servicio);
    }
}