package com.pripelu.backend.entities;

import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "Horario")
public class Horario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_horario")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "Id_empleado", nullable = false)
    @JsonIgnoreProperties("horarios")
    private Empleado empleado;

    @Column(name = "Dia_semana", nullable = false)
    private Integer diaSemana;

    @Column(name = "Hora_inicio", nullable = false)
    private LocalTime horaInicio;

    @Column(name = "Hora_in_almuerzo", nullable = false)
    private LocalTime horaInicioAlmuerzo;

    @Column(name = "Hora_fin_almuerzo", nullable = false)
    private LocalTime horaFinAlmuerzo;

    @Column(name = "Hora_cierre", nullable = false)
    private LocalTime horacierre;
}
