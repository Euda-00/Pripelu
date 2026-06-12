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

import com.pripelu.backend.entities.Inventario;
import com.pripelu.backend.services.InventarioServices;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/inventarios")
public class InventarioRestControllers {
    
    private final InventarioServices inventarioService;

    public InventarioRestControllers(InventarioServices inventarioService) {
        this.inventarioService = inventarioService;
    }

    @PostMapping
    public  ResponseEntity<Inventario> crearInventario(@RequestBody Inventario inventario) {
        Inventario nuevoInventario = inventarioService.crear(inventario);
        return ResponseEntity.ok(nuevoInventario);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Inventario> obtenerInventarioPorId(@PathVariable Long id) {
        Inventario inventario = inventarioService.obtenerPorId(id);
        return ResponseEntity.ok(inventario);
    }
    
    @GetMapping
    public ResponseEntity<List<Inventario>> listaInventarios() {
        List<Inventario> inventarios = inventarioService.obtenerTodos();
        return ResponseEntity.ok(inventarios);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarInventario(@PathVariable Long id) {
        inventarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Inventario> actualizarInventario(@PathVariable Long id, @RequestBody Inventario inventarioActualizado) {
        Inventario inventario = inventarioService.actualizar(id, inventarioActualizado);
        return ResponseEntity.ok(inventario);
    }
}
