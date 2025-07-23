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
    private PdIRepository pdIRepository;
    private FachadaSolicitudes fachadaSolicitudes;

    public Fachada() {
        this.pdIRepository = new InMemoryPdIRepo();
    }

    @Autowired
    public Fachada(PdIRepository pdIRepository) {
        this.pdIRepository= pdIRepository;
    }

    @Override
    public PdIDTO procesar(PdIDTO pdIDTO) throws IllegalStateException {
        if (this.fachadaSolicitudes == null) {
            throw new IllegalStateException("FachadaSolicitudes no fue inyectada");
        }
        if (!this.fachadaSolicitudes.estaActivo(pdIDTO.hechoId())) {
            throw new IllegalStateException("La solicitud no está activa");
        }

        PdI pdI = convertirADomino(pdIDTO);

        List<PdI> pdisPorHecho = this.pdIRepository.findByHecho(pdI.getHecho());

        Optional<PdI> yaExistente = pdisPorHecho.stream()
                .filter(existing -> sonIgualesSinId(existing, pdI))
                .findFirst();

        if (yaExistente.isPresent()) {
            return convertirADto(yaExistente.get());
        }

        ProcesadorPdI procesadorPdI = new ProcesadorPdI();

        String nuevoId = UUID.randomUUID().toString();
        pdI.setId(nuevoId);

        PdI procesado = procesadorPdI.procesar(pdI);
        this.pdIRepository.save(procesado);
        return convertirADto(procesado);
    }

    @Override
    public PdIDTO buscarPdIPorId(String pdiId) throws NoSuchElementException {
        val pdiOptional = this.pdIRepository.findById(pdiId);
        if(pdiOptional.isEmpty()){
            throw new NoSuchElementException(pdiId + " No Existe ");
        }
        val pdi = pdiOptional.get();
        return convertirADto(pdi);
    }

    @Override
    public List<PdIDTO> buscarPorHecho(String hecho) {
        List<PdI> encontrados = pdIRepository.findByHecho(hecho);
        if (encontrados.isEmpty()) {
            throw new NoSuchElementException("No se encontró PdI con hecho: " + hecho);
        }
        return encontrados.stream().map(this::convertirADto).toList();
    }

    @Override
    public void setFachadaSolicitudes(FachadaSolicitudes fachadaSolicitudes) {
        this.fachadaSolicitudes = fachadaSolicitudes;
    }

    private PdIDTO convertirADto(PdI pdi){
        return new PdIDTO(pdi.getId(), pdi.getHecho(), pdi.getDescripcion(),
                pdi.getLugar(), pdi.getMomento(),pdi.getContenido(),pdi.getEtiquetas());
    }

    private PdI convertirADomino(PdIDTO pdiDTO){
        return new PdI(pdiDTO.id(), pdiDTO.hechoId(), pdiDTO.descripcion(),
                pdiDTO.lugar(), pdiDTO.momento(),pdiDTO.contenido(),
                new ArrayList<>(pdiDTO.etiquetas()));
    }

    private boolean sonIgualesSinId(PdI a, PdI b) {
        return a.getHecho().equals(b.getHecho()) &&
                a.getDescripcion().equals(b.getDescripcion()) &&
                a.getLugar().equals(b.getLugar()) &&
                a.getMomento().equals(b.getMomento()) &&
                a.getContenido().equals(b.getContenido());
    }
}
