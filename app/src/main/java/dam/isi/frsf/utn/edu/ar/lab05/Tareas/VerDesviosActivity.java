package dam.isi.frsf.utn.edu.ar.lab05.Tareas;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import dam.isi.frsf.utn.edu.ar.lab05.MainActivity;
import dam.isi.frsf.utn.edu.ar.lab05.R;
import dam.isi.frsf.utn.edu.ar.lab05.dao.ProyectoDAO;
import dam.isi.frsf.utn.edu.ar.lab05.modelo.Tarea;

public class VerDesviosActivity extends AppCompatActivity {
    private EditText minutosDeDesvio;
    private Switch tareaTerminada;
    private Button buscar;
    private TextView resultado;
    private ProyectoDAO myDao;
    private List<Tarea> tareas;
    private String texto="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_desvios);

        minutosDeDesvio = (EditText) findViewById(R.id.editTextMinutosDesvio);
        tareaTerminada = (Switch) findViewById(R.id.switchTareaTerminada);
        buscar = (Button) findViewById(R.id.buttonBuscarDesvios);
        resultado = (TextView) findViewById(R.id.textViewResultadoDesvios);

        myDao = MainActivity.proyectoDAO;

        buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String minutos = minutosDeDesvio.getText().toString();
                Boolean terminada = tareaTerminada.isChecked();
                Log.d("TERMINADA",""+terminada.toString());
                tareas = myDao.listarDesviosPlanificacion(terminada,Integer.parseInt(minutos));
                texto = "Descripcion | Finalizada | Desvío";
                for(Tarea t: tareas){
                    texto+="\n\t"+t.getDescripcion()+"\t\t\t|\t"+t.getTerminada()+"\t\t|\t"+(t.getHorasEstimadas()-t.getMinutosTrabajados());
                }
                resultado.setText(texto);
            }
        });
    }
}
