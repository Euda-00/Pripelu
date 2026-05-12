package com.pripelu.backend;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;

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
import com.pripelu.backend.repositories.CitaRepository;
import com.pripelu.backend.services.CitaServiceImpl;

@ExtendWith(MockitoExtension.class)
public class CitaServiceImplTest {

    @Mock
    private CitaRepository citaRepo;

    @InjectMocks
    private CitaServiceImpl citaService;

    private Cita citaTest;
    
    @BeforeEach
    public void setUp() {
        citaTest = new Cita();
        citaTest.setId(1L);
        citaTest.setFechaHora(null);
        citaTest.setDuracionTotal(null);
    }

    @Test
    void testCrearCita() {
        Cita citaNueva = new Cita();
        citaNueva.setFechaHora(null);
        citaNueva.setDuracionTotal(null);

        when(citaRepo.save(any(Cita.class))).thenReturn(citaTest);

        Cita resultado = citaService.crear(citaNueva);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals(null, resultado.getFechaHora());
        assertEquals(null, resultado.getDuracionTotal());

        verify(citaRepo, times(1)).save(any(Cita.class));
    }

    @Test
    void testObtenerId_CuandoCitaExiste() {

        when(citaRepo.findById(1L)).thenReturn(Optional.of(citaTest));

        Cita resultado = citaService.obtenerPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals(null, resultado.getFechaHora());
        assertEquals(null, resultado.getDuracionTotal());
        verify(citaRepo, times(1)).findById(1L);
    }
    
    @Test
    void testObtenerId_CuandoCitaNOExiste() {

        when(citaRepo.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            citaService.obtenerPorId(99L);
        });

        verify(citaRepo, times(1)).findById(99L);
    }

    @Test
    void testObtenerTodosLasCitas() {

        Cita cita2 = new Cita();
        cita2.setId(2L);
        cita2.setFechaHora(null);
        cita2.setDuracionTotal(null);
        List<Cita> listaDeCitas = Arrays.asList(citaTest, cita2);

        when(citaRepo.findAll()).thenReturn(listaDeCitas);

        List<Cita> resultado = citaService.obtenerTodos();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(citaRepo, times(1)).findAll();
    }

    @Test
    void testActualizarCita() {

        Cita datosNuevos = new Cita();
        datosNuevos.setFechaHora(null);
        datosNuevos.setDuracionTotal(null);

        when(citaRepo.findById(1L)).thenReturn(Optional.of(citaTest));

        when(citaRepo.save(any(Cita.class))).thenReturn(citaTest);

        Cita resultado = citaService.actualizar(1L, datosNuevos);

        assertNotNull(resultado);
        assertEquals(null, resultado.getFechaHora());
        assertEquals(null, resultado.getDuracionTotal());

        verify(citaRepo, times(1)).findById(1L);
        verify(citaRepo, times(1)).save(any(Cita.class));
    }

    @Test
    void testEliminarCita() {
        Long idDelete = 1L;
        when(citaRepo.findById(idDelete)).thenReturn(Optional.of(citaTest));
        doNothing().when(citaRepo).delete(citaTest);
        citaService.eliminar(idDelete);
        verify(citaRepo, times(1)).findById(idDelete);
        verify(citaRepo, times(1)).delete(citaTest);
    }
}

