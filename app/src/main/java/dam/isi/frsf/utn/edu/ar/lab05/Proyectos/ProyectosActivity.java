package dam.isi.frsf.utn.edu.ar.lab05.Proyectos;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import dam.isi.frsf.utn.edu.ar.lab05.MainActivity;
import dam.isi.frsf.utn.edu.ar.lab05.R;
import dam.isi.frsf.utn.edu.ar.lab05.dao.ProyectoApiRest;
import dam.isi.frsf.utn.edu.ar.lab05.dao.ProyectoDAO;
import dam.isi.frsf.utn.edu.ar.lab05.modelo.Proyecto;

public class ProyectosActivity extends AppCompatActivity {
    private ListView lvProyectos;
    private ArrayList<String> listaProyectos;
    private ArrayList<Integer> idProyectos;
    private ArrayAdapter<String> listAdapter;
    private String m_Text;

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

                AlertDialog.Builder builder = new AlertDialog.Builder(ProyectosActivity.this);
                builder.setTitle("Crear nuevo Proyecto");

                final EditText input = new EditText(ProyectosActivity.this);

                input.setInputType(InputType.TYPE_CLASS_TEXT);

                input.setHint("Descripción del proyecto");

                /*final TextView textview = new TextView(ProyectosActivity.this);
                textview.setText("Ingrese descripción");
                builder.setCustomTitle(textview);*/

                builder.setView(input);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        m_Text = input.getText().toString();

                        // Debemos pegarle a la api rest para insertar un proyecto con descripción m_text

                        ProyectoApiRest rest = new ProyectoApiRest();

                        Proyecto p = new Proyecto();

                        p.setNombre(m_Text);

                        ProyectoDAO pd = MainActivity.proyectoDAO;

                        Integer valorMaximoIdProyectos = pd.obtenerMaximoIdValorProyecto();

                        Integer valorIdActual = valorMaximoIdProyectos + 1;

                        //p.setId(valorIdActual);

                        new CrearProyecto(p,1,null).execute("");



                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();

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


            case R.id.botonBorrarProyecto:
                Toast.makeText(this, "Borrando la tarea", Toast.LENGTH_LONG).show();
                new CrearProyecto(null,2,idProyectos.get(info.position)).execute("");
                Log.d("ITEM ID: ", ""+idProyectos.get(info.position));
        }

        return true;
    }


    private class TareaAsincronica extends AsyncTask<String, Void, ArrayList<ArrayList>> {
        @Override
        protected ArrayList<ArrayList> doInBackground(String... params) {
            ProyectoApiRest rest = new ProyectoApiRest();
            ArrayList<ArrayList> listaProyectos = rest.listarProyectos();
            return listaProyectos;
        }

        @Override
        protected void onPostExecute(ArrayList<ArrayList> result) {
            listAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, android.R.id.text1, result.get(1));
            idProyectos = result.get(0);
            ArrayAdapter<String> adapter=new ArrayAdapter<String>(
                    getApplicationContext(), android.R.layout.simple_list_item_1, result.get(1)){

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

            lvProyectos.setAdapter(adapter);
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }


    private class CrearProyecto extends AsyncTask<Object, Object, Integer> {
        private Proyecto p;
        private Integer i;
        private Integer id;
        public CrearProyecto(Proyecto p, Integer i, Integer id){
            this.p = p;
            this.i = i;
            this.id = id;
        }

        @Override
        protected Integer doInBackground(Object... params) {

            ProyectoApiRest rest = new ProyectoApiRest();

            switch(i){
                case 1: {
                    rest.crearProyecto(p);
                }break;
                case 2:{
                    rest.borrarProyecto(id);
                }break;
                case 3:{

                }break;
                case 4:{

                }break;
            }
            //return listaProyectos;
            return 1;
        }

        @Override
        protected void onPostExecute(Integer result) {

            new TareaAsincronica().execute();

        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Object... values) {
        }
    }


}
