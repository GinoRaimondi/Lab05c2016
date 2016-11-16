package dam.isi.frsf.utn.edu.ar.lab05.modelo;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import dam.isi.frsf.utn.edu.ar.lab05.MainActivity;
import dam.isi.frsf.utn.edu.ar.lab05.R;
import dam.isi.frsf.utn.edu.ar.lab05.dao.ProyectoApiRest;
import dam.isi.frsf.utn.edu.ar.lab05.dao.ProyectoDAO;

public class ProyectosActivity extends AppCompatActivity {
    private ListView lvProyectos;
    private ArrayList<String> listaProyectos;
    private ArrayAdapter<String> listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proyectos);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        new TareaAsincronica().execute("");

        listaProyectos = new ArrayList<String>();
        listaProyectos.add("proyecto1");
        listaProyectos.add("proyecto2");
        listaProyectos.add("proyecto3");
        lvProyectos = (ListView) findViewById(R.id.listaProyectos);
        //listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, listaProyectos);
        //lvProyectos.setAdapter(listAdapter);
    }

    private class TareaAsincronica extends AsyncTask<String, Void, ArrayList<String>> {
        @Override
        protected ArrayList<String> doInBackground(String... params) {
            ProyectoApiRest rest = new ProyectoApiRest();
            ArrayList<String> listaProyectos = rest.listarProyectos();
            return listaProyectos;
        }

        @Override
        protected void onPostExecute(ArrayList<String> result) {
            listAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, android.R.id.text1, result);
            lvProyectos.setAdapter(listAdapter);
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }


}
