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

import com.pripelu.backend.entities.Horario;
import com.pripelu.backend.repositories.HorarioRepository;
import com.pripelu.backend.services.HorarioServiceImpl;

@ExtendWith(MockitoExtension.class)
public class HorarioServiceImplTest {
    
    @Mock
    private HorarioRepository horarioRepo;

    @InjectMocks
    private HorarioServiceImpl horarioService;

    private Horario horarioTest;

    @BeforeEach
    public void setUp() {
        horarioTest = new Horario();
        horarioTest.setId(1L);
        horarioTest.setDiaSemana(2);
        horarioTest.setHoraInicio(null);
        horarioTest.setHoracierre(null);
    }

    @Test
    void testCrearHorario() {
        Horario horarioNuevo = new Horario();      

        when(horarioRepo.save(any(Horario.class))).thenReturn(horarioTest);

        Horario resultado = horarioService.crear(horarioNuevo);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals(2, resultado.getDiaSemana());
        assertEquals(null, resultado.getHoraInicio());
        assertEquals(null, resultado.getHoracierre());

        verify(horarioRepo, times(1)).save(any(Horario.class));
    }

    @Test
    void testObtenerId_CuandoHorarioExiste() {

        when(horarioRepo.findById(1L)).thenReturn(Optional.of(horarioTest));

        Horario resultado = horarioService.obtenerPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals(2, resultado.getDiaSemana());
        assertEquals(null, resultado.getHoraInicio());
        assertEquals(null, resultado.getHoracierre());
        verify(horarioRepo, times(1)).findById(1L);
    }
    
    @Test
    void testObtenerId_CuandoHorarioNOExiste() {

        when(horarioRepo.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            horarioService.obtenerPorId(99L);
        });

        verify(horarioRepo, times(1)).findById(99L);
    }

    @Test
    void testObtenerTodosLosHorarios() {

        Horario horario2 = new Horario();
        horario2.setId(2L);
        horario2.setDiaSemana(3);
        horario2.setHoraInicio(null);
        horario2.setHoracierre(null);
        List<Horario> listaDeHorarios = Arrays.asList(horarioTest, horario2);

        when(horarioRepo.findAll()).thenReturn(listaDeHorarios);

        List<Horario> resultado = horarioService.obtenerTodos();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(horarioRepo, times(1)).findAll();
    }

    @Test
    void testActualizarHorario() {

        Horario datosNuevos = new Horario();
        datosNuevos.setDiaSemana(4);
        datosNuevos.setHoraInicio(null);
        datosNuevos.setHoracierre(null);

        when(horarioRepo.findById(1L)).thenReturn(Optional.of(horarioTest));

        when(horarioRepo.save(any(Horario.class))).thenReturn(horarioTest);

        Horario resultado = horarioService.actualizar(1L, datosNuevos);

        assertNotNull(resultado);
        assertEquals(4, resultado.getDiaSemana());
        assertEquals(null, resultado.getHoraInicio());
        assertEquals(null, resultado.getHoracierre());

        verify(horarioRepo, times(1)).findById(1L);
        verify(horarioRepo, times(1)).save(any(Horario.class));
    }

    @Test
    void testEliminarHorario() {
        Long idDelete = 1L;
        when(horarioRepo.findById(idDelete)).thenReturn(Optional.of(horarioTest));
        doNothing().when(horarioRepo).delete(horarioTest);
        horarioService.eliminar(idDelete);
        verify(horarioRepo, times(1)).findById(idDelete);
        verify(horarioRepo, times(1)).delete(horarioTest);
    }
}