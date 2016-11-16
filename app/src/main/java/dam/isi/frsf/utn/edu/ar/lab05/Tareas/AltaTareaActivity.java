package dam.isi.frsf.utn.edu.ar.lab05.Tareas;

import android.database.Cursor;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;

import dam.isi.frsf.utn.edu.ar.lab05.MainActivity;
import dam.isi.frsf.utn.edu.ar.lab05.R;
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
        valorSeekBar = 1;

        final ProyectoDAO myDao = MainActivity.proyectoDAO;

        Bundle extras = getIntent().getExtras();

        idTarea = extras.getInt("ID_TAREA");
        //Log.d("Extras ver ver ver ",extras.getString("ID_TAREA"));

        if (idTarea != 0) {
            idTarea = extras.getInt("ID_TAREA");
            Snackbar.make(btnGuardar, "Usted editará la tarea con id " + idTarea, Snackbar.LENGTH_LONG).show();

            // Debemos buscar en la base los datos para la tarea con id x;

            Cursor cursor = myDao.getTarea(idTarea);

            if (cursor.moveToFirst()); // data?

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

                    //Toast.makeText(getApplicationContext(), spinner.getSelectedItem().toString() , Toast.LENGTH_LONG).show();

                    Tarea t = new Tarea(idTarea, descripcion.getText().toString(), false, Integer.parseInt(horasEstimadas.getText().toString()), 0, false,
                            new Proyecto(1, "TP integrador"), p,
                            new Usuario(id_nombre_usuario, nombre_usuario, "sdadad"));
                    // Debemos ir a ProyectoDAO



                    myDao.actualizarTareaCompleta(t);

                }
            });


        } else {
            Snackbar.make(btnGuardar, "Usted dará de alta una tarea.", Snackbar.LENGTH_LONG).show();
            idTarea = null;

            btnGuardar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //Log.d("llegamos 0","sabe 0");

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

                    //Toast.makeText(getApplicationContext(), spinner.getSelectedItem().toString() , Toast.LENGTH_LONG).show();

                    Tarea t = new Tarea(5, descripcion.getText().toString(), false, Integer.parseInt(horasEstimadas.getText().toString()), 0, false,
                            new Proyecto(1, "TP integrador"), p,
                            new Usuario(id_nombre_usuario, nombre_usuario, "sdadad"));
                    // Debemos ir a ProyectoDAO
                    myDao.nuevaTarea(t);

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
}
