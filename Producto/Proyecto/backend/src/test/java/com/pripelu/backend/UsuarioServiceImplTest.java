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

import com.pripelu.backend.entities.Usuario;
import com.pripelu.backend.repositories.UsuarioRepository;
import com.pripelu.backend.services.UsuarioServiceImpl;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceImplTest {

    @Mock
    private UsuarioRepository usuarioRepo;

    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    private Usuario usuarioTest;

    @BeforeEach
    void setUp() {
        usuarioTest = new Usuario();
        usuarioTest.setId(1L);
        usuarioTest.setNombre("Juan");
        usuarioTest.setApellido("Perez");
        usuarioTest.setEmail("juan.perez@example.com");
        usuarioTest.setTelefono(null);
    }

    @Test
    void testCrearUsuario() {
        Usuario usuarioNuevo = new Usuario();
        usuarioNuevo.setNombre("Juan");
        usuarioNuevo.setEmail("juan@correo.com");

        when(usuarioRepo.save(any(Usuario.class))).thenReturn(usuarioTest);

        Usuario resultado = usuarioService.crear(usuarioNuevo);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Juan", resultado.getNombre());

        verify(usuarioRepo, times(1)).save(any(Usuario.class));
    }

    @Test
    void testObtenerId_CuandoUsuarioExiste() {

        when(usuarioRepo.findById(1L)).thenReturn(Optional.of(usuarioTest));

        Usuario resultado = usuarioService.obtenerPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Juan", resultado.getNombre());
        verify(usuarioRepo, times(1)).findById(1L);
    }
    
    @Test
    void testObtenerId_CuandoUsuarioNOExiste() {

        when(usuarioRepo.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            usuarioService.obtenerPorId(99L);
        });

        verify(usuarioRepo, times(1)).findById(99L);
    }

    @Test
    void testObtenerTodosLosUsuarios() {

        Usuario usuario2 = new Usuario();
        usuario2.setId(2L);
        usuario2.setNombre("Ana");
        List<Usuario> listaDeUsuarios = Arrays.asList(usuarioTest, usuario2);
        
        when(usuarioRepo.findAll()).thenReturn(listaDeUsuarios);

        List<Usuario> resultado = usuarioService.obtenerTodos();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(usuarioRepo, times(1)).findAll();
    }

    @Test
    void testActualizarUsuario() {

        Usuario datosNuevos = new Usuario();
        datosNuevos.setNombre("Juanito");
        datosNuevos.setEmail("juanito@nuevo.com");

        when(usuarioRepo.findById(1L)).thenReturn(Optional.of(usuarioTest));

        when(usuarioRepo.save(any(Usuario.class))).thenReturn(usuarioTest);

        Usuario resultado = usuarioService.actualizar(1L, datosNuevos);

        assertNotNull(resultado);
        assertEquals("Juanito", resultado.getNombre());
        assertEquals("juanito@nuevo.com", resultado.getEmail());

        verify(usuarioRepo, times(1)).findById(1L);
        verify(usuarioRepo, times(1)).save(any(Usuario.class));
    }

@Test
    void testEliminarUsuario() {
        Long idDelete = 1L;
        when(usuarioRepo.findById(idDelete)).thenReturn(Optional.of(usuarioTest));
        doNothing().when(usuarioRepo).delete(usuarioTest);
        usuarioService.eliminar(idDelete);
        verify(usuarioRepo, times(1)).findById(idDelete);
        verify(usuarioRepo, times(1)).delete(usuarioTest);
    }
    
}
