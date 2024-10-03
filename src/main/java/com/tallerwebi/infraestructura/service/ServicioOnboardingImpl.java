package com.tallerwebi.infraestructura.service;

import com.tallerwebi.dominio.model.Autor;
import com.tallerwebi.dominio.model.Genero;
import com.tallerwebi.dominio.model.Libro;
import com.tallerwebi.dominio.model.Usuario;
import com.tallerwebi.dominio.repository.RepositorioUsuario;
import com.tallerwebi.infraestructura.repository.RepositorioOnboardingImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class ServicioOnboardingImpl implements ServicioOnboarding {

    private final RepositorioOnboardingImpl repositorioOnboarding;
    private RepositorioUsuario repositorioUsuario;

    @Autowired
    public ServicioOnboardingImpl(RepositorioUsuario repositorioUsuario, RepositorioOnboardingImpl repositorioOnboarding) {
        this.repositorioUsuario = repositorioUsuario;
        this.repositorioOnboarding = repositorioOnboarding;
    }

    @Override
    public List<Genero> obtenerGeneros() {
        return repositorioOnboarding.obtenerGeneros();
    }

    @Override
    @Transactional
    public void guardarGeneros(Long usuarioId, List<Long> generos) {
        try {
            Usuario usuario = repositorioUsuario.buscarUsuarioPorId(usuarioId);
            for (Long generoId : generos) {
                Genero genero = repositorioOnboarding.obtenerGeneroPorId(generoId);
                usuario.setGenero(genero);
            }
        } catch (Exception e) {

            System.out.println("Error al guardar géneros: " + e.getMessage());
        }
    }

}