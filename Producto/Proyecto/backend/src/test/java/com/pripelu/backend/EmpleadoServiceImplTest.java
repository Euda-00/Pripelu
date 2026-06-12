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

import com.pripelu.backend.entities.Empleado;
import com.pripelu.backend.repositories.EmpleadoRepository;
import com.pripelu.backend.services.EmpleadoServiceImpl;

@ExtendWith(MockitoExtension.class)
public class EmpleadoServiceImplTest {
    
    @Mock
    private EmpleadoRepository empleadoRepo;

    @InjectMocks
    private EmpleadoServiceImpl empleadoService;

    private Empleado empleadoTest;

    @BeforeEach
    void setUp() {
        empleadoTest = new Empleado();
        empleadoTest.setId(1L);
        empleadoTest.setNombre("Carlos");
        empleadoTest.setApellido("Gomez");
    }

    @Test
    void testCrearEmpleado() {
        Empleado empleadoNuevo = new Empleado();
        empleadoNuevo.setNombre("Ana");
        empleadoNuevo.setApellido("Lopez");

        when(empleadoRepo.save(any(Empleado.class))).thenReturn(empleadoTest);

        Empleado resultado = empleadoService.crear(empleadoNuevo);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Carlos", resultado.getNombre());
        assertEquals("Gomez", resultado.getApellido());

        verify(empleadoRepo, times(1)).save(any(Empleado.class));
    }

    @Test
    void testObtenerId_CuandoEmpleadoExiste() {

        when(empleadoRepo.findById(1L)).thenReturn(Optional.of(empleadoTest));

        Empleado resultado = empleadoService.obtenerPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Carlos", resultado.getNombre());
        assertEquals("Gomez", resultado.getApellido());
        verify(empleadoRepo, times(1)).findById(1L);
    }
    
    @Test
    void testObtenerId_CuandoEmpleadoNOExiste() {

        when(empleadoRepo.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            empleadoService.obtenerPorId(99L);
        });

        verify(empleadoRepo, times(1)).findById(99L);
    }

    @Test
    void testObtenerTodosLosEmpleados() {

        Empleado empleado2 = new Empleado();
        empleado2.setId(2L);
        empleado2.setNombre("Maria");
        empleado2.setApellido("Rodriguez");
        List<Empleado> listaDeEmpleados = Arrays.asList(empleadoTest, empleado2);

        when(empleadoRepo.findAll()).thenReturn(listaDeEmpleados);

        List<Empleado> resultado = empleadoService.obtenerTodos();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(empleadoRepo, times(1)).findAll();
    }

    @Test
    void testActualizarEmpleado() {

        Empleado datosNuevos = new Empleado();
        datosNuevos.setNombre("Ana");
        datosNuevos.setApellido("Lopez");

        when(empleadoRepo.findById(1L)).thenReturn(Optional.of(empleadoTest));

        when(empleadoRepo.save(any(Empleado.class))).thenReturn(empleadoTest);

        Empleado resultado = empleadoService.actualizar(1L, datosNuevos);

        assertNotNull(resultado);
        assertEquals("Ana", resultado.getNombre());
        assertEquals("Lopez", resultado.getApellido());

        verify(empleadoRepo, times(1)).findById(1L);
        verify(empleadoRepo, times(1)).save(any(Empleado.class));
    }

    @Test
    void testEliminarEmpleado() {
        Long idDelete = 1L;
        when(empleadoRepo.findById(idDelete)).thenReturn(Optional.of(empleadoTest));
        doNothing().when(empleadoRepo).delete(empleadoTest);
        empleadoService.eliminar(idDelete);
        verify(empleadoRepo, times(1)).findById(idDelete);
        verify(empleadoRepo, times(1)).delete(empleadoTest);
    }
}