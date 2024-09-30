package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.excepcion.LibroNoEncontrado;
import com.tallerwebi.dominio.model.Libro;
import com.tallerwebi.dominio.model.UsuarioLibro;
import com.tallerwebi.infraestructura.service.ServicioLibro;
import com.tallerwebi.dominio.excepcion.ListaVacia;
import com.tallerwebi.dominio.excepcion.QueryVacia;
import com.tallerwebi.infraestructura.service.ServicioUsuarioLibro;
import com.tallerwebi.presentacion.controller.ControladorLibro;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.mockito.Mockito.*;

public class ControladorLibroTest {

    ServicioLibro servicioLibro = mock(ServicioLibro.class);
    ServicioUsuarioLibro servicioUsuarioLibro = mock(ServicioUsuarioLibro.class);
    ControladorLibro controladorLibro = new ControladorLibro(servicioLibro, servicioUsuarioLibro);

    @Test
    public void siLaQueryDeBusquedaContieneTextoLaBusquedaEsExitosa() throws ListaVacia, QueryVacia {
        //given
        givenExistenLibros();
        when(servicioLibro.buscar("amor")).thenReturn(new HashSet<>(
                Set.of(new Libro())));
        //when
        ModelAndView mav = whenBuscarLibro("amor");
        //then
        thenLaBusquedaEsExitosa(mav);
    }

    @Test
    public void siLaQueryDeBusquedaEstaVaciaMuestraError() throws ListaVacia, QueryVacia {
        givenExistenLibros();
        doThrow(QueryVacia.class).when(servicioLibro).buscar("");

        ModelAndView mav = whenBuscarLibro("");

        thenLaBusquedaFalla(mav, "El campo de busqueda esta vacio");
    }

    @Test
    public void siLaListaDeLibrosObtenidosEstaVaciaMuestraError() throws ListaVacia, QueryVacia {
        givenExistenLibros();
        doThrow(ListaVacia.class).when(servicioLibro).buscar("*-,vasda");

        ModelAndView mav = whenBuscarLibro("*-,vasda");

        thenLaBusquedaFalla(mav, "No se encontraron libros que coincidan con la busqueda");
    }

    @Test
    public void siElLibroExisteDevuelveDetalleLibro() throws LibroNoEncontrado {
        // Given
        Long libroId = 1L;
        Libro libro = new Libro();
        when(servicioLibro.obtenerIdLibro(libroId)).thenReturn(libro);
        when(servicioUsuarioLibro.obtenerUsuarioLibro(2L, libroId)).thenReturn(new UsuarioLibro());

        // When
        String vista = controladorLibro.detalleLibro(new ModelMap(), libroId);

        // Then
        assertThat(vista, equalTo("infoLibro"));
    }

    @Test
    public void siElEstadoDeLecturaEsCambiadoExitosamenteRedirigeALaVistaDeDetalle() {
        // Given
        Long libroId = 1L;
        String nuevoEstado = "Leído";

        // When
        String vista = controladorLibro.cambiarEstadoDeLectura(new ModelMap(), libroId, nuevoEstado, new RedirectAttributesModelMap());

        // Then
        assertThat(vista, equalTo("redirect:/libro/resena/" + libroId + "?usuarioId=" + 2L));
    }

    @Test
    public void siElLibroExisteMuestraLaResena() throws LibroNoEncontrado {
        // Given
        Long libroId = 1L;
        Libro libro = new Libro(); // Crea un libro simulado
        when(servicioLibro.obtenerIdLibro(libroId)).thenReturn(libro);

        // When
        String vista = controladorLibro.mostrarResena(new ModelMap(), libroId);

        // Then
        assertThat(vista, equalTo("resenaLibro"));
    }

    @Test
    public void siLaResenaSeGuardaCorrectamenteRedirigeAlDetalleDelLibro() throws LibroNoEncontrado {
        // Given
        Long libroId = 1L;
        Integer puntuacion = 5;
        String reseña = "Excelente libro";

        // When
        String vista = controladorLibro.guardarResena(new ModelMap(), libroId, puntuacion, reseña);

        // Then
        assertThat(vista, equalTo("redirect:/libro/detalle/" + libroId));
    }

    private void givenExistenLibros() {
    }

    private ModelAndView whenBuscarLibro(String query) {
        return controladorLibro.buscarLibros(query);
    }

    private void thenLaBusquedaEsExitosa(ModelAndView mav) {
        assertThat(mav.getViewName(), equalToIgnoringCase("resultados_busqueda"));
    }

    private void thenLaBusquedaFalla(ModelAndView mav, String mensajeError) {
        assertThat(mav.getModel().get("error").toString(), equalToIgnoringCase(mensajeError));
    }
}
