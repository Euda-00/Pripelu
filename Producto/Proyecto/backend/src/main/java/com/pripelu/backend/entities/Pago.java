package com.pripelu.backend.entities;

import java.time.LocalDateTime;

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
@Table(name = "Pago")
public class Pago {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pago")
    private Long idPago;

    @ManyToOne
    @JoinColumn(name = "id_cita", nullable = false)
    @JsonIgnoreProperties("pagos")
    private Cita cita;

    @Column(name = "monto", nullable = false)
    private Integer monto; 

    @Column(name = "fecha_pago", nullable = false)
    private LocalDateTime fechaPago;

    @Column(name = "metodo_pago", length = 20, nullable = false)
    private String metodoPago; 

    @Column(name = "id_transaccion_exacta", length = 50, nullable = false)
    private String idTransaccionExacta; 

    @Column(name = "estado_pago", length = 20, nullable = false)
    private String estadoPago;

    @Column(name = "tipo_pago", length = 30, nullable = false)
    private String tipoPago;
}
