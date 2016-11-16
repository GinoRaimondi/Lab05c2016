package dam.isi.frsf.utn.edu.ar.lab05.dao;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import dam.isi.frsf.utn.edu.ar.lab05.modelo.Proyecto;

import static android.R.attr.id;
import static android.R.attr.path;

/**
 * Created by martdominguez on 20/10/2016.
 */
public class RestClient {

    private final String IP_SERVER = "192.168.1.105";
    private final String PORT_SERVER = "4000";
    private final String TAG_LOG = "LAB06";


    public JSONObject getById(Integer id,String path) {
        JSONObject resultado = null;
        HttpURLConnection urlConnection=null;
        try {
            URL url = new URL("http://"+IP_SERVER+":"+PORT_SERVER+"/"+path+"/"+id);
            Log.d("TAG_LOG",url.getPath()+ " --> "+url.toString());
            urlConnection= (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            InputStreamReader isw = new InputStreamReader(in);
            StringBuilder sb = new StringBuilder();

            int data = isw.read();
            while (data != -1) {
                char current = (char) data;
                sb.append(current);
                data = isw.read();
            }
            Log.d("TAG_LOG",url.getPath()+ " --> "+sb.toString());
            resultado = new JSONObject(sb.toString());

            //isw.close();
        }
        catch (IOException e) {
            Log.e("TEST-ARR",e.getMessage(),e);
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            if(urlConnection!=null) urlConnection.disconnect();
        }
        return resultado;
    }

    public JSONArray getByAll(Integer id,String path) {
        JSONArray resultado = null;
        HttpURLConnection urlConnection=null;
        try {
            URL url = new URL("http://"+IP_SERVER+":"+PORT_SERVER+"/"+path);
            Log.d("TAG_LOG",url.getPath()+ " --> "+url.toString());
            urlConnection= (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            InputStreamReader isw = new InputStreamReader(in);
            StringBuilder sb = new StringBuilder();

            int data = isw.read();
            while (data != -1) {
                char current = (char) data;
                sb.append(current);
                data = isw.read();
            }
            Log.d("TAG_LOG",url.getPath()+ " --> "+sb.toString());
            resultado = new JSONArray(sb.toString());
            Log.d("TAG_LOG_RESULTADO"," --> "+resultado);

            /*isw.close();
            in.close();*/
        }
        catch (IOException e) {
            Log.e("TEST-ARR",e.getMessage(),e);
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            if(urlConnection!=null) urlConnection.disconnect();
        }
        return resultado;
    }


    public void crear(Proyecto p,String path) {

        JSONObject resultado = null;
        HttpURLConnection urlConnection = null;


        StringBuffer chaine = new StringBuffer("");
        try {

            URL url = new URL("http://" + IP_SERVER + ":" + PORT_SERVER + "/" + path + "/" + p.getId());
            Log.d("url ", url.toString());
            //URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("User-Agent", "");
            connection.setRequestMethod("PUT");
            connection.setDoInput(true);


            connection.setDoOutput(true);
            /*connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");*/

            JSONObject jsonObject = getById(1, "proyectos");



            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            wr.writeBytes(jsonObject.toString());
            wr.flush();
            wr.close();



        } catch (IOException e) {
            // Writing exception to log
            e.printStackTrace();
        }
        //return chaine;

    }

    public void crearProyecto(Proyecto p, String path){

            HttpURLConnection urlConnection=null;
            //SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy mm:ss SSS");
            try {


                JSONObject nuevoObjeto= new JSONObject();
                nuevoObjeto = p.toJSON();
                //nuevoObjeto.put("nombre", p.getNombre());

                String str= nuevoObjeto.toString();
                byte[] data=str.getBytes("UTF-8");
                Log.d("EjemploPost","str---> "+str);


                URL url = new URL("http://"+IP_SERVER+":"+PORT_SERVER+"/"+path);
                Log.d("EjemploPost","str---> "+url.toString());
                // VER AQUI https://developer.android.com/reference/java/net/HttpURLConnection.html
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setRequestMethod("POST");
                urlConnection.setFixedLengthStreamingMode(data.length);
                urlConnection.setRequestProperty("Content-Type","application/json");
                DataOutputStream printout = new DataOutputStream(urlConnection.getOutputStream());

                printout.write(data);
                printout.flush ();
                printout.close ();
                Log.d("TEST-ARR","FIN!!! "+urlConnection.getResponseMessage());

            } catch (IOException e1) {
                Log.e("TEST-ARR",e1.getMessage(),e1);
                e1.printStackTrace();
            }finally {
                urlConnection.disconnect();
            }

    }



    public void actualizar(JSONObject objeto,String path) {
    }

    public void borrar(Integer id,String path) {
    }
}