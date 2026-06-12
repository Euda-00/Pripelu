package com.pripelu.backend.services;

import java.util.List;

import com.pripelu.backend.entities.Usuario;

public interface UsuarioServices {

    Usuario crear(Usuario usuario);
    Usuario login(String email, String contrasena);
    Usuario obtenerPorId(Long id);
    List<Usuario> obtenerTodos();
    void eliminar(Long id);
    Usuario actualizar(Long id, Usuario usuarioActualizado);
    Usuario obtenerPorEmail(String email);
    
}
