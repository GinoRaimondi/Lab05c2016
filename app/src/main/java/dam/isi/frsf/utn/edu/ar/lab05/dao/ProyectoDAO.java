package dam.isi.frsf.utn.edu.ar.lab05.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

import dam.isi.frsf.utn.edu.ar.lab05.modelo.Prioridad;
import dam.isi.frsf.utn.edu.ar.lab05.modelo.Tarea;
import dam.isi.frsf.utn.edu.ar.lab05.modelo.Usuario;

/**
 * Created by mdominguez on 06/10/16.
 */
public class ProyectoDAO {

    private static final String _SQL_TAREAS_X_PROYECTO = "SELECT "+ProyectoDBMetadata.TABLA_TAREAS_ALIAS+"."+ProyectoDBMetadata.TablaTareasMetadata._ID+" as "+ProyectoDBMetadata.TablaTareasMetadata._ID+
            ", "+ProyectoDBMetadata.TablaTareasMetadata.TAREA +
            ", "+ProyectoDBMetadata.TablaTareasMetadata.HORAS_PLANIFICADAS +
            ", "+ProyectoDBMetadata.TablaTareasMetadata.MINUTOS_TRABAJADOS +
            ", "+ProyectoDBMetadata.TablaTareasMetadata.FINALIZADA +
            ", "+ProyectoDBMetadata.TablaTareasMetadata.PRIORIDAD +
            ", "+ProyectoDBMetadata.TABLA_PRIORIDAD_ALIAS+"."+ProyectoDBMetadata.TablaPrioridadMetadata.PRIORIDAD +" as "+ProyectoDBMetadata.TablaPrioridadMetadata.PRIORIDAD_ALIAS+
            ", "+ProyectoDBMetadata.TablaTareasMetadata.RESPONSABLE +
            ", "+ProyectoDBMetadata.TABLA_USUARIOS_ALIAS+"."+ProyectoDBMetadata.TablaUsuariosMetadata.USUARIO +" as "+ProyectoDBMetadata.TablaUsuariosMetadata.USUARIO_ALIAS+
            " FROM "+ProyectoDBMetadata.TABLA_PROYECTO + " "+ProyectoDBMetadata.TABLA_PROYECTO_ALIAS+", "+
            ProyectoDBMetadata.TABLA_USUARIOS + " "+ProyectoDBMetadata.TABLA_USUARIOS_ALIAS+", "+
            ProyectoDBMetadata.TABLA_PRIORIDAD + " "+ProyectoDBMetadata.TABLA_PRIORIDAD_ALIAS+", "+
            ProyectoDBMetadata.TABLA_TAREAS + " "+ProyectoDBMetadata.TABLA_TAREAS_ALIAS+
            " WHERE "+ProyectoDBMetadata.TABLA_TAREAS_ALIAS+"."+ProyectoDBMetadata.TablaTareasMetadata.PROYECTO+" = "+ProyectoDBMetadata.TABLA_PROYECTO_ALIAS+"."+ProyectoDBMetadata.TablaProyectoMetadata._ID +" AND "+
            ProyectoDBMetadata.TABLA_TAREAS_ALIAS+"."+ProyectoDBMetadata.TablaTareasMetadata.RESPONSABLE+" = "+ProyectoDBMetadata.TABLA_USUARIOS_ALIAS+"."+ProyectoDBMetadata.TablaUsuariosMetadata._ID +" AND "+
            ProyectoDBMetadata.TABLA_TAREAS_ALIAS+"."+ProyectoDBMetadata.TablaTareasMetadata.PRIORIDAD+" = "+ProyectoDBMetadata.TABLA_PRIORIDAD_ALIAS+"."+ProyectoDBMetadata.TablaPrioridadMetadata._ID +" AND "+
            ProyectoDBMetadata.TABLA_TAREAS_ALIAS+"."+ProyectoDBMetadata.TablaTareasMetadata.PROYECTO+" = ?";

    private ProyectoOpenHelper dbHelper;
    private SQLiteDatabase db;

    public ProyectoDAO(Context c){
        this.dbHelper = new ProyectoOpenHelper(c);
    }

    public void open(){
        this.open(false);
    }

    public void open(Boolean toWrite){
        if(toWrite) {
            db = dbHelper.getWritableDatabase();
        }
        else{
            db = dbHelper.getReadableDatabase();
        }
    }

    public void close(){
        db = dbHelper.getReadableDatabase();
    }

