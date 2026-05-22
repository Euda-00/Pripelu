package com.pripelu.backend.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pripelu.backend.entities.Cita;
import com.pripelu.backend.entities.Empleado;
import com.pripelu.backend.entities.Usuario;
import com.pripelu.backend.repositories.CitaRepository;
import com.pripelu.backend.repositories.UsuarioRepository;
import com.pripelu.backend.repositories.EmpleadoRepository;

import org.springframework.transaction.annotation.Transactional;

@Service
public class CitaServiceImpl implements CitaServices {

    @Autowired
    private CitaRepository citaRepo;

    @Autowired
    private UsuarioRepository usuarioRepo;

    @Autowired
    private EmpleadoRepository empleadoRepo;

    @Override
    @Transactional(readOnly = true)
    public List<Cita> obtenerTodos() {
        return (List<Cita>) citaRepo.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Cita obtenerPorId(Long id) {
        return citaRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada con id: " + id));
    }

    @Override
    @Transactional
    public Cita crear(Cita cita) {
        
        if (cita.getDetalles() != null) {
            cita.getDetalles().forEach(detalle -> detalle.setCita(cita));
        }

        if(cita.getEstado() == null) {
            cita.setEstado("PENDIENTE");
        }
        return citaRepo.save(cita);
    }

    @Override
    @Transactional
    public Cita actualizar(Long id, Cita actualizada) {
        Cita citaExistente = citaRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada con ID: " + id));

        if (actualizada.getUsuario() != null && actualizada.getUsuario().getId() != null) {
            Long usuarioId = actualizada.getUsuario().getId();
            Usuario usuarioReal = usuarioRepo.findById(usuarioId)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + usuarioId));
            citaExistente.setUsuario(usuarioReal); 
        }

        if (actualizada.getEmpleado() != null && actualizada.getEmpleado().getId() != null) {
            Long empleadoId = actualizada.getEmpleado().getId();
            Empleado empleadoReal = empleadoRepo.findById(empleadoId)
                    .orElseThrow(() -> new RuntimeException("Empleado no encontrado con ID: " + empleadoId));
            citaExistente.setEmpleado(empleadoReal); 
        }

        citaExistente.setFechaHora(actualizada.getFechaHora());
        citaExistente.setEstado(actualizada.getEstado());

        return citaRepo.save(citaExistente);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        Cita cita = obtenerPorId(id);
        citaRepo.delete(cita);
    }

}