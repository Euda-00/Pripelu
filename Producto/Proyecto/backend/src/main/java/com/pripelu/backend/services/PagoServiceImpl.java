package com.pripelu.backend.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pripelu.backend.entities.Cita;
import com.pripelu.backend.entities.Pago;
import com.pripelu.backend.repositories.CitaRepository;
import com.pripelu.backend.repositories.PagoRepository;

import org.springframework.transaction.annotation.Transactional;

@Service
public class PagoServiceImpl implements PagoServices {

    @Autowired
    private PagoRepository pagoRepo;

    @Autowired
    private CitaRepository citaRepo;

    @Override
    @Transactional
    public Pago crear(Pago pago) {
        Long citaId = pago.getCita().getId();
        Cita cita = citaRepo.findById(citaId)
                .orElseThrow(() -> new RuntimeException("No se puede registrar pago: La cita ID " + citaId + " no existe."));
        pago.setCita(cita);

        pago.setFechaPago(LocalDateTime.now());
        if (pago.getEstadoPago() == null) {
            pago.setEstadoPago("COMPLETADO");
        }

        if (pago.getTipoPago() != null) {
            String tipo = pago.getTipoPago().toUpperCase();
            
            if (tipo.equals("ABONO")) {
                cita.setEstado("CONFIRMADA");
            } else if (tipo.equals("TOTAL")) {
                cita.setEstado("PAGADA");
            }
            
        } else {
            throw new RuntimeException("El tipo de pago (ABONO o TOTAL) es obligatorio.");
        }

        return pagoRepo.save(pago);
    }

    @Override
    @Transactional(readOnly = true)
    public Pago obtenerPorId(Long id) { 
        return pagoRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado con ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pago> obtenerTodos() {
        return (List<Pago>) pagoRepo.findAll();
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        Pago pago = obtenerPorId(id);
        pagoRepo.delete(pago);
    }
}