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

import com.pripelu.backend.entities.Horario;
import com.pripelu.backend.services.HorarioServices;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/horarios")
public class HorarioRestControllers {

    private final HorarioServices horarioService;

    public HorarioRestControllers(HorarioServices horarioService) {
        this.horarioService = horarioService;
    }

    @PostMapping
    public  ResponseEntity<Horario> crearHorario(@RequestBody Horario horario) {
        Horario nuevoHorario = horarioService.crear(horario);
        return ResponseEntity.ok(nuevoHorario);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Horario> obtenerHorarioPorId(@PathVariable Long id) {
        Horario horario = horarioService.obtenerPorId(id);
        return ResponseEntity.ok(horario);
    }
    
    @GetMapping
    public ResponseEntity<List<Horario>> listaHorarios() {
        List<Horario> horarios = horarioService.obtenerTodos();
        return ResponseEntity.ok(horarios);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarHorario(@PathVariable Long id) {
        horarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Horario> actualizarHorario(@PathVariable Long id, @RequestBody Horario horarioActualizado) {
        Horario horario = horarioService.actualizar(id, horarioActualizado);
        return ResponseEntity.ok(horario);
    }
    
}
