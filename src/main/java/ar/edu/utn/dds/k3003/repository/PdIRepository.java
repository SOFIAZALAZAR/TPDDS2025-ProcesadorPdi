package ar.edu.utn.dds.k3003.repository;

import ar.edu.utn.dds.k3003.model.PdI;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public interface PdIRepository {
    Optional<PdI> findById(String id);
    List<PdI> findByHecho(String hecho);;
    PdI save(PdI pdi);
}