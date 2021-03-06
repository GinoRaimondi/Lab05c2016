package dam.isi.frsf.utn.edu.ar.lab05.modelo;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by mdominguez on 06/10/16.
 */
public class Usuario {

    private Integer id;
    private String nombre;
    private String correoElectronico;

    public Usuario(){

    }

    public Usuario(Integer id, String nombre, String correoElectronico) {
        this.id = id;
        this.nombre = nombre;
        this.correoElectronico = correoElectronico;
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

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public JSONObject toJSON(){

        JSONObject jsonObject= new JSONObject();
        try {
            jsonObject.put("id", getId()+"");
            jsonObject.put("nombre", getNombre());
            jsonObject.put("correoElectronico", getCorreoElectronico());

            return jsonObject;
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }

    }

}
