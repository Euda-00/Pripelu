package com.pripelu.backend;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.pripelu.backend.entities.Cita;
import com.pripelu.backend.entities.CitaDetalles;
import com.pripelu.backend.entities.Servicio;
import com.pripelu.backend.repositories.CitaDetallesRepository;
import com.pripelu.backend.repositories.CitaRepository;
import com.pripelu.backend.repositories.ServicioRepository;
import com.pripelu.backend.services.CitaDetallesServiceImpl;

@ExtendWith(MockitoExtension.class)
public class CitaDetallesServiceImplTest {

    @Mock
    private CitaDetallesRepository citaDetallesRepo;

    @Mock
    private CitaRepository citaRepo;

    @Mock
    private ServicioRepository servicioRepo;

    @InjectMocks
    private CitaDetallesServiceImpl citaDetallesService;

    private CitaDetalles citaDetallesTest;

    @BeforeEach
    public void setUp() {
        citaDetallesTest = new CitaDetalles();
        citaDetallesTest.setId(1L);
        citaDetallesTest.setPrecioCita(new BigDecimal(10000));
    }

    @Test
    void testCrearCitaDetalles() {
        Cita citaMock = new Cita();
    citaMock.setId(1L);
    
    Servicio servicioMock = new Servicio();
    servicioMock.setId(2L);

    CitaDetalles citaDetallesNuevo = new CitaDetalles();
    citaDetallesNuevo.setCita(citaMock);
    citaDetallesNuevo.setServicio(servicioMock);
    citaDetallesNuevo.setPrecioCita(new BigDecimal("10000"));

    citaDetallesTest.setId(1L);
    citaDetallesTest.setPrecioCita(new BigDecimal("10000"));

    when(citaRepo.findById(1L)).thenReturn(Optional.of(citaMock));
    when(servicioRepo.findById(2L)).thenReturn(Optional.of(servicioMock));
    when(citaDetallesRepo.save(any(CitaDetalles.class))).thenReturn(citaDetallesTest);

    CitaDetalles resultado = citaDetallesService.crear(citaDetallesNuevo);

    assertNotNull(resultado);
    assertEquals(1L, resultado.getId());
    assertEquals(new BigDecimal("10000"), resultado.getPrecioCita());

    verify(citaRepo, times(1)).findById(1L);
    verify(servicioRepo, times(1)).findById(2L);
    verify(citaDetallesRepo, times(1)).save(any(CitaDetalles.class));
    }

    @Test
    void testObtenerId_CuandoCitaDetallesExiste() {

        when(citaDetallesRepo.findById(1L)).thenReturn(Optional.of(citaDetallesTest));

        CitaDetalles resultado = citaDetallesService.obtenerPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals(new BigDecimal(10000), resultado.getPrecioCita());
        verify(citaDetallesRepo, times(1)).findById(1L);
    }
    
    @Test
    void testObtenerId_CuandoCitaDetallesNOExiste() {

        when(citaDetallesRepo.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            citaDetallesService.obtenerPorId(99L);
        });

        verify(citaDetallesRepo, times(1)).findById(99L);
    }

    @Test
    void testObtenerTodosLosCitaDetalles() {

        CitaDetalles citaDetalles2 = new CitaDetalles();
        citaDetalles2.setId(2L);
        citaDetalles2.setPrecioCita(new BigDecimal(15000));
        List<CitaDetalles> listaDeCitaDetalles = Arrays.asList(citaDetallesTest, citaDetalles2);

        when(citaDetallesRepo.findAll()).thenReturn(listaDeCitaDetalles);

        List<CitaDetalles> resultado = citaDetallesService.obtenerTodos();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(citaDetallesRepo, times(1)).findAll();
    }

}
