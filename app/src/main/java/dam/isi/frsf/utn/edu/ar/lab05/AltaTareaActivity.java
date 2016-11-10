package dam.isi.frsf.utn.edu.ar.lab05;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;

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

        spinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Debemos ir a ProyectoDAO

            }
        });


    }



}
