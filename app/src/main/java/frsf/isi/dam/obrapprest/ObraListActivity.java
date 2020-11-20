package frsf.isi.dam.obrapprest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import frsf.isi.dam.obrapprest.modelo.Obra;
import frsf.isi.dam.obrapprest.dao.ObraRepository;

public class ObraListActivity extends AppCompatActivity {

    List<Obra>  listaDataSet;
    ListView lvObras;
    TextView obraSeleccionada;
    Button btnMenu,btnAdd,btnBorrarObra,btnEditarObra;
    ArrayAdapter<Obra> adapter;
    Obra currentObra;
    int indiceCurrentObra=-1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_obra_list);

        lvObras = (ListView) findViewById(R.id.listaObras);
        obraSeleccionada = (TextView) findViewById(R.id.obraSeleccionada);
        btnEditarObra = (Button) findViewById(R.id.btnEditarObra);
        btnMenu = (Button) findViewById(R.id.btnObraMenuPpal);
        btnAdd = (Button) findViewById(R.id.btnAddObra);
        btnBorrarObra = (Button) findViewById(R.id.btnBorrarObra);

        btnEditarObra .setEnabled(false);
        btnBorrarObra.setEnabled(false);

        ObraRepository.getInstance().listarObra(miHandler);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ObraListActivity.this,ObraActivity.class);
                startActivity(i);
            }
        });

        btnBorrarObra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnBorrarObra.setEnabled(false);
                btnEditarObra.setEnabled(false);
                ObraRepository.getInstance().borrarObra(currentObra,miHandler);
            }
        });

        btnEditarObra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnEditarObra.setEnabled(false);
                btnBorrarObra.setEnabled(false);
                Intent i = new Intent(ObraListActivity.this,ObraActivity.class);
                i.putExtra("indiceObraActual",indiceCurrentObra);
                startActivity(i);
            }
        });

        lvObras.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                indiceCurrentObra = position;
                currentObra = adapter.getItem(position);
                obraSeleccionada.setText(currentObra.getId()+": "+currentObra.getDescripcion());
                btnBorrarObra.setEnabled(true);
                btnEditarObra.setEnabled(true);
            }
        });

        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ObraListActivity.this,MainActivity.class);
                startActivity(i);
            }
        });
    }

    Handler miHandler = new Handler(Looper.myLooper()){
        @Override
        public void handleMessage(Message msg) {
            listaDataSet = ObraRepository.getInstance().getListaObras();

            switch (msg.arg1 ){
                case ObraRepository._CONSULTA_OBRA:

                    adapter = new ArrayAdapter<>(ObraListActivity.this,android.R.layout.simple_list_item_single_choice,listaDataSet );
                    lvObras.setAdapter(adapter);

                    break;
                case ObraRepository._BORRADO_OBRA:

                    lvObras.clearChoices();
                    btnBorrarObra.setEnabled(false);
                    adapter.notifyDataSetChanged();

                    break;
            }
        }
    };

}
