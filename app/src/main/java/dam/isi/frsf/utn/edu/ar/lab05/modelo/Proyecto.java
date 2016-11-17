package dam.isi.frsf.utn.edu.ar.lab05.modelo;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by mdominguez on 06/10/16.
 */
public class Proyecto {

    private Integer id;
    private String nombre;

    public Proyecto() {

    }


    public Proyecto(Integer id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public JSONObject toJSON(){

        JSONObject jsonObject= new JSONObject();
        try {
            jsonObject.put("id", getId()+"");
            jsonObject.put("nombre", getNombre());

            return jsonObject;
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }

    }
}
