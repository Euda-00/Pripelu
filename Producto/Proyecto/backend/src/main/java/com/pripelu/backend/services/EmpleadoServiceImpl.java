package com.pripelu.backend.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pripelu.backend.entities.Empleado;
import com.pripelu.backend.repositories.EmpleadoRepository;

import org.springframework.transaction.annotation.Transactional;

@Service
public class EmpleadoServiceImpl implements EmpleadoServices {

    @Autowired
    private EmpleadoRepository empleadoRepo;

    @Override
    @Transactional(readOnly = true)
    public List<Empleado> obtenerTodos() {
        return (List<Empleado>) empleadoRepo.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Empleado obtenerPorId(Long id) {
        return empleadoRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado"));
    }

    @Override
    @Transactional
    public Empleado crear(Empleado empleado) {
        return empleadoRepo.save(empleado);
    }

    @Override
    @Transactional
    public Empleado actualizar(Long id, Empleado actualizado) {
        Empleado existente = obtenerPorId(id);
        existente.setNombre(actualizado.getNombre());
        existente.setApellido(actualizado.getApellido());
        existente.setEspecialidad(actualizado.getEspecialidad());
        // No olvides actualizar los campos específicos que tengas en tu entidad
        return empleadoRepo.save(existente);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        Empleado empleado = obtenerPorId(id);
        empleadoRepo.delete(empleado);
    }
}
