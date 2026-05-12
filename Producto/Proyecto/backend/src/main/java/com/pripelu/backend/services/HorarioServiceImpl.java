package com.pripelu.backend.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pripelu.backend.entities.Horario;
import com.pripelu.backend.repositories.HorarioRepository;

import org.springframework.transaction.annotation.Transactional;

@Service
public class HorarioServiceImpl implements HorarioServices {

    @Autowired
    private HorarioRepository horarioRepo;

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
