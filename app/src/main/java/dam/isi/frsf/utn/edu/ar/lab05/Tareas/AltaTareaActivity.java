package dam.isi.frsf.utn.edu.ar.lab05.Tareas;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import dam.isi.frsf.utn.edu.ar.lab05.MainActivity;
import dam.isi.frsf.utn.edu.ar.lab05.Proyectos.ProyectosActivity;
import dam.isi.frsf.utn.edu.ar.lab05.R;
import dam.isi.frsf.utn.edu.ar.lab05.contactos.Contactos;
import dam.isi.frsf.utn.edu.ar.lab05.contactos.Permisos;
import dam.isi.frsf.utn.edu.ar.lab05.dao.ProyectoApiRest;
import dam.isi.frsf.utn.edu.ar.lab05.dao.ProyectoDAO;
import dam.isi.frsf.utn.edu.ar.lab05.dao.ProyectoDBMetadata;
import dam.isi.frsf.utn.edu.ar.lab05.modelo.Prioridad;
import dam.isi.frsf.utn.edu.ar.lab05.modelo.Proyecto;
import dam.isi.frsf.utn.edu.ar.lab05.modelo.Tarea;
import dam.isi.frsf.utn.edu.ar.lab05.modelo.Usuario;

public class AltaTareaActivity extends AppCompatActivity {

