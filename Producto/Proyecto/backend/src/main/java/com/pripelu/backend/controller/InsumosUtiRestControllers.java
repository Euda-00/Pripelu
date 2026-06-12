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

import com.pripelu.backend.entities.InsumosUtilizados;
import com.pripelu.backend.services.InsumosUtiServices;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/insumosuti")
public class InsumosUtiRestControllers {
    
    private final InsumosUtiServices insumosUtiService;

    public InsumosUtiRestControllers(InsumosUtiServices insumosUtiService) {
        this.insumosUtiService = insumosUtiService;
    }

    @PostMapping
    public  ResponseEntity<InsumosUtilizados> crearInsumosUti(@RequestBody InsumosUtilizados insumosUti) {
        InsumosUtilizados nuevoInsumosUti = insumosUtiService.crear(insumosUti);
        return ResponseEntity.ok(nuevoInsumosUti);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InsumosUtilizados> obtenerInsumosUtiPorId(@PathVariable Long id) {
        InsumosUtilizados insumosUti = insumosUtiService.obtenerPorId(id);
        return ResponseEntity.ok(insumosUti);
    }
    
    @GetMapping
    public ResponseEntity<List<InsumosUtilizados>> listaInsumosUti() {
        List<InsumosUtilizados> insumosUti = insumosUtiService.obtenerTodos();
        return ResponseEntity.ok(insumosUti);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarInsumosUti(@PathVariable Long id) {
        insumosUtiService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<InsumosUtilizados> actualizarInsumosUti(@PathVariable Long id, @RequestBody InsumosUtilizados insumosUtiActualizado) {
        InsumosUtilizados insumosUti = insumosUtiService.actualizar(id, insumosUtiActualizado);
        return ResponseEntity.ok(insumosUti);
    }
}
