package ar.edu.utn.dds.k3003.controller;

import ar.edu.utn.dds.k3003.app.FakeFachadaSolicitudes;
import ar.edu.utn.dds.k3003.facades.FachadaProcesadorPdI;
import ar.edu.utn.dds.k3003.facades.FachadaSolicitudes;
import ar.edu.utn.dds.k3003.facades.dtos.PdIDTO;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.mockito.Mockito.when;

@RestController
@RequestMapping("/api/pdis")
public class PdiController {

private final FachadaProcesadorPdI fachadaProcesadorPdI;

@Autowired
    public PdiController(FachadaProcesadorPdI fachadaProcesadorPdI) {
        this.fachadaProcesadorPdI = fachadaProcesadorPdI;
    }

@GetMapping
public ResponseEntity<List<PdIDTO>> listarPdis(@RequestParam(required = true) String hecho) {
    return ResponseEntity.ok(fachadaProcesadorPdI.buscarPorHecho(hecho));
}

@GetMapping("/{id}")
public ResponseEntity<PdIDTO> obtenerPdiPorId(@PathVariable String id){
    return ResponseEntity.ok(fachadaProcesadorPdI.buscarPdIPorId(id));
}

    @PostMapping
    public ResponseEntity<PdIDTO> crearPdi(@RequestBody PdIDTO pdi) {
        fachadaProcesadorPdI.setFachadaSolicitudes(new FakeFachadaSolicitudes());
        return ResponseEntity.ok(fachadaProcesadorPdI.procesar(pdi));
    }
}


