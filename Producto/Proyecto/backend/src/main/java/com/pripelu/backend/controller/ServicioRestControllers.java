package com.pripelu.backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pripelu.backend.entities.Servicio;
import com.pripelu.backend.services.ServicioServices;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/servicios")
public class ServicioRestControllers {
    
    private final ServicioServices servicioService;

    public ServicioRestControllers(ServicioServices servicioServices) {
        this.servicioService = servicioServices;
    }

    @PostMapping
    public  ResponseEntity<Servicio> crearServicio(@RequestBody Servicio servicio) {
        Servicio nuevoServicio = servicioService.crear(servicio);
        return ResponseEntity.ok(nuevoServicio);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Servicio> obtenerServicioPorId(@PathVariable Long id) {
        Servicio servicio = servicioService.obtenerPorId(id);
        return ResponseEntity.ok(servicio);
    }
    
    @GetMapping
    public ResponseEntity<List<Servicio>> listaServicios() {
        List<Servicio> servicios = servicioService.obtenerTodos();
        return ResponseEntity.ok(servicios);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarServicio(@PathVariable Long id) {
        servicioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Servicio> actualizarServicio(@PathVariable Long id, @RequestBody Servicio servicioActualizado) {
        Servicio servicio = servicioService.actualizar(id, servicioActualizado);
        return ResponseEntity.ok(servicio);
    }

}
