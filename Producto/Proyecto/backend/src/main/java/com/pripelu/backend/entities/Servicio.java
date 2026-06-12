package com.pripelu.backend.entities;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "Servicio")
public class Servicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_servicio")
    private Long id;

    @Column(name = "Nombre", length = 50, nullable = false)
    private String nombre;

    @OneToMany(mappedBy = "servicio", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("servicio")
    private List<InsumosUtilizados> insumosRequeridos = new ArrayList<>();

    @Lob
    @Column(name = "Descripcion", columnDefinition = "TEXT", nullable = false)
    private String descripcion;

    @Column(name = "Precio", nullable = false)
    private Integer precio;

    @Column(name = "Duracion_min", nullable = false)
    private Integer duracionMinutos;

}
