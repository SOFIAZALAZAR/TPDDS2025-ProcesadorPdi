package ar.edu.utn.dds.k3003.clients;


import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.PathVariable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

@Profile("deploy")
public interface SolicitudesRetrofitClient {
    @GET("solicitudes/{id}/activo")
    Call<Boolean>estaActivo(@Path ("id") String id);

}