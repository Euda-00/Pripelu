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
    citaMock.setId(100L); // Asegúrate de usar el método exacto de tu Cita (ej: setId)
    citaMock.setEstado("PENDIENTE");
    
    // 2. Preparamos el Pago con su nuevo campo obligatorio
    Pago pagoNuevo = new Pago();
    pagoNuevo.setCita(citaMock);
    pagoNuevo.setMonto(15000);
    pagoNuevo.setTipoPago("ABONO"); // <-- ¡ESTO SALVA EL TEST!
    pagoNuevo.setMetodoPago("TRANSF");
    pagoNuevo.setIdTransaccionExacta("TX12345");

    // 3. Mockeamos el comportamiento de los repositorios
    when(citaRepo.findById(100L)).thenReturn(Optional.of(citaMock));
    when(pagoRepo.save(any(Pago.class))).thenAnswer(i -> i.getArguments()[0]);

    // 4. Ejecutamos
    Pago resultado = pagoService.crear(pagoNuevo);

    // 5. Verificaciones
    assertNotNull(resultado.getFechaPago()); 
    assertEquals("COMPLETADO", resultado.getEstadoPago());
    assertEquals("ABONO", resultado.getTipoPago());
    
    // Verificamos que la lógica de negocio cambió el estado de la cita a CONFIRMADA
    assertEquals("CONFIRMADA", citaMock.getEstado()); 
    
    verify(citaRepo).findById(100L);
    verify(pagoRepo).save(any(Pago.class));
    }
}