    private EditText descripcion;
    private EditText horasEstimadas;
    private SeekBar seekBar;
    private Spinner spinner;
    private Button btnGuardar;
    private Button btnCancelar;
    private Integer valorSeekBar;
    private String nombre_usuario;
    private Integer id_nombre_usuario;
    private Integer idTarea;
    private Spinner spinner_contactos;
    private ContextCompat context;
    private ArrayList<Usuario> usuarios_contactos;
    public Tarea t;
    public Proyecto p;
    private boolean flagPermisoPedido;
    private static final int PERMISSION_REQUEST_CONTACT = 999;
    ProyectoDAO myDao = new ProyectoDAO(null);

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_alta_tarea);

        descripcion = (EditText) findViewById(R.id.descripcion);
        horasEstimadas = (EditText) findViewById(R.id.horasEstimadas);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        spinner = (Spinner) findViewById(R.id.spinner);
        btnGuardar = (Button) findViewById(R.id.btnGuardar);
        btnCancelar = (Button) findViewById(R.id.btnCancelar);
        spinner_contactos = (Spinner) findViewById(R.id.spinner_contactos);

        valorSeekBar = 1;

        askForContactPermission();

        myDao = MainActivity.proyectoDAO;

        Bundle extras = getIntent().getExtras();

        idTarea = extras.getInt("ID_TAREA");
        //Log.d("Extras ver ver ver ",extras.getString("ID_TAREA"));

        if (idTarea != 0) {
            idTarea = extras.getInt("ID_TAREA");
            Snackbar.make(btnGuardar, "Usted editará la tarea con id " + idTarea, Snackbar.LENGTH_LONG).show();

            // Debemos buscar en la base los datos para la tarea con id x;

            Cursor cursor = myDao.getTarea(idTarea);

            if (cursor.moveToFirst()) ; // data?

            String descripcionTarea = cursor.getString(cursor.getColumnIndex("DESCRIPCION"));
            String horas_planificadas = cursor.getString(cursor.getColumnIndex("HORAS_PLANIFICADAS"));
            String minutos_trabajados = cursor.getString(cursor.getColumnIndex("MINUTOS_TRABAJDOS"));
            String id_prioridad = cursor.getString(cursor.getColumnIndex("ID_PRIORIDAD"));
            String id_responsable = cursor.getString(cursor.getColumnIndex("ID_RESPONSABLE"));
            String id_proyecto = cursor.getString(cursor.getColumnIndex("ID_PROYECTO"));
            String finalizada = cursor.getString(cursor.getColumnIndex("FINALIZADA"));

            cursor.close();

            // Debemos cargar los valores en la interface.

            this.descripcion.setText(descripcionTarea);
            this.horasEstimadas.setText(horas_planificadas);
            this.seekBar.setProgress(Integer.parseInt(id_prioridad));
            this.spinner.setSelection(Integer.parseInt(id_responsable));

            btnGuardar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //Log.d("llegamos 0","sabe 0");
                    new GetMaxIdUser().execute();


                    //Toast.makeText(getApplicationContext(), spinner.getSelectedItem().toString() , Toast.LENGTH_LONG).show();


                }
            });


        } else {
            Snackbar.make(btnGuardar, "Usted dará de alta una tarea.", Snackbar.LENGTH_LONG).show();
            idTarea = null;

            btnGuardar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //Log.d("llegamos 0","sabe 0");


                    //Toast.makeText(getApplicationContext(), spinner.getSelectedItem().toString() , Toast.LENGTH_LONG).show();

                    new CrearTarea().execute();


                }
            });
        }

        // Debemos cargar el spinner

        Cursor cursor = myDao.listarUsuarios();

        // make an adapter from the cursor.
        String[] from = new String[]{ProyectoDBMetadata.TablaUsuariosMetadata.USUARIO};
        int[] to = new int[]{android.R.id.text1};
        SimpleCursorAdapter sca = new SimpleCursorAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, cursor, from, to, 1);

        // set layout for activated adapter
        sca.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // get xml file spinner and set adapter

        spinner.setAdapter(sca);

        spinner.setVisibility(View.GONE);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                Cursor c = (Cursor) spinner.getSelectedItem();

                nombre_usuario = c.getString(c.getColumnIndex(ProyectoDBMetadata.TablaUsuariosMetadata.USUARIO));
                id_nombre_usuario = c.getInt(c.getColumnIndex(ProyectoDBMetadata.TablaUsuariosMetadata._ID));

                Log.i("Selected item : ", nombre_usuario);

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }

        });

        spinner_contactos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                nombre_usuario = spinner_contactos.getSelectedItem().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

                nombre_usuario = "DENIS";

            }

        });


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {


                //textAMostrarValorSeekSeleccionado.setTextSize(progress);
                //Toast.makeText(getApplicationContext(), String.valueOf(progress), Toast.LENGTH_LONG).show();
                valorSeekBar = progress;
            }

        });

    }

    public void askForContactPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.CALL_PHONE)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Permisos Peligrosos!!!");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setMessage("Puedo acceder a un permiso peligroso???");
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @TargetApi(Build.VERSION_CODES.M)
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            flagPermisoPedido = true;
                            requestPermissions(
                                    new String[]
                                            {Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS, Manifest.permission.GET_ACCOUNTS}
                                    , PERMISSION_REQUEST_CONTACT);
                        }
                    });
                    builder.show();
                } else {
                    flagPermisoPedido = true;
                    ActivityCompat.requestPermissions(this,
                            new String[]
                                    {Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS, Manifest.permission.GET_ACCOUNTS}
                            , PERMISSION_REQUEST_CONTACT);
                }

            }
        }
        if (!flagPermisoPedido) hacerAlgoQueRequeriaPermisosPeligrosos();
    }

    public void hacerAlgoQueRequeriaPermisosPeligrosos() {

        usuarios_contactos = buscarTodosContactos();

        ArrayList<String> a = new ArrayList<String>();


        for (Integer i = 0; i < usuarios_contactos.size(); i++) {

            a.add(usuarios_contactos.get(i).getNombre().toString());

        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, a);
        spinner_contactos.setAdapter(adapter);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        Log.d("ESCRIBIR_JSON", "req code" + requestCode + " " + Arrays.toString(permissions) + " ** " + Arrays.toString(grantResults));
        switch (requestCode) {
            case PERMISSION_REQUEST_CONTACT: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    usuarios_contactos = buscarTodosContactos();

                    ArrayList<String> a = new ArrayList<String>();


                    for (Integer i = 0; i < usuarios_contactos.size(); i++) {

                        a.add(usuarios_contactos.get(i).getNombre().toString());

                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                            android.R.layout.simple_spinner_item, a);
                    spinner_contactos.setAdapter(adapter);

                } else {
                    Toast.makeText(this, "No permission for contacts", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }

    }

    public ArrayList<Usuario> buscarTodosContactos() {

        ArrayList<Usuario> usuarios = new ArrayList<Usuario>();

        JSONArray arr = new JSONArray();
        final StringBuilder resultado = new StringBuilder();
        Uri uri = ContactsContract.Contacts.CONTENT_URI;
        String sortOrder = ContactsContract.Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC";

        // consulta ejemplo buscando por nombre visualizado en los contactos agregados
        Cursor c = this.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);

        //Cursor c = this.getContentResolver().query(uri, null, ContactsContract.Contacts.DISPLAY_NAME, null, sortOrder);
        int count = c.getColumnCount();
        int fila = 0;
        String[] columnas = new String[count];
        try {
            while (c.moveToNext()) {
                JSONObject unContacto = new JSONObject();
                for (int i = 0; (i < count); i++) {
                    if (fila == 0) columnas[i] = c.getColumnName(i);
                    unContacto.put(columnas[i], c.getString(i));
                }
                Log.d("TEST-ARR", unContacto.toString());
                arr.put(fila, unContacto);
                fila++;
                Log.d("TEST-ARR", "fila : " + fila);

                // elegir columnas de ejemplo
                resultado.append(unContacto.get("name_raw_contact_id") + " - " + unContacto.get("display_name") + System.getProperty("line.separator"));

                Usuario usuario = new Usuario();
                usuario.setId(fila);
                usuario.setNombre(unContacto.get("display_name").toString());
                usuarios.add(usuario);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("TEST-ARR", resultado.toString());


        /*this.arraySpinner = new String[] {
                "1", "2", "3", "4", "5"
        };*/

        /*ArrayList<String> a = new ArrayList<String>();


        for (Integer i = 0; i< usuarios_contactos.size(); i++){

            a.add(usuarios_contactos.get(i).getNombre().toString());

        }*/


        return usuarios;
    }


    private class GetMaxIdUser extends AsyncTask<Object, Object, Integer> {

        public GetMaxIdUser() {

        }

        @Override
        protected Integer doInBackground(Object... params) {

            Integer valor = 100;

            ProyectoApiRest rest = new ProyectoApiRest();

            try {
                valor = rest.getMaxIdUser();
            } catch (Exception e) {

            }

            return valor;

        }

        @Override
        protected void onPostExecute(Integer result) {

            id_nombre_usuario = result;

            Prioridad p = new Prioridad();
            String prioridad = "Baja";
            switch (valorSeekBar) {
                case 0:
                    prioridad = "Baja";
                    break;
                case 1:
                    prioridad = "Urgente";
                    break;
                case 2:
                    prioridad = "Alta";
                    break;
                case 3:
                    prioridad = "Media";
                    break;
                case 4:
                    prioridad = "Baja";
                    break;

            }

            p.setId(valorSeekBar);

            p.setPrioridad(prioridad);

            Tarea t = new Tarea(idTarea, descripcion.getText().toString(), false, Integer.parseInt(horasEstimadas.getText().toString()), 0, false,
                    new Proyecto(1, "TP integrador"), p,
                    new Usuario(/*id_nombre_usuario*/1, nombre_usuario, "sdadad"));
            // Debemos ir a ProyectoDAO


            myDao.actualizarTareaCompleta(t);

        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onProgressUpdate(Object... values) {

        }

    }

    private class CrearTarea extends AsyncTask<Object, Object, Integer> {

        public CrearTarea() {

        }

        @Override
        protected Integer doInBackground(Object... params) {

            Integer valor = 0;
            Integer idMax = 100;
            Integer idApiRest=0;
            Usuario u = new Usuario();

            ProyectoApiRest rest = new ProyectoApiRest();

            try {

                idMax = rest.getMaxIdUser();

                idApiRest = rest.existeUsuario(nombre_usuario);

                if(idApiRest>0){

                    // Si ya esta en la api rest no lo creamos en ningun lado
                    // Para hacer esto debemos estar sincronizados con la base sqlLite

                    valor=idApiRest;

                    // Ponemos por defecto para generar la terea en sqlLite un usuario.
                    // Habria que buscar en sqlLite el user que tenga el nombre requerido.
                    // Deben sincronizarse las bases.
                    id_nombre_usuario = 1;

                }else {

                    valor=idMax;

                    u.setNombre(nombre_usuario);
                    u.setId(valor);
                    u.setCorreoElectronico(nombre_usuario+"@gmail.com");
                    rest.crearUsuario(u);

                    id_nombre_usuario = valor;
                    // Generamos el nuevo usuario en sqlLite
                    myDao.nuevoUsuario(u);
                }

            } catch (Exception e) {

            }

            return valor;

        }

        @Override
        protected void onPostExecute(Integer result) {

            //id_nombre_usuario = result;
            Log.d("ID USUARIO",""+id_nombre_usuario);

            Prioridad p = new Prioridad();
            String prioridad = "Baja";
            switch (valorSeekBar) {
                case 0:
                    prioridad = "Baja";
                    break;
                case 1:
                    prioridad = "Urgente";
                    break;
                case 2:
                    prioridad = "Alta";
                    break;
                case 3:
                    prioridad = "Media";
                    break;
                case 4:
                    prioridad = "Baja";
                    break;

            }

            p.setId(valorSeekBar);

            p.setPrioridad(prioridad);

            Tarea t = new Tarea(idTarea, descripcion.getText().toString(), false, Integer.parseInt(horasEstimadas.getText().toString()), 0, false,
                    new Proyecto(1, "TP integrador"), p,
                    new Usuario(id_nombre_usuario, nombre_usuario, "sdadad"));
            myDao.nuevaTarea(t);

        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onProgressUpdate(Object... values) {

        }

    }

}


