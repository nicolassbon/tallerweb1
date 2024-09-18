package com.tallerwebi.infraestructura.service;

import com.tallerwebi.dominio.model.Libro;
import com.tallerwebi.dominio.repository.RepositorioLibro;
import com.tallerwebi.infraestructura.service.ServicioLibro;
import com.tallerwebi.dominio.excepcion.ListaVacia;
import com.tallerwebi.dominio.excepcion.QueryVacia;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class ServicioLibroImpl implements ServicioLibro {

    RepositorioLibro repositorioLibro;

    @Autowired
    public ServicioLibroImpl(RepositorioLibro repositorioLibro) {
        this.repositorioLibro = repositorioLibro;
    }

    @Override
    public Set<Libro> buscar(String query) throws QueryVacia, ListaVacia {
        if(query.isEmpty())
           throw new QueryVacia();

        List<Libro> librosObtenidos = repositorioLibro.buscar(query);

        if(librosObtenidos.isEmpty())
            throw new ListaVacia();

        return new HashSet<>(librosObtenidos);
    }
}