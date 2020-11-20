package frsf.isi.dam.obrapprest.dao;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import frsf.isi.dam.obrapprest.modelo.Material;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MaterialRepository {

    public static final int _ALTA_MATERIAL =1;
    public static final int _UPDATE_MATERIAL =2;
    public static final int _BORRADO_MATERIAL =3;
    public static final int _CONSULTA_MATERIAL =4;
    public static final int _ERROR_MATERIAL =9;

    public static String _SERVER = "http://10.0.2.2:5000/";
    private static MaterialRepository _INSTANCE;
    private Retrofit rf;
    private MaterialRest materialRest;
    private List<Material> listaMateriales;

    private MaterialRepository(){}

    public static MaterialRepository getInstance(){
        if(_INSTANCE==null){
            _INSTANCE = new MaterialRepository();
            _INSTANCE.configurarRetrofit();
            _INSTANCE.listaMateriales = new ArrayList<>();
        }
        return _INSTANCE;
    }

    private void configurarRetrofit(){
        this.rf = new Retrofit.Builder()
                .baseUrl(_SERVER)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Log.d("APP_2","INSTANCIA CREADA");

        this.materialRest = this.rf.create(MaterialRest.class);
    }

    public void actualizarObra(final Material mat, final Handler h){
        Call<Material> llamada = this.materialRest.actualizar(mat.getId(),mat);
        llamada.enqueue(new Callback<Material>() {

            @Override
            public void onResponse(Call<Material> call, Response<Material> response) {

                Log.d("APP_2","Despues que ejecuta: "+ response.isSuccessful());
                Log.d("APP_2","Codigo: "+ response.code());

                if(response.isSuccessful()){
                    Log.d("APP_2","EJECUTO");

                    listaMateriales.remove(mat);
                    listaMateriales.add(response.body());

                    Message m = new Message();
                    m.arg1 = _UPDATE_MATERIAL;
                    h.sendMessage(m);
                }
            }

            @Override
            public void onFailure(Call<Material> call, Throwable t) {
                Log.d("APP_2","ERROR "+t.getMessage());

                Message m = new Message();
                m.arg1 = _ERROR_MATERIAL;
                h.sendMessage(m);
            }
        });
    }

    public void crearObra(Material mat, final Handler h){
        Call<Material> llamada = this.materialRest.crear(mat);
        llamada.enqueue(new Callback<Material>() {
            @Override
            public void onResponse(Call<Material> call, Response<Material> response) {
                Log.d("APP_2","Despues que ejecuta"+ response.isSuccessful());
                Log.d("APP_2","COdigo"+ response.code());

                if(response.isSuccessful()){
                    Log.d("APP_2","EJECUTO");
                    listaMateriales.add(response.body());
                    Message m = new Message();
                    m.arg1 = _ALTA_MATERIAL;
                    h.sendMessage(m);
                }
            }

            @Override
            public void onFailure(Call<Material> call, Throwable t) {
                Log.d("APP_2","ERROR "+t.getMessage());
                Message m = new Message();
                m.arg1 = _ERROR_MATERIAL;
                h.sendMessage(m);
            }
        });
    }

    public void listarMaterial(final Handler h){
        Call<List<Material>> llamada = this.materialRest.buscarTodas();
        llamada.enqueue(new Callback<List<Material>>() {
            @Override
            public void onResponse(Call<List<Material>> call, Response<List<Material>> response) {
                if(response.isSuccessful()){
                    listaMateriales.clear();
                    listaMateriales.addAll(response.body());
                    Message m = new Message();
                    m.arg1 = _CONSULTA_MATERIAL;
                    h.sendMessage(m);
                }
            }

            @Override
            public void onFailure(Call<List<Material>> call, Throwable t) {
                Message m = new Message();
                m.arg1 = _ERROR_MATERIAL;
                h.sendMessage(m);
            }
        });
    }

    public void borrarObra(final Material mat, final Handler h){
        Call<Void> llamada = this.materialRest.borrar(mat.getId());
        llamada.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.d("APP_2","Despues que ejecuta"+ response.isSuccessful());
                Log.d("APP_2","COdigo"+ response.code());

                if(response.isSuccessful()){
                    Log.d("APP_2","EJECUTO");
                    for(Material mat : listaMateriales){
                        Log.d("APP_2","Material "+mat.getId());
                    }
                    Log.d("APP_2","BORRA Material "+mat.getId());
                    listaMateriales.remove(mat);
                    for(Material mat : listaMateriales){
                        Log.d("APP_2","Material "+mat.getId());
                    }
                    Message m = new Message();
                    m.arg1 = _BORRADO_MATERIAL;
                    h.sendMessage(m);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("APP_2","ERROR "+t.getMessage());
                Message m = new Message();
                m.arg1 = _ERROR_MATERIAL;
                h.sendMessage(m);
            }
        });
    }

    public List<Material> getListaMateriales() {
        return listaMateriales;
    }
}
