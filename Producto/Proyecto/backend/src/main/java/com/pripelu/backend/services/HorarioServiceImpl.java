package com.pripelu.backend.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pripelu.backend.entities.Empleado;
import com.pripelu.backend.entities.Horario;
import com.pripelu.backend.repositories.EmpleadoRepository;
import com.pripelu.backend.repositories.HorarioRepository;

import org.springframework.transaction.annotation.Transactional;

@Service
public class HorarioServiceImpl implements HorarioServices {

    @Autowired
    private HorarioRepository horarioRepo;

    @Autowired
    private EmpleadoRepository empleadoRepo;

    @Override
    @Transactional(readOnly = true)
    public List<Horario> obtenerTodos() {
        return (List<Horario>) horarioRepo.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Horario obtenerPorId(Long id) {
        return horarioRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Horario no encontrado con ID: " + id));
    }

    @Override
    @Transactional
    public Horario crear(Horario horario) {
        if (horario.getEmpleado() != null && horario.getEmpleado().getId() != null) {
            Long empId = horario.getEmpleado().getId();
            Empleado empReal = empleadoRepo.findById(empId)
                    .orElseThrow(() -> new RuntimeException("Empleado no encontrado con ID: " + empId));
            horario.setEmpleado(empReal);
        }

        return horarioRepo.save(horario);
    }

    @Override
    @Transactional
    public Horario actualizar(Long id, Horario actualizado) {
        Horario existente = obtenerPorId(id);
        
        existente.setDiaSemana(actualizado.getDiaSemana());
        existente.setHoraInicio(actualizado.getHoraInicio());
        existente.setHoraInicioAlmuerzo(actualizado.getHoraInicioAlmuerzo());
        existente.setHoraFinAlmuerzo(actualizado.getHoraFinAlmuerzo());
        existente.setHoracierre(actualizado.getHoracierre());

        // Enganchar Empleado real por si decidieran cambiar el horario de un empleado a otro
        if (actualizado.getEmpleado() != null && actualizado.getEmpleado().getId() != null) {
            Long empId = actualizado.getEmpleado().getId();
            Empleado empReal = empleadoRepo.findById(empId)
                    .orElseThrow(() -> new RuntimeException("Empleado no encontrado con ID: " + empId));
            existente.setEmpleado(empReal);
        }

        return horarioRepo.save(existente);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        Horario horario = obtenerPorId(id);
        horarioRepo.delete(horario);
    }

    @Transactional(readOnly = true)
    public List<Horario> obtenerPorEmpleado(Long empleadoId) {
        return horarioRepo.findByEmpleadoId(empleadoId);
    }
}
