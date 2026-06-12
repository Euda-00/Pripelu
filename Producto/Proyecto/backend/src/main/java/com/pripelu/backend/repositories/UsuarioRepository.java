package com.pripelu.backend.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.pripelu.backend.entities.Usuario;

public interface UsuarioRepository extends CrudRepository <Usuario, Long>{
    Optional<Usuario> findByEmail(String email);
}
