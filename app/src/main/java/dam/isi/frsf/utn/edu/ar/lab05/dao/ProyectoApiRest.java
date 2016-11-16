package dam.isi.frsf.utn.edu.ar.lab05.dao;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import dam.isi.frsf.utn.edu.ar.lab05.modelo.Proyecto;

/**
 * Created by martdominguez on 20/10/2016.
 */
public class ProyectoApiRest {

    public void crearProyecto(Proyecto p){

    }
    public void borrarProyecto(Integer id){

    }
    public void actualizarProyecto(Proyecto p){

    }
    public ArrayList<String> listarProyectos(){
        ArrayList<String> listaProyectos = new ArrayList<String>();
        RestClient cliRest = new RestClient();
        JSONArray array = cliRest.getByAll(null, "proyectos");
        for(int i=0; i<array.length(); i++){
            try {
                JSONObject o = array.getJSONObject(i);
                listaProyectos.add(o.getString("nombre"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        Log.d("listaProyectosAString: ",listaProyectos.toString());
        return listaProyectos;
    }

    public Proyecto buscarProyecto(Integer id){
        RestClient cliRest = new RestClient();
        JSONObject t = cliRest.getById(1,"proyectos");
        // transformar el objeto JSON a proyecto y retornarlo

        Proyecto p= new Proyecto();
        try {
            Integer idS = t.getInt("id");
            p.setId(idS);

            String nombre = t.getString("nombre");
            p.setNombre(nombre);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return p;
    }

}