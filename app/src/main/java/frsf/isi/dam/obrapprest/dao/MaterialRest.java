package frsf.isi.dam.obrapprest.dao;

import java.util.List;

import frsf.isi.dam.obrapprest.modelo.Material;
import frsf.isi.dam.obrapprest.modelo.Obra;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface MaterialRest {

    @GET("material/")
    Call<List<Material>> buscarTodas();

    @DELETE("material/{id}")
    Call<Void> borrar(@Path("id") Integer id);

    @PUT("material/{id}")
    Call<Material> actualizar(@Path("id") Integer id, @Body Material material);

    @POST("material/")
    Call<Material> crear(@Body Material material);
}
