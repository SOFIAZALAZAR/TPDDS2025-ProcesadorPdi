package ar.edu.utn.dds.k3003.app;

import ar.edu.utn.dds.k3003.facades.FachadaFuente;
import ar.edu.utn.dds.k3003.facades.FachadaSolicitudes;
import ar.edu.utn.dds.k3003.facades.dtos.EstadoSolicitudBorradoEnum;
import ar.edu.utn.dds.k3003.facades.dtos.SolicitudDTO;

import java.util.List;
import java.util.NoSuchElementException;

public class FakeFachadaSolicitudes implements FachadaSolicitudes {

    @Override
    public SolicitudDTO agregar(SolicitudDTO solicitudDTO) {
        return solicitudDTO;
    }

    @Override
    public SolicitudDTO modificar(String s, EstadoSolicitudBorradoEnum estadoSolicitudBorradoEnum, String s1) throws NoSuchElementException {
        return null;
    }

    @Override
    public List<SolicitudDTO> buscarSolicitudXHecho(String s) {
        return List.of();
    }

    @Override
    public SolicitudDTO buscarSolicitudXId(String s) {
        return null;
    }

    @Override
    public boolean estaActivo(String hechoId) {
        return hechoId.endsWith("0");
    }

    @Override
    public void setFachadaFuente(FachadaFuente fachadaFuente) {
    }
}
