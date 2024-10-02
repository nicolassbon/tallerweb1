package com.tallerwebi.dominio.repository;

import com.tallerwebi.dominio.model.Usuario;

import java.time.LocalDate;
import java.util.List;

public interface RepositorioUsuario {

    Usuario buscarUsuario(String email, String password);
    void guardar(String email, String password, String nombreUsuario, String nombre, LocalDate fechaNacimiento);
    void modificar(Usuario usuario);
    Usuario buscar(String email);
    Usuario buscarUsuarioPorId(Long id);
    void guardarUsuario(Usuario usuario);
    void guardarTokenDeRecuperacion(Usuario usuario, String token);
    void guardarGeneros(Long usuarioId, List<Long> generos);
    void guardarUsuarioOnboarding(Usuario usuario);
}

