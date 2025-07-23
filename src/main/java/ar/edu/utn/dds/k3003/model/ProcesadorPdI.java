package ar.edu.utn.dds.k3003.model;

public class ProcesadorPdI {

    public PdI procesar(PdI pdI) {
        pdI.agregarEtiqueta();
        pdI.setFueProcesado();
        return pdI;
    }
}