    public Cursor listaTareas(Integer idProyecto){
        Cursor cursorPry = db.rawQuery("SELECT "+ProyectoDBMetadata.TablaProyectoMetadata._ID+ " FROM "+ProyectoDBMetadata.TABLA_PROYECTO,null);
        Integer idPry= 0;
        if(cursorPry.moveToFirst()){
            idPry=cursorPry.getInt(0);
        }
        cursorPry.close();
        Cursor cursor = null;
        Log.d("LAB05-MAIN","PROYECTO : _"+idPry.toString()+" - "+ _SQL_TAREAS_X_PROYECTO);
        cursor = db.rawQuery(_SQL_TAREAS_X_PROYECTO,new String[]{idPry.toString()});
        return cursor;
    }

    public Cursor getTarea(Integer idTarea){

        Cursor cursor = db.rawQuery("SELECT DESCRIPCION,HORAS_PLANIFICADAS,MINUTOS_TRABAJDOS,ID_PRIORIDAD,ID_RESPONSABLE,ID_PROYECTO,FINALIZADA FROM TAREA WHERE _id = ?", new String[] {""+idTarea+""});

        Tarea tarea = new Tarea();

        return cursor;

    }

    public void nuevaTarea(Tarea t){

        String sql = "INSERT INTO TAREA (DESCRIPCION,HORAS_PLANIFICADAS,MINUTOS_TRABAJDOS,ID_PRIORIDAD,ID_RESPONSABLE,ID_PROYECTO,FINALIZADA) VALUES ('"+t.getDescripcion()+"'," + t.getHorasEstimadas()+"," + t.getMinutosTrabajados()+","+ t.getPrioridad().getId() + "," + t.getResponsable().getId()+",1,0)";
        //db.rawQuery()
        db.execSQL(sql);

        Log.d("llegamos 1","sabe 1");

    }

    public void actualizarTarea(Tarea t){

        //TODO

        ContentValues registro = new ContentValues();

        //registro.put("_ID",t.getId());

        registro.put(ProyectoDBMetadata.TablaTareasMetadata.MINUTOS_TRABAJADOS, t.getMinutosTrabajados());

        db.update(ProyectoDBMetadata.TABLA_TAREAS, registro, "_ID="+t.getId(), null);

    }

    public void actualizarTareaCompleta(Tarea t){

        //TODO

        ContentValues registro = new ContentValues();

        //registro.put("_ID",t.getId());

        registro.put(ProyectoDBMetadata.TablaTareasMetadata.TAREA, t.getDescripcion().toString());

        registro.put(ProyectoDBMetadata.TablaTareasMetadata.HORAS_PLANIFICADAS, t.getHorasEstimadas().toString());

        registro.put(ProyectoDBMetadata.TablaTareasMetadata.PRIORIDAD, t.getPrioridad().getId());

        registro.put(ProyectoDBMetadata.TablaTareasMetadata.RESPONSABLE, t.getResponsable().getId());

        db.update(ProyectoDBMetadata.TABLA_TAREAS, registro, "_ID="+t.getId(), null);

    }

    public void borrarTarea(Integer id_tarea){

        db.delete(ProyectoDBMetadata.TABLA_TAREAS,"_ID="+id_tarea,null);
        Log.d("BORRAR TAREA: ","Se ha borrado la tarea de id: "+ id_tarea);

    }

    public List<Prioridad> listarPrioridades(){
        return null;
    }

    public Cursor listarUsuarios(){

        Cursor cursor = db.rawQuery("SELECT "+ProyectoDBMetadata.TablaUsuariosMetadata._ID+" as _id, "+ProyectoDBMetadata.TablaUsuariosMetadata.USUARIO+
                " FROM "+ProyectoDBMetadata.TABLA_USUARIOS,null);;

        while (cursor.moveToNext()){
            Log.d("USUARIO", cursor.getString(1));
            Log.d("NOMBRE", cursor.getColumnName(1));
        }


        return cursor;
    }

    public void finalizar(Integer idTarea){
        //Establecemos los campos-valores a actualizar
        ContentValues valores = new ContentValues();
        valores.put(ProyectoDBMetadata.TablaTareasMetadata.FINALIZADA,1);
        SQLiteDatabase mydb =dbHelper.getWritableDatabase();
        mydb.update(ProyectoDBMetadata.TABLA_TAREAS, valores, "_id=?", new String[]{idTarea.toString()});
    }

    public List<Tarea> listarDesviosPlanificacion(Boolean soloTerminadas,Integer desvioMaximoMinutos){
        // retorna una lista de todas las tareas que tardaron más (en exceso) o menos (por defecto)
        // que el tiempo planificado.
        // si la bandera soloTerminadas es true, se busca en las tareas terminadas, sino en todas.
        return null;
    }


}
