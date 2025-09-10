package ar.edu.utn.dds.k3003.repository;

import ar.edu.utn.dds.k3003.model.PdI;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Repository
@Profile("local")
public class InMemoryPdIRepo implements PdIRepository {

    private List<PdI> pdis;

    public InMemoryPdIRepo(){
        this.pdis = new ArrayList<>();
    }

    @Override
    public Optional<PdI> findById(Long id) {
        return pdis.stream().filter(p -> p.getId().equals(id)).findFirst();
    }

    @Override
    public List<PdI> findByHechoId(String hechoId) {
        return this.pdis.stream()
                .filter(p -> hechoId.equals(p.getHechoId()))
                .toList();
    }

    @Override
    public PdI save(PdI pdi) {
        this.pdis.add(pdi);
        return pdi;
    }
}
