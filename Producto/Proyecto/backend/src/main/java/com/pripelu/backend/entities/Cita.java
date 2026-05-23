package com.pripelu.backend.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "Cita")
public class Cita {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_cita")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    @JsonIgnoreProperties("citas")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "id_empleado", nullable = false)
    @JsonIgnoreProperties("citas")
    private Empleado empleado;

    @OneToMany(mappedBy = "cita", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<CitaDetalles> detalles = new ArrayList<>();

    @Column(name = "Fecha_hora", nullable = false)
    private LocalDateTime fechaHora;

    @Column(name = "Duracion_total", nullable = false)
    private Integer duracionTotal;

    @Column(name = "Valor_total", precision = 10, scale = 2, nullable = false)
    private BigDecimal valorTotal;

    @Column(name = "Estado", length = 20, nullable = false)
    private String estado;

    @Column(name = "Notas", columnDefinition = "TEXT")
    private String notas;
}
