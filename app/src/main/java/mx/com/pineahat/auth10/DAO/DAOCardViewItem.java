package mx.com.pineahat.auth10.DAO;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import mx.com.pineahat.auth10.utilerias.Conexion;

/**
 * Created by ${Ignacio} on 03/07/2015.
 */
public class DAOCardViewItem {
    private Context miContext;
    private String idAsignacion;
    public DAOCardViewItem (Context miContext, String idAsignacion)
    {
        this.miContext= miContext;
        this.idAsignacion=idAsignacion;
    }

    public JSONArray getActividades ()
    {
        JSONArray miArray = new JSONArray();
        Conexion miConexion = new Conexion(miContext);
        SQLiteDatabase miDatabase = miConexion.getBD();
        JSONObject miJsonObject;
        try
        {
            String consulta="select * from actividades where idAsignacion='"+idAsignacion+"' and estado='Activo' ORDER BY datetime(fechaCreacion) DESC;";
            Cursor cursor = miDatabase.rawQuery(consulta, null);
            try{

                if(cursor.moveToFirst())
                {
                    do {
                        miJsonObject= new JSONObject();
                        try
                        {

                            miJsonObject.put("idActividades",cursor.getString(0));
                            miJsonObject.put("idAsignacion",cursor.getString(1));
                            miJsonObject.put("nombre",cursor.getString(2));
                            miJsonObject.put("descripcion",cursor.getString(3));
                            miJsonObject.put("fechaCreacion",cursor.getString(4));
                            miJsonObject.put("fechaRealizacion",cursor.getString(5));
                            miJsonObject.put("fechaActualizacion",cursor.getString(6));
                            miJsonObject.put("estado",cursor.getString(7));
                            miJsonObject.put("tipo",cursor.getString(8));
                            miJsonObject.put("color",cursor.getString(9));
                            miArray.put(miJsonObject);
                            miDatabase.close();
                            miConexion.close();
                        }catch(Exception e)
                        {

                        }
                    }
                    while (cursor.moveToNext());
                }
                miConexion.close();
                miDatabase.close();
            }
            catch (Exception e) {
            }

        }catch (Exception e)
        {
        }
        return  miArray;
    }
    public void moveTrash(String id)
    {
        Calendar calendar= Calendar.getInstance();
        Date rightNow = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strDate = sdf.format(rightNow.getTime());

        Conexion miConexion = new Conexion(miContext);
        SQLiteDatabase miDatabase = miConexion.getBD();
        try
        {
            String query ="UPDATE actividades SET fechaCreacion='"+strDate+"', estado ='Papelera' WHERE (idActividades='"+id+"')";

            miDatabase.execSQL(query);
        }
        catch (Exception e)
        {
            Toast.makeText(miContext,"Error al mover a la papelera",Toast.LENGTH_SHORT).show();
        }
    }
    public void returnActividades(String id)
    {
        Calendar calendar= Calendar.getInstance();
        Date rightNow = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strDate = sdf.format(rightNow.getTime());

        Conexion miConexion = new Conexion(miContext);
        SQLiteDatabase miDatabase = miConexion.getBD();
        try
        {
            String query ="UPDATE actividades SET fechaCreacion='"+strDate+"', estado ='Activo' WHERE (idActividades='"+id+"')";

            miDatabase.execSQL(query);
        }
        catch (Exception e)
        {
            Toast.makeText(miContext,"Error al regresar a las actividades",Toast.LENGTH_SHORT).show();
        }
    }

    public JSONArray getPapelera() {
        JSONArray miArray = new JSONArray();
        Conexion miConexion = new Conexion(miContext);
        SQLiteDatabase miDatabase = miConexion.getBD();
        JSONObject miJsonObject;
        try
        {
            String consulta="select * from actividades where estado='Papelera' ORDER BY datetime(fechaCreacion) DESC;";
            Cursor cursor = miDatabase.rawQuery(consulta, null);
            try{

                if(cursor.moveToFirst())
                {
                    do {
                        miJsonObject= new JSONObject();
                        try
                        {

                            miJsonObject.put("idActividades",cursor.getString(0));
                            miJsonObject.put("idAsignacion",cursor.getString(1));
                            miJsonObject.put("nombre",cursor.getString(2));
                            miJsonObject.put("descripcion",cursor.getString(3));
                            miJsonObject.put("fechaCreacion",cursor.getString(4));
                            miJsonObject.put("fechaRealizacion",cursor.getString(5));
                            miJsonObject.put("fechaActualizacion",cursor.getString(6));
                            miJsonObject.put("estado",cursor.getString(7));
                            miJsonObject.put("tipo",cursor.getString(8));
                            miJsonObject.put("color",cursor.getString(9));
                            miArray.put(miJsonObject);
                            miDatabase.close();
                            miConexion.close();
                        }catch(Exception e)
                        {

                        }
                    }
                    while (cursor.moveToNext());
                }
                miConexion.close();
                miDatabase.close();
            }
            catch (Exception e) {
            }

        }catch (Exception e)
        {
        }
        return  miArray;
    }
}
