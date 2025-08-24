package ar.edi.itn.dds.k3003.app;

import ar.edu.utn.dds.k3003.repository.InMemoryPdIRepo;
import ar.edu.utn.dds.k3003.app.Fachada;
import ar.edu.utn.dds.k3003.model.PdI;
import ar.edu.utn.dds.k3003.facades.FachadaProcesadorPdI;
import ar.edu.utn.dds.k3003.facades.FachadaSolicitudes;
import ar.edu.utn.dds.k3003.facades.dtos.PdIDTO;
import ar.edu.utn.dds.k3003.tests.TestTP;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith({MockitoExtension.class})
public class FachadaTest implements TestTP<FachadaProcesadorPdI> {

    private static final String HECHO_EXISTENTE = "Hecho";
    private static final String ID_EXISTENTE_1 = "12";
    private static final String ID_EXISTENTE_2 = "293";
    private static final String ID_INEXISTENTE = "idInexistente";
    private static final String HECHO_INEXISTENTE = "HechoIdInexistente";
    private static final String INFO = "Descripcio: ...";
    private static final String UBICACION = "Argentina";
    private static final String CONTENIDO = "Contenido: ";
    private static final LocalDateTime FECHA = LocalDateTime.now();
    private static final String UN_HECHO_ID = "unHechoId";
    private static final PdIDTO PDI_CON_ID_NO_PROCESADO = new PdIDTO("idCliente123", UN_HECHO_ID, "info", "bsas", LocalDateTime.now(), "1234556", List.of());

    private Fachada fachada;
    private InMemoryPdIRepo repoCompartido;

    @Mock
    FachadaSolicitudes fachadaSolicitudes;

    @BeforeEach
    void setUp() {
        repoCompartido = new InMemoryPdIRepo();

        repoCompartido.save(new PdI(ID_EXISTENTE_1, HECHO_EXISTENTE, INFO, UBICACION, FECHA, CONTENIDO, new ArrayList<>()));
        repoCompartido.save(new PdI(ID_EXISTENTE_2, HECHO_EXISTENTE, INFO, UBICACION, FECHA, CONTENIDO, new ArrayList<>()));

        fachada = new Fachada(); // Sin pasar repo por constructor
        fachada.setRepository(repoCompartido);
        fachada.setFachadaSolicitudes(fachadaSolicitudes);
    }

    @Test
    @DisplayName("Procesar PdI con id enviado no procesado genera nuevo id")
    void testProcesarConIdPeroNoProcesadoGeneraNuevoId() {
        Mockito.when(fachadaSolicitudes.estaActivo(UN_HECHO_ID)).thenReturn(true);

        PdIDTO pdiProcesado = fachada.procesar(PDI_CON_ID_NO_PROCESADO);

        Assertions.assertNotNull(pdiProcesado.id(), "El PdI procesado debería tener un id no nulo");
        Assertions.assertNotEquals(PDI_CON_ID_NO_PROCESADO.id(), pdiProcesado.id(), "El id procesado debe ser diferente al id enviado");
    }

    @Test
    @DisplayName("Buscar por id, te devuelve el Pdi con ese id")
    void testBuscarPorIdExistente() {
        PdIDTO resultado = fachada.buscarPdIPorId(ID_EXISTENTE_1);

        Assertions.assertNotNull(resultado);
        Assertions.assertEquals(ID_EXISTENTE_1, resultado.id());
    }

    @Test
    @DisplayName("Buscar por hecho, te devuelve una lista del tamaño correspondiente")
    void testBuscarPorHechoExistente() {
        List<PdIDTO> resultado = fachada.buscarPorHecho(HECHO_EXISTENTE);

        Assertions.assertNotNull(resultado);
        Assertions.assertEquals(2, resultado.size());
    }

    @Test
    @DisplayName("Buscar por id inexistente, debe lanzar una excepción")
    void testBuscarPorIdInexistente() {
        Assertions.assertThrows(NoSuchElementException.class, () -> {
            fachada.buscarPdIPorId(ID_INEXISTENTE);
        });
    }

    @Test
    @DisplayName("Buscar por hecho inexistente, debe lanzar una excepción")
    void testBuscarPorHechoInexistente() {
        Assertions.assertThrows(NoSuchElementException.class, () -> {
            fachada.buscarPorHecho(HECHO_INEXISTENTE);
        });
    }

    public String paquete() {
        return "ar.edu.utn.dds.k3003.tests.pdi";
    }

    public Class<FachadaProcesadorPdI> clase() {
        return FachadaProcesadorPdI.class;
    }
}