package dam.isi.frsf.utn.edu.ar.lab05;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import dam.isi.frsf.utn.edu.ar.lab05.dao.ProyectoDAO;
import dam.isi.frsf.utn.edu.ar.lab05.dao.ProyectoDBMetadata;
import dam.isi.frsf.utn.edu.ar.lab05.modelo.Tarea;

/**
 * Created by mdominguez on 06/10/16.
 */
public class TareaCursorAdapter extends CursorAdapter {
    private LayoutInflater inflador;
    private ProyectoDAO myDao;
    private Context contexto;
    private Long marcaDeTiempo;
    private Tarea tarea;

    public TareaCursorAdapter (Context contexto, Cursor c, ProyectoDAO dao) {
        super(contexto, c, false);
        myDao= dao;
        this.contexto = contexto;
    }

    @Override
        public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        inflador = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View vista = inflador.inflate(R.layout.fila_tarea,viewGroup,false);
        return vista;
    }

    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {
        //obtener la posicion de la fila actual y asignarla a los botones y checkboxes
        int pos = cursor.getPosition();

        // Referencias UI.
        TextView nombre= (TextView) view.findViewById(R.id.tareaTitulo);
        TextView tiempoAsignado= (TextView) view.findViewById(R.id.tareaMinutosAsignados);
        TextView tiempoTrabajado= (TextView) view.findViewById(R.id.tareaMinutosTrabajados);
        TextView prioridad= (TextView) view.findViewById(R.id.tareaPrioridad);
        TextView responsable= (TextView) view.findViewById(R.id.tareaResponsable);
        final TextView descripcion= (TextView) view.findViewById(R.id.tareaTitulo);
        final CheckBox finalizada = (CheckBox)  view.findViewById(R.id.tareaFinalizada);

        final Button btnFinalizar = (Button)   view.findViewById(R.id.tareaBtnFinalizada);
        final Button btnEditar = (Button)   view.findViewById(R.id.tareaBtnEditarDatos);
        final ToggleButton btnEstado = (ToggleButton) view.findViewById(R.id.tareaBtnTrabajando);

        nombre.setText(cursor.getString(cursor.getColumnIndex(ProyectoDBMetadata.TablaTareasMetadata.TAREA)));
        final Integer horasAsigandas = cursor.getInt(cursor.getColumnIndex(ProyectoDBMetadata.TablaTareasMetadata.HORAS_PLANIFICADAS));
        tiempoAsignado.setText(horasAsigandas*60 + " minutos");

        Integer minutosAsigandos = cursor.getInt(cursor.getColumnIndex(ProyectoDBMetadata.TablaTareasMetadata.MINUTOS_TRABAJADOS));
        tiempoTrabajado.setText(minutosAsigandos+ " minutos");
        String p = cursor.getString(cursor.getColumnIndex(ProyectoDBMetadata.TablaPrioridadMetadata.PRIORIDAD_ALIAS));
        prioridad.setText(p);
        responsable.setText(cursor.getString(cursor.getColumnIndex(ProyectoDBMetadata.TablaUsuariosMetadata.USUARIO_ALIAS)));
        finalizada.setChecked(cursor.getInt(cursor.getColumnIndex(ProyectoDBMetadata.TablaTareasMetadata.FINALIZADA))==1);
        finalizada.setTextIsSelectable(false);



        btnEstado.setTag(cursor.getInt(cursor.getColumnIndex("_id")));

        tarea = new Tarea();

       /* btnEstado.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //final Integer idTarea= (Integer) view.getTag();

                if (isChecked) {
                    // The toggle is enabled
                } else {
                    // The toggle is disabled
                }
            }
        });*/


        btnEstado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Integer idTarea= (Integer) view.getTag();
                /*Intent intEditarAct = new Intent(contexto,AltaTareaActivity.class);
                intEditarAct.putExtra("ID_TAREA",idTarea);
                context.startActivity(intEditarAct);*/
                if(btnEstado.isChecked()){
                    marcaDeTiempo = System.currentTimeMillis();
                    Toast.makeText(contexto, "Ha checkeado la tarea con id " + idTarea,Toast.LENGTH_SHORT).show();

                }
                else
                {
                    Long finalTrabajo = System.currentTimeMillis();

                    Long tiempoTrabajado = finalTrabajo - marcaDeTiempo;

                    Integer i = tiempoTrabajado != null ? tiempoTrabajado.intValue() : null;

                    Integer minutosTrabajados = i / 100;

                    tarea.setId(idTarea);

                    tarea.setMinutosTrabajados( cursor.getInt(cursor.getColumnIndex(ProyectoDBMetadata.TablaTareasMetadata.MINUTOS_TRABAJADOS)) + minutosTrabajados);

                    MainActivity.proyectoDAO.actualizarTarea(tarea);

                    // Pasar a segundos

                    // Guardar en labase

                    Toast.makeText(contexto, "Ha trabajado durante " + minutosTrabajados + " Milisegundos",Toast.LENGTH_SHORT).show();
                }

            }
        });

        btnEditar.setTag(cursor.getInt(cursor.getColumnIndex("_id")));
        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Integer idTarea= (Integer) view.getTag();
                Intent intEditarAct = new Intent(contexto,AltaTareaActivity.class);
                intEditarAct.putExtra("ID_TAREA",idTarea);
                context.startActivity(intEditarAct);

            }
        });

        btnFinalizar.setTag(cursor.getInt(cursor.getColumnIndex("_id")));
        btnFinalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Integer idTarea= (Integer) view.getTag();
                Thread backGroundUpdate = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("LAB05-MAIN","finalizar tarea : --- "+idTarea);
                        myDao.finalizar(idTarea);
                        handlerRefresh.sendEmptyMessage(1);
                    }
                });
                backGroundUpdate.start();
            }
        });
    }

    Handler handlerRefresh = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message inputMessage) {
            TareaCursorAdapter.this.changeCursor(myDao.listaTareas(1));
        }
    };
}

