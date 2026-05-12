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

import com.pripelu.backend.repositories.InsumosUtiRepository;
import com.pripelu.backend.services.InsumosUtiServiceImpl;
import com.pripelu.backend.entities.InsumosUtilizados;

@ExtendWith(MockitoExtension.class)
public class InsumosUtiServiceImplTest {
    
    @Mock
    private InsumosUtiRepository insumoUtiRepo;

    @InjectMocks
    private InsumosUtiServiceImpl insumoUtiService;

    private InsumosUtilizados insumoUtiTest;

    @BeforeEach
    public void setUp() {
        insumoUtiTest = new InsumosUtilizados();
        insumoUtiTest.setId(1L);
        insumoUtiTest.setCantidad(new BigDecimal(0.5));
    }
    @Test
    void testCrearInsumoUti() {
        InsumosUtilizados insumoNuevo = new InsumosUtilizados();

        when(insumoUtiRepo.save(any(InsumosUtilizados.class))).thenReturn(insumoUtiTest);

        InsumosUtilizados resultado = insumoUtiService.crear(insumoNuevo);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals(new BigDecimal(0.5), resultado.getCantidad());

        verify(insumoUtiRepo, times(1)).save(any(InsumosUtilizados.class));
    }

    @Test
    void testObtenerId_CuandoInsumoUtiExiste() {

        when(insumoUtiRepo.findById(1L)).thenReturn(Optional.of(insumoUtiTest));

        InsumosUtilizados resultado = insumoUtiService.obtenerPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals(insumoUtiTest, resultado);
        verify(insumoUtiRepo, times(1)).findById(1L);
    }
    
    @Test
    void testObtenerId_CuandoInsumoUtiNOExiste() {

        when(insumoUtiRepo.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            insumoUtiService.obtenerPorId(99L);
        });

        verify(insumoUtiRepo, times(1)).findById(99L);
    }

    @Test
    void testObtenerTodosLosInsumosUti() {

        InsumosUtilizados insumo2 = new InsumosUtilizados();
        insumo2.setId(2L);
        insumo2.setCantidad(null);
        List<InsumosUtilizados> listaDeInsumos = Arrays.asList(insumoUtiTest, insumo2);

        when(insumoUtiRepo.findAll()).thenReturn(listaDeInsumos);

        List<InsumosUtilizados> resultado = insumoUtiService.obtenerTodos();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(insumoUtiRepo, times(1)).findAll();
    }

    @Test
    void testActualizarInsumoUti() {

        InsumosUtilizados datosNuevos = new InsumosUtilizados();
        datosNuevos.setCantidad(null);

        when(insumoUtiRepo.findById(1L)).thenReturn(Optional.of(insumoUtiTest));

        when(insumoUtiRepo.save(any(InsumosUtilizados.class))).thenReturn(insumoUtiTest);

        InsumosUtilizados resultado = insumoUtiService.actualizar(1L, datosNuevos);

        assertNotNull(resultado);
        assertEquals(null, resultado.getCantidad());

        verify(insumoUtiRepo, times(1)).findById(1L);
        verify(insumoUtiRepo, times(1)).save(any(InsumosUtilizados.class));
    }

    @Test
    void testEliminarInsumoUti() {
        Long idDelete = 1L;
        when(insumoUtiRepo.findById(idDelete)).thenReturn(Optional.of(insumoUtiTest));
        doNothing().when(insumoUtiRepo).delete(insumoUtiTest);
        insumoUtiService.eliminar(idDelete);
        verify(insumoUtiRepo, times(1)).findById(idDelete);
        verify(insumoUtiRepo, times(1)).delete(insumoUtiTest);
    }
}
   