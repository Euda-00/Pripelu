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

import com.pripelu.backend.entities.Servicio;
import com.pripelu.backend.repositories.ServicioRepository;
import com.pripelu.backend.services.ServicioServiceImpl;

@ExtendWith(MockitoExtension.class)
public class ServicioServiceImplTest {

    @Mock
    private ServicioRepository servicioRepo;

    @InjectMocks
    private ServicioServiceImpl servicioService;

    private Servicio servicioTest;
    
    @BeforeEach
    public void setUp() {
        servicioTest = new Servicio();
        servicioTest.setId(1L);
        servicioTest.setNombre("Servicio de Prueba");
        servicioTest.setPrecio(5000);
    }

    @Test
    void testCrearServicio() {
        Servicio servicioNuevo = new Servicio();
        servicioNuevo.setNombre("Servicio de Prueba");
        servicioNuevo.setPrecio(5000);

        when(servicioRepo.save(any(Servicio.class))).thenReturn(servicioTest);

        Servicio resultado = servicioService.crear(servicioNuevo);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Servicio de Prueba", resultado.getNombre());

        verify(servicioRepo, times(1)).save(any(Servicio.class));
    }

    @Test
    void testObtenerId_CuandoServicioExiste() {

        when(servicioRepo.findById(1L)).thenReturn(Optional.of(servicioTest));

        Servicio resultado = servicioService.obtenerPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Servicio de Prueba", resultado.getNombre());
        verify(servicioRepo, times(1)).findById(1L);
    }
    
    @Test
    void testObtenerId_CuandoServicioNOExiste() {

        when(servicioRepo.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            servicioService.obtenerPorId(99L);
        });

        verify(servicioRepo, times(1)).findById(99L);
    }

    @Test
    void testObtenerTodosLosServicios() {

        Servicio servicio2 = new Servicio();
        servicio2.setId(2L);
        servicio2.setNombre("Servicio de Prueba 2");
        servicio2.setPrecio(6000);
        List<Servicio> listaDeServicios = Arrays.asList(servicioTest, servicio2);
        
        when(servicioRepo.findAll()).thenReturn(listaDeServicios);

        List<Servicio> resultado = servicioService.obtenerTodos();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(servicioRepo, times(1)).findAll();
    }

    @Test
    void testActualizarServicio() {

        Servicio datosNuevos = new Servicio();
        datosNuevos.setNombre("Servicio de Prueba Actualizado");
        datosNuevos.setPrecio(7000);

        when(servicioRepo.findById(1L)).thenReturn(Optional.of(servicioTest));

        when(servicioRepo.save(any(Servicio.class))).thenReturn(servicioTest);

        Servicio resultado = servicioService.actualizar(1L, datosNuevos);

        assertNotNull(resultado);
        assertEquals("Servicio de Prueba Actualizado", resultado.getNombre());
        assertEquals(7000, resultado.getPrecio());

        verify(servicioRepo, times(1)).findById(1L);
        verify(servicioRepo, times(1)).save(any(Servicio.class));
    }

    @Test
    void testEliminarServicio() {
        Long idDelete = 1L;
        when(servicioRepo.findById(idDelete)).thenReturn(Optional.of(servicioTest));
        doNothing().when(servicioRepo).delete(servicioTest);
        servicioService.eliminar(idDelete);
        verify(servicioRepo, times(1)).findById(idDelete);
        verify(servicioRepo, times(1)).delete(servicioTest);
    }
}
