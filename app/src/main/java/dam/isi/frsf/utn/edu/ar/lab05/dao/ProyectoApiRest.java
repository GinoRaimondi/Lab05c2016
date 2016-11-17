package dam.isi.frsf.utn.edu.ar.lab05.dao;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import dam.isi.frsf.utn.edu.ar.lab05.R;
import dam.isi.frsf.utn.edu.ar.lab05.modelo.Proyecto;

/**
 * Created by martdominguez on 20/10/2016.
 */
public class ProyectoApiRest {

    public void crearProyecto(Proyecto p){

        RestClient cliRest = new RestClient();

        // Crear Object json a partir de p.

        cliRest.crearProyecto(p,"proyectos");

    }
    public void borrarProyecto(Integer id){
        RestClient cliRest = new RestClient();
        cliRest.borrar(id,"proyectos");
    }

    public Integer getMaxId() throws JSONException {

        RestClient cliRest = new RestClient();

        // Crear Object json a partir de p.

        JSONArray j = cliRest.getByAll(null,"proyectos");

        Integer maxId = 1;

        for (int i=0; i<j.length(); i++) {
            JSONObject actor = j.getJSONObject(i);
            String name = actor.getString("id");

            Integer val = Integer.parseInt(name);
            if (val>maxId)
                maxId = val;

            //Log.d("nooooombre ", name );
            //allNames.add(name);
        }

        return maxId +1;

    }

    public void actualizarProyecto(Proyecto p){

    }

    public ArrayList<ArrayList> listarProyectos(){
        ArrayList<String> listaProyectos = new ArrayList<String>();
        ArrayList<Integer> idProyectos = new ArrayList<Integer>();
        ArrayList<ArrayList> resultado = new ArrayList<ArrayList>();
        RestClient cliRest = new RestClient();
        JSONArray array = cliRest.getByAll(null, "proyectos");
        for(int i=0; i<array.length(); i++){
            try {
                JSONObject o = array.getJSONObject(i);
                listaProyectos.add(o.getString("nombre"));
                idProyectos.add(o.getInt("id"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        resultado.add(idProyectos);
        resultado.add(listaProyectos);
        Log.d("listaProyectosAString: ",listaProyectos.toString());
        return resultado;
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