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

import com.pripelu.backend.entities.Inventario;
import com.pripelu.backend.repositories.InventarioRepository;
import com.pripelu.backend.services.InventarioServiceImpl;

@ExtendWith(MockitoExtension.class)
public class InventarioServiceImplTest {
    
    @Mock
    private InventarioRepository inventarioRepo;

    @InjectMocks
    private InventarioServiceImpl inventarioService;

    private Inventario inventarioTest;

    @BeforeEach
    public void setUp() {
        inventarioTest = new Inventario();
        inventarioTest.setId(1L);
        inventarioTest.setNombre("Producto de Prueba");
        inventarioTest.setStockActual(new BigDecimal(10));
    }

    @Test
    void testCrearInventario() {
        Inventario inventarioNuevo = new Inventario();
        inventarioNuevo.setNombre("Producto de Prueba");
        inventarioNuevo.setStockActual(new BigDecimal(10));

        when(inventarioRepo.save(any(Inventario.class))).thenReturn(inventarioTest);

        Inventario resultado = inventarioService.crear(inventarioNuevo);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Producto de Prueba", resultado.getNombre());
        assertEquals(new BigDecimal(10), resultado.getStockActual());

        verify(inventarioRepo, times(1)).save(any(Inventario.class));
    }

    @Test
    void testObtenerId_CuandoInventarioExiste() {

        when(inventarioRepo.findById(1L)).thenReturn(Optional.of(inventarioTest));

        Inventario resultado = inventarioService.obtenerPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Producto de Prueba", resultado.getNombre());
        assertEquals(new BigDecimal(10), resultado.getStockActual());
        verify(inventarioRepo, times(1)).findById(1L);
    }
    
    @Test
    void testObtenerId_CuandoInventarioNOExiste() {

        when(inventarioRepo.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            inventarioService.obtenerPorId(99L);
        });

        verify(inventarioRepo, times(1)).findById(99L);
    }

    @Test
    void testObtenerTodosLosInventarios() {

        Inventario inventario2 = new Inventario();
        inventario2.setId(2L);
        inventario2.setNombre("Producto de Prueba 2");
        inventario2.setStockActual(new BigDecimal(20));
        List<Inventario> listaDeInventarios = Arrays.asList(inventarioTest, inventario2);
        
        when(inventarioRepo.findAll()).thenReturn(listaDeInventarios);

        List<Inventario> resultado = inventarioService.obtenerTodos();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(inventarioRepo, times(1)).findAll();
    }

    @Test
    void testActualizarInventario() {

        Inventario datosNuevos = new Inventario();
        datosNuevos.setNombre("Producto de Prueba Actualizado");
        datosNuevos.setStockActual(new BigDecimal(30));

        when(inventarioRepo.findById(1L)).thenReturn(Optional.of(inventarioTest));

        when(inventarioRepo.save(any(Inventario.class))).thenReturn(inventarioTest);

        Inventario resultado = inventarioService.actualizar(1L, datosNuevos);

        assertNotNull(resultado);
        assertEquals("Producto de Prueba Actualizado", resultado.getNombre());
        assertEquals(new BigDecimal(30), resultado.getStockActual());

        verify(inventarioRepo, times(1)).findById(1L);
        verify(inventarioRepo, times(1)).save(any(Inventario.class));
    }

@Test
    void testEliminarInventario() {
        Long idDelete = 1L;
        when(inventarioRepo.findById(idDelete)).thenReturn(Optional.of(inventarioTest));
        doNothing().when(inventarioRepo).delete(inventarioTest);
        inventarioService.eliminar(idDelete);
        verify(inventarioRepo, times(1)).findById(idDelete);
        verify(inventarioRepo, times(1)).delete(inventarioTest);
    }

}
