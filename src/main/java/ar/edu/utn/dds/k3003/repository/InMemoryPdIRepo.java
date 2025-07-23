package ar.edu.utn.dds.k3003.repository;

import ar.edu.utn.dds.k3003.model.PdI;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class InMemoryPdIRepo implements PdIRepository {

    private List<PdI> pdis;

    public InMemoryPdIRepo(){
        this.pdis = new ArrayList<>();
    }

    @Override
    public Optional<PdI> findById(String id) {
        return pdis.stream().filter(p -> p.getId().equals(id)).findFirst();
    }

    @Override
    public List<PdI> findByHecho(String hecho) {
        return pdis.stream()
                .filter(p -> p.getHecho().equals(hecho))
                .toList();
    }

    @Override
    public PdI save(PdI pdi) {
        this.pdis.removeIf(p -> p.getId().equals(pdi.getId()));
        this.pdis.add(pdi);
        return pdi;
    }
}
