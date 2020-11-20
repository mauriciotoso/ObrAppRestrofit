package frsf.isi.dam.obrapprest.dao;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import frsf.isi.dam.obrapprest.modelo.Obra;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Retrofit;

public class ObraRepository {

    public static String _SERVER = "http://10.0.2.2:5000/";
    private List<Obra> listaObras;

    public static final int _ALTA_OBRA =1;
    public static final int _UPDATE_OBRA =2;
    public static final int _BORRADO_OBRA =3;
    public static final int _CONSULTA_OBRA =4;
    public static final int _ERROR_OBRA =9;

    private static ObraRepository _INSTANCE;
    private Retrofit rf;
    private ObraRest obraRest;

    private ObraRepository(){}

    public static ObraRepository getInstance(){
        if(_INSTANCE==null){
            _INSTANCE = new ObraRepository();
            _INSTANCE.configurarRetrofit();
            _INSTANCE.listaObras = new ArrayList<>();
        }
        return _INSTANCE;
    }

    private void configurarRetrofit(){
        this.rf = new Retrofit.Builder()
                .baseUrl(_SERVER)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Log.d("APP_2","INSTANCIA CREADA");

        this.obraRest = this.rf.create(ObraRest.class);
    }

    public void actualizarObra(final Obra o, final Handler h){
        Call<Obra> llamada = this.obraRest.actualizar(o.getId(),o);
        llamada.enqueue(new Callback<Obra>() {

            @Override
            public void onResponse(Call<Obra> call, Response<Obra> response) {

                Log.d("APP_2","Despues que ejecuta"+ response.isSuccessful());
                Log.d("APP_2","Codigo"+ response.code());

                if(response.isSuccessful()){
                    Log.d("APP_2","EJECUTO");
                    listaObras.remove(o);
                    listaObras.add(response.body());
                    Message m = new Message();
                    m.arg1 = _UPDATE_OBRA;
                    h.sendMessage(m);
                }
            }

            @Override
            public void onFailure(Call<Obra> call, Throwable t) {
                Log.d("APP_2","ERROR "+t.getMessage());
                Message m = new Message();
                m.arg1 = _ERROR_OBRA;
                h.sendMessage(m);
            }
        });
    }

    public void crearObra(Obra o, final Handler h){
        Call<Obra> llamada = this.obraRest.crear(o);
        llamada.enqueue(new Callback<Obra>() {
            @Override
            public void onResponse(Call<Obra> call, Response<Obra> response) {
                Log.d("APP_2","Despues que ejecuta"+ response.isSuccessful());
                Log.d("APP_2","COdigo"+ response.code());

                if(response.isSuccessful()){
                    Log.d("APP_2","EJECUTO");
                    listaObras.add(response.body());
                    Message m = new Message();
                    m.arg1 = _ALTA_OBRA;
                    h.sendMessage(m);
                }
            }

            @Override
            public void onFailure(Call<Obra> call, Throwable t) {
                Log.d("APP_2","ERROR "+t.getMessage());
                Message m = new Message();
                m.arg1 = _ERROR_OBRA;
                h.sendMessage(m);
            }
        });
    }

    public void listarObra(final Handler h){
        Call<List<Obra>> llamada = this.obraRest.buscarTodas();
        llamada.enqueue(new Callback<List<Obra>>() {
            @Override
            public void onResponse(Call<List<Obra>> call, Response<List<Obra>> response) {
                if(response.isSuccessful()){
                    listaObras.clear();
                    listaObras.addAll(response.body());
                    Message m = new Message();
                    m.arg1 = _CONSULTA_OBRA;
                    h.sendMessage(m);
                }
            }

            @Override
            public void onFailure(Call<List<Obra>> call, Throwable t) {
                Message m = new Message();
                m.arg1 = _ERROR_OBRA;
                h.sendMessage(m);
            }
        });
    }

    public void borrarObra(final Obra o, final Handler h){
        Call<Void> llamada = this.obraRest.borrar(o.getId());
        llamada.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.d("APP_2","Despues que ejecuta"+ response.isSuccessful());
                Log.d("APP_2","COdigo"+ response.code());

                if(response.isSuccessful()){
                    Log.d("APP_2","EJECUTO");
                    for(Obra o : listaObras){
                        Log.d("APP_2","Obra "+o.getId());
                    }
                    Log.d("APP_2","BORRA Obra "+o.getId());
                    listaObras.remove(o);
                    for(Obra o : listaObras){
                        Log.d("APP_2","Obra "+o.getId());
                    }
                    Message m = new Message();
                    m.arg1 = _BORRADO_OBRA;
                    h.sendMessage(m);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("APP_2","ERROR "+t.getMessage());
                Message m = new Message();
                m.arg1 = _ERROR_OBRA;
                h.sendMessage(m);
            }
        });
    }

    public List<Obra> getListaObras() {
        return listaObras;
    }
}