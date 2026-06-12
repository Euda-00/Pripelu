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
@Table(name = "Insumos_utilizados")
public class InsumosUtilizados {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; 

    @ManyToOne
    @JoinColumn(name = "id_insumo", nullable = false)
    @JsonIgnoreProperties("insumosUtilizados")
    private Inventario inventario;

    @ManyToOne
    @JoinColumn(name = "id_servicio", nullable = false)
    @JsonIgnoreProperties("insumosUtilizados")
    private Servicio servicio;

    @Column(name = "cantidad", precision = 10, scale = 2, nullable = false)
    private BigDecimal cantidad;
}
