package com.pripelu.backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pripelu.backend.entities.CitaDetalles;
import com.pripelu.backend.services.CitaDetallesServices;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/citasDetalles")
public class CitaDetallesRestControllers {
    
    private final CitaDetallesServices citaDetallesService;

    public CitaDetallesRestControllers(CitaDetallesServices citaDetallesService) {
        this.citaDetallesService = citaDetallesService;
    }

    @PostMapping
    public  ResponseEntity<CitaDetalles> crearCitaDeta(@RequestBody CitaDetalles citaDetalles) {
        CitaDetalles nuevaCitaDeta = citaDetallesService.crear(citaDetalles);
        return ResponseEntity.ok(nuevaCitaDeta);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CitaDetalles> obtenerCitaPorId(@PathVariable Long id) {
        CitaDetalles citaDeta = citaDetallesService.obtenerPorId(id);
        return ResponseEntity.ok(citaDeta);
    }
    
    @GetMapping
    public ResponseEntity<List<CitaDetalles>> listaCitas() {
        List<CitaDetalles> citasDeta = citaDetallesService.obtenerTodos();
        return ResponseEntity.ok(citasDeta);
    }
}
