package com.pripelu.backend.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pripelu.backend.entities.Usuario;
import com.pripelu.backend.repositories.UsuarioRepository;

import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioServiceImpl implements UsuarioServices {
    
    @Autowired
    private UsuarioRepository usuarioRepo;

    @Override
    @Transactional
    public Usuario crear(Usuario usuario) {
        if(usuarioRepo.findByEmail(usuario.getEmail()).isPresent()) {
            throw new RuntimeException("El email ya está registrado");
        }
        //usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));
        
        return usuarioRepo.save(usuario);
    }

    @Override
    @Transactional(readOnly = true)
    public Usuario login(String email, String contrasena) {
        Usuario usuario = usuarioRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Email o contraseña incorrectos"));
                
        if (!usuario.getContrasena().equals(contrasena)) {
            throw new RuntimeException("Email o contraseña incorrectos");
        }
        return usuario;
    }

    @Override
    @Transactional(readOnly = true)
    public Usuario obtenerPorId(Long id) {
        return usuarioRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
    }

    @Override
    @Transactional
    public Usuario actualizar(Long id, Usuario actualizado) {
        Usuario usuarioExistente = obtenerPorId(id); // Reutilizamos el buscar de arriba
        

        usuarioExistente.setNombre(actualizado.getNombre());
        usuarioExistente.setApellido(actualizado.getApellido());
        usuarioExistente.setEmail(actualizado.getEmail());
        usuarioExistente.setTelefono(actualizado.getTelefono());
        
        if (actualizado.getContrasena() != null && !actualizado.getContrasena().isEmpty()) {
            usuarioExistente.setContrasena(actualizado.getContrasena());
        }

        return usuarioRepo.save(usuarioExistente);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        Usuario usuario = obtenerPorId(id);
        usuarioRepo.delete(usuario);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> obtenerTodos() {
        return (List<Usuario>) usuarioRepo.findAll();
    }

    @Override
    public Usuario obtenerPorEmail(String email) {
        return usuarioRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con email: " + email));
    }
}