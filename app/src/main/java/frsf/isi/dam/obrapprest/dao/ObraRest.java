package frsf.isi.dam.obrapprest.dao;

import java.util.List;

import frsf.isi.dam.obrapprest.modelo.Obra;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;


public interface ObraRest {

    @GET("obras/")
    Call<List<Obra>> buscarTodas();

    @DELETE("obras/{id}")
    Call<Void> borrar(@Path("id") Integer id);

    @PUT("obras/{id}")
    Call<Obra> actualizar(@Path("id") Integer id, @Body Obra obra);

    @POST("obras/")
    Call<Obra> crear(@Body Obra obra);

}
