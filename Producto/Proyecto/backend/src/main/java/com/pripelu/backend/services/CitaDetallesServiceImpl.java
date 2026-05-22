package com.pripelu.backend.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pripelu.backend.entities.Cita;
import com.pripelu.backend.entities.CitaDetalles;
import com.pripelu.backend.entities.Servicio;
import com.pripelu.backend.repositories.CitaDetallesRepository;
import com.pripelu.backend.repositories.CitaRepository;
import com.pripelu.backend.repositories.ServicioRepository;

import org.springframework.transaction.annotation.Transactional;

@Service
public class CitaDetallesServiceImpl implements CitaDetallesServices {

    @Autowired
    private CitaDetallesRepository detallesRepo;

    @Autowired
    private CitaRepository citaRepo;

    @Autowired
    private ServicioRepository servicioRepo;

    @Override
    @Transactional
    public CitaDetalles crear(CitaDetalles citaDetalles) {
        if (citaDetalles.getCita() != null && citaDetalles.getCita().getId() != null) {
            Long citaId = citaDetalles.getCita().getId();
            Cita citaReal = citaRepo.findById(citaId)
                    .orElseThrow(() -> new RuntimeException("No se puede crear el detalle: La cita ID " + citaId + " no existe."));
            citaDetalles.setCita(citaReal);
        } else {
            throw new RuntimeException("El detalle debe estar asociado a una Cita válida.");
        }

        if (citaDetalles.getServicio() != null && citaDetalles.getServicio().getId() != null) {
            Long servicioId = citaDetalles.getServicio().getId();
            Servicio servicioReal = servicioRepo.findById(servicioId)
                    .orElseThrow(() -> new RuntimeException("No se puede crear el detalle: El servicio ID " + servicioId + " no existe."));
            citaDetalles.setServicio(servicioReal);
        } else {
            throw new RuntimeException("El detalle debe estar asociado a un Servicio válido.");
        }

        return detallesRepo.save(citaDetalles);
    }

    @Override
    @Transactional(readOnly = true)
    public CitaDetalles obtenerPorId(Long id) {
        return detallesRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Detalle de cita no encontrado con ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CitaDetalles> obtenerTodos() {
        return (List<CitaDetalles>) detallesRepo.findAll();
    }
    
    // Método extra útil para el historial
    @Transactional(readOnly = true)
    public List<CitaDetalles> obtenerPorCita(Long citaId) {
        return detallesRepo.findByCitaId(citaId);
    }
}
