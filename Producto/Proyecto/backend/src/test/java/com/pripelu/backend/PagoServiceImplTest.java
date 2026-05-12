package com.pripelu.backend;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.pripelu.backend.entities.Cita;
import com.pripelu.backend.entities.Pago;
import com.pripelu.backend.repositories.CitaRepository;
import com.pripelu.backend.repositories.PagoRepository;
import com.pripelu.backend.services.PagoServiceImpl;

@ExtendWith(MockitoExtension.class)
public class PagoServiceImplTest {
    
    @Mock
    private PagoRepository pagoRepo;

    @Mock
    private CitaRepository citaRepo;

    @InjectMocks
    private PagoServiceImpl pagoService;


    @Test
    void testCrearPago_Exitoso() {
        // 1. Preparamos los datos
        Cita citaMock = new Cita();
        citaMock.setId(100L);
        
        Pago pagoNuevo = new Pago();
        pagoNuevo.setCita(citaMock);
        pagoNuevo.setMonto(15000);

        // 2. Mokeamos la validación de la cita y el guardado
        when(citaRepo.findById(100L)).thenReturn(Optional.of(citaMock));
        when(pagoRepo.save(any(Pago.class))).thenAnswer(i -> i.getArguments()[0]);

        // 3. Ejecutamos
        Pago resultado = pagoService.crear(pagoNuevo);

        // 4. Verificamos
        assertNotNull(resultado.getFechaPago()); // El service debe setear la fecha actual
        assertEquals("COMPLETADO", resultado.getEstadoPago());
        verify(citaRepo).findById(100L);
    }
}
