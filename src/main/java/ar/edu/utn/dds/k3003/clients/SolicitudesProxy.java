package ar.edu.utn.dds.k3003.clients;

import ar.edu.utn.dds.k3003.facades.FachadaFuente;
import ar.edu.utn.dds.k3003.facades.FachadaSolicitudes;
import ar.edu.utn.dds.k3003.facades.dtos.EstadoSolicitudBorradoEnum;
import ar.edu.utn.dds.k3003.facades.dtos.SolicitudDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.util.List;
import java.util.NoSuchElementException;


@Profile("deploy")
@Service
public class SolicitudesProxy implements FachadaSolicitudes {
    private final String endpoint;
    private final SolicitudesRetrofitClient service;

    public SolicitudesProxy(ObjectMapper objectMapper){
        var env = System.getenv();
        this.endpoint = env.getOrDefault("URL_FUENTE", "https://tpdds2025-solicitudes.onrender.com/");

        var retrofit =
                new Retrofit.Builder()
                        .baseUrl(this.endpoint)
                        .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                        .build();

        this.service = retrofit.create(SolicitudesRetrofitClient.class);
    }


    @Override
    public SolicitudDTO agregar(SolicitudDTO solicitudDTO) {
        return null;
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

    @SneakyThrows
    @Override
    public boolean estaActivo(String id) {
        Response<Boolean> response = service.estaActivo(id).execute();
        if (!response.isSuccessful()) {
            throw new RuntimeException("Error al consultar el estado de la solicitud: " + response.code());
        }

        Boolean activo = response.body();
        if (activo == null) {
            throw new RuntimeException("No se recibió información de la solicitud.");
        }

        if (!activo) {
            throw new IllegalStateException("La solicitud con id " + id + " no está activa.");
        }
        return activo;
    }

    @Override
    public void setFachadaFuente(FachadaFuente fachadaFuente) {

    }
}


