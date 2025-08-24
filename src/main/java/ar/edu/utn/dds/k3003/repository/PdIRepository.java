package ar.edu.utn.dds.k3003.repository;

import ar.edu.utn.dds.k3003.model.PdI;

import java.util.List;
import java.util.Optional;

public interface PdIRepository {
    Optional<PdI> findById(String id);
    List<PdI> findByHechoId(String hechoId);;
    PdI save(PdI pdi);
}