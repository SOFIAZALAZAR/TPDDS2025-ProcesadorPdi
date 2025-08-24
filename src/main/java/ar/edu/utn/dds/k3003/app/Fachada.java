package ar.edu.utn.dds.k3003.app;

import ar.edu.utn.dds.k3003.repository.InMemoryPdIRepo;
import ar.edu.utn.dds.k3003.model.PdI;
import ar.edu.utn.dds.k3003.model.ProcesadorPdI;
import ar.edu.utn.dds.k3003.facades.FachadaSolicitudes;
import ar.edu.utn.dds.k3003.facades.FachadaProcesadorPdI;
import ar.edu.utn.dds.k3003.facades.dtos.PdIDTO;
import ar.edu.utn.dds.k3003.repository.PdIRepository;
import lombok.Setter;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class Fachada implements FachadaProcesadorPdI {

    @Setter
    private PdIRepository Repository;
    private FachadaSolicitudes fachadaSolicitudes;

    public Fachada() {
        this.Repository = new InMemoryPdIRepo();
    }

    @Autowired
    public Fachada(PdIRepository pdIRepository) {
        this.Repository= pdIRepository;
    }

    @Override
    public PdIDTO procesar(PdIDTO pdIDTO) throws IllegalStateException {
        ValidacionFachadaSolicitudes(pdIDTO);
        PdI pdI = convertirADomino(pdIDTO);

        //Buscar si ya fue procesado
        List<PdI> pdisPorHecho = this.Repository.findByHechoId(pdI.getHechoId());
        Optional<PdI> yaExistente = pdisPorHecho.stream()
                .filter(existe -> sonIgualesSinId(existe, pdI))
                .findFirst();

        //Si ya existe lo convierte a dto y devuelve el ya existente, sino procesa el PdI nuevo y devuelve ese
        return yaExistente.map(this::convertirADto).orElseGet(() -> procesarNuveoPdI(pdIDTO));
    }

    @Override
    public PdIDTO buscarPdIPorId(String pdiId) throws NoSuchElementException {
        val pdiOptional = this.Repository.findById(pdiId);
        if(pdiOptional.isEmpty()){
            throw new NoSuchElementException(pdiId + " No Existe ");
        }
        val pdi = pdiOptional.get();
        return convertirADto(pdi);
    }

    @Override
    public List<PdIDTO> buscarPorHecho(String hecho) {
        List<PdI> encontrados = this.Repository.findByHechoId(hecho);
        if (encontrados.isEmpty()) {
            throw new NoSuchElementException("No se encontró PdI con hecho: " + hecho);
        }
        return encontrados.stream().map(this::convertirADto).toList();
    }

    @Override
    public void setFachadaSolicitudes(FachadaSolicitudes fachadaSolicitudes) {
        this.fachadaSolicitudes = fachadaSolicitudes;
    }

//Metodos privados para mas claridad
    private void ValidacionFachadaSolicitudes(PdIDTO entrada){
        if (this.fachadaSolicitudes == null) {
            throw new IllegalStateException("FachadaSolicitudes no fue inyectada");
        }
        if (!this.fachadaSolicitudes.estaActivo(entrada.hechoId())) {
            throw new IllegalStateException("La solicitud no está activa");
        }
    }

    private PdIDTO procesarNuveoPdI(PdIDTO entrada){
        ProcesadorPdI procesador = new ProcesadorPdI();
        PdI dominio = convertirADomino(entrada);
        String nuevoId = UUID.randomUUID().toString();
        dominio.setId(nuevoId);
        PdI PdIprocesado = procesador.procesar(dominio);
        this.Repository.save(PdIprocesado);

        return convertirADto(PdIprocesado);
    }

    private boolean sonIgualesSinId(PdI a, PdI b) {
        return a.getHechoId().equals(b.getHechoId()) &&
                a.getDescripcion().equals(b.getDescripcion()) &&
                a.getLugar().equals(b.getLugar()) &&
                a.getMomento().equals(b.getMomento()) &&
                a.getContenido().equals(b.getContenido());
    }

//Metodos privados para omision de logica repetida
    private PdIDTO convertirADto(PdI pdi){
        return new PdIDTO(pdi.getId(), pdi.getHechoId(), pdi.getDescripcion(),
                pdi.getLugar(), pdi.getMomento(),pdi.getContenido(),pdi.getEtiquetas());
    }

    private PdI convertirADomino(PdIDTO pdiDTO){
        return new PdI(pdiDTO.id(), pdiDTO.hechoId(), pdiDTO.descripcion(),
                pdiDTO.lugar(), pdiDTO.momento(),pdiDTO.contenido(),
                new ArrayList<>(pdiDTO.etiquetas()));
    }
}
