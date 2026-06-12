package com.pripelu.backend.entities;

import java.math.BigDecimal;

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
@Table(name = "Cita_detalles")
public class CitaDetalles {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_detalles")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_cita", nullable = false)
    @JsonIgnoreProperties("detalles")
    private Cita cita;

    @ManyToOne
    @JoinColumn(name = "id_servicio", nullable = false)
    @JsonIgnoreProperties("detalles")
    private Servicio servicio; 

    @Column(name = "Precio_cita", precision = 10, scale = 2, nullable = false)
    private BigDecimal precioCita;
}
