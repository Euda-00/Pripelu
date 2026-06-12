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

import com.pripelu.backend.entities.Cita;
import com.pripelu.backend.services.CitaServices;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/citas")
public class CitaRestControllers {
    
    private final CitaServices citaService;

    public CitaRestControllers(CitaServices citaService) {
        this.citaService = citaService;
    }

    @PostMapping
    public  ResponseEntity<Cita> crearCita(@RequestBody Cita cita) {
        Cita nuevaCita = citaService.crear(cita);
        return ResponseEntity.ok(nuevaCita);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cita> obtenerCitaPorId(@PathVariable Long id) {
        Cita cita = citaService.obtenerPorId(id);
        return ResponseEntity.ok(cita);
    }
    
    @GetMapping
    public ResponseEntity<List<Cita>> listaCitas() {
        List<Cita> citas = citaService.obtenerTodos();
        return ResponseEntity.ok(citas);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCita(@PathVariable Long id) {
        citaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cita> actualizarCita(@PathVariable Long id, @RequestBody Cita citaActualizada) {
        Cita cita = citaService.actualizar(id, citaActualizada);
        return ResponseEntity.ok(cita);
    }
}
