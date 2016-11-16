package dam.isi.frsf.utn.edu.ar.lab05;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

        setTitle("GESTIÓN DE PROYECTOS");

        listaProyectos = new ArrayList<String>();
        /*listaProyectos.add("proyecto1");
        listaProyectos.add("proyecto2");
        listaProyectos.add("proyecto3");*/
        lvProyectos = (ListView) findViewById(R.id.listaProyectos);

        registerForContextMenu(lvProyectos);
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

            ArrayAdapter<String> adapter=new ArrayAdapter<String>(
                    getApplicationContext(), android.R.layout.simple_list_item_1, result){

                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View view =super.getView(position, convertView, parent);

                    TextView textView=(TextView) view.findViewById(android.R.id.text1);

                    /*Elegimos el color*/

                    Integer color = Color.parseColor("#2196F3");

                    textView.setTextColor(color);

                    SpannableString spanString = new SpannableString(textView.getText().toString());

                    spanString.setSpan(new StyleSpan(Typeface.BOLD), 0, spanString.length(), 0);

                    textView.setText(spanString);

                    return view;
                }
            };

    /*SET THE ADAPTER TO LISTVIEW*/
            //setListAdapter(adapter);




            lvProyectos.setAdapter(adapter);
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }


    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
        //Proyecto proyecto = (Proyecto)  lvProyectos.getAdapter().getItem(info.position);
        //menu.setHeaderTitle("Opciones para " + tarea.getDescripcion());

        inflater.inflate(R.menu.menu_gestion_proyecto,menu);

    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()) {


            case R.id.borrarTarea:

                Toast.makeText(this, "Borrando la tarea", Toast.LENGTH_LONG).show();

                return true;
        }

        return true;
    }


}