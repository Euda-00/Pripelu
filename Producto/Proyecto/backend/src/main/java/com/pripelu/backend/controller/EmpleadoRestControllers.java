package com.pripelu.backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pripelu.backend.entities.Empleado;
import com.pripelu.backend.services.EmpleadoServices;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/empleado")
public class EmpleadoRestControllers {

    private final EmpleadoServices empleadoService;

    public EmpleadoRestControllers(EmpleadoServices empleadoService) {
        this.empleadoService = empleadoService;
    }

    @PostMapping
    public  ResponseEntity<Empleado> crearEmpleado(@RequestBody Empleado empleado) {
        Empleado nuevoEmpleado = empleadoService.crear(empleado);
        return ResponseEntity.ok(nuevoEmpleado);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Empleado> obtenerEmpleadoPorId(@PathVariable Long id) {
        Empleado empleado = empleadoService.obtenerPorId(id);
        return ResponseEntity.ok(empleado);
    }
    
    @GetMapping
    public ResponseEntity<List<Empleado>> listaEmpleados() {
        List<Empleado> empleados = empleadoService.obtenerTodos();
        return ResponseEntity.ok(empleados);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarEmpleado(@PathVariable Long id) {
        empleadoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Empleado> actualizarEmpleado(@PathVariable Long id, @RequestBody Empleado empleadoActualizado) {
        Empleado empleado = empleadoService.actualizar(id, empleadoActualizado);
        return ResponseEntity.ok(empleado);
    }
    
}
