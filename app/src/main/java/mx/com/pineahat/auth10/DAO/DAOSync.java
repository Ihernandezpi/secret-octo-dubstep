package mx.com.pineahat.auth10.DAO;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Connection;

import mx.com.pineahat.auth10.utilerias.Conexion;

/**
 * Created by ${Ignacio} on 19/07/2015.
 */
public class DAOSync {
    private Context context;
    public DAOSync(Context context)
    {
        this.context = context;
    }

    public JSONArray getActividades (String date, String dateS)
    {
        JSONArray miArray = new JSONArray();
        Conexion miConexion = new Conexion(context);
        SQLiteDatabase bd = miConexion.getBD();
        try
        {
            JSONObject infoDispositivo = new JSONObject();

            //infoDispositivo.put("idDispo", Build.SERIAL);
            //infoDispositivo.put("ultimaFecha",date);
            String query ="select * from actividades where datetime(fechaCreacion) > datetime('"+date+"');";
            Cursor resp = bd.rawQuery(query,null);
            if(resp.moveToFirst())
            {
                do{
                    if(resp.getString(7).equals("Activo") || resp.getString(7).equals("Papelera") || resp.getString(7).equals("Terminado")) {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("tipoAccion", "actualizar");
                        jsonObject.put("idActividades", resp.getString(0));
                        jsonObject.put("idAsignacion", resp.getString(1));
                        jsonObject.put("nombre", resp.getString(2));
                        jsonObject.put("descripcion", resp.getString(3));
                        jsonObject.put("fechaCreacion", resp.getString(4));
                        jsonObject.put("fechaRealizacion", resp.getString(5));
                        jsonObject.put("fechaActualizacion", resp.getString(6));
                        jsonObject.put("estado", resp.getString(7));
                        jsonObject.put("tipo", resp.getString(8));
                        jsonObject.put("color", resp.getString(9));
                        miArray.put(jsonObject);
                    }
                }while (resp.moveToNext());
            }
            else
            {
                infoDispositivo.put("tipoAccion", "dispositivo");
                infoDispositivo.put("ultimaFecha",dateS);

                miArray.put(infoDispositivo);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally {
            bd.close();
            miConexion.close();
        }
        return  miArray;
    }

    public boolean insertarInfo(JSONArray array)
    {
        boolean resp = false;
        try {
            for (int i = 1 ;i<=array.length();i++) {
                JSONObject jsonObject = array.getJSONObject(i);
                if (jsonObject.getString("tipo").equals("actualizar")) {

                    actualizar(jsonObject);

                } else if (jsonObject.getString("tipo").equals("eliminar")) {

                    eliminar(jsonObject.getString("idActividades"));

                }
            }
        }catch (Exception e)
        {

        }
        return resp;
    }
    private boolean ifExist(String id)
    {
        boolean resp = false;

        return resp;

    }
    private void eliminar(String id)
    {
        Conexion conexion = new Conexion(context);
        SQLiteDatabase bd = conexion.getBD();
        try
        {
            String query="update actividades set estado='Eliminado' where idActividades = "+id+";";
            bd.rawQuery(query,null);

        }
        catch (Exception e)
        {

        }
        finally {
            bd.close();
            conexion.close();
        }
    }
    private void actualizar(JSONObject jsonObject)
    {
        //Revisar si existe
        Conexion con = new Conexion(context);
        SQLiteDatabase bd = con.getBD();
        try
        {
            if(ifExist(jsonObject.getString("idActividades")))
            {
                String query ="update";
            }
            else
            {
                String query ="insert into";
            }
        }catch (Exception e)
        {

        }

    }
}
