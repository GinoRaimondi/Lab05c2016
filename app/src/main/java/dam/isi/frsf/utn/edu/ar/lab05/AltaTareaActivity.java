package dam.isi.frsf.utn.edu.ar.lab05;

import android.content.Intent;
import android.database.Cursor;
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

        // Debemos cargar el spinner


        final ProyectoDAO myDao = MainActivity.proyectoDAO;
        Cursor cursor = myDao.listarUsuarios();

        // make an adapter from the cursor.
        String[] from = new String[] {ProyectoDBMetadata.TablaUsuariosMetadata.USUARIO};
        int[] to = new int[] { android.R.id.text1};
        SimpleCursorAdapter sca = new SimpleCursorAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, cursor, from, to,1);

        // set layout for activated adapter
        sca.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // get xml file spinner and set adapter

        spinner.setAdapter(sca);


        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("llegamos 0","sabe 0");
                Tarea t = new Tarea(5,descripcion.getText().toString(),false,2,50,false,
                        new Proyecto(1,"TP integrador"),new Prioridad(),
                        new Usuario(1,spinner.getSelectedItem().toString(),"sdadad"));
                // Debemos ir a ProyectoDAO
                myDao.nuevaTarea(t);

            }
        });

    }

}
