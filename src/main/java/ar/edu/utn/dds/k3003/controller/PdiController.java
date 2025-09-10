package ar.edu.utn.dds.k3003.controller;

import ar.edu.utn.dds.k3003.app.FakeFachadaSolicitudes;
import ar.edu.utn.dds.k3003.facades.FachadaProcesadorPdI;
import ar.edu.utn.dds.k3003.facades.dtos.PdIDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
    @RequestMapping("/pdis")
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
        return ResponseEntity.ok(fachadaProcesadorPdI.procesar(pdi));
    }
}


