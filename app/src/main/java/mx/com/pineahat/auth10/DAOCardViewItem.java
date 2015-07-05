package mx.com.pineahat.auth10;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.json.JSONArray;
import org.json.JSONObject;

import mx.com.pineahat.auth10.utilerias.Conexion;

/**
 * Created by ${Ignacio} on 03/07/2015.
 */
public class DAOCardViewItem {
    private Context miContext;
    public DAOCardViewItem (Context miContext)
    {
        this.miContext= miContext;
    }

    public JSONArray getActividades ()
    {
        JSONArray miArray = null;
        Conexion miConexion = new Conexion(miContext);
        SQLiteDatabase miDatabase = miConexion.getBD();
        JSONObject miJsonObject;
        try
        {
            String consulta="";
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
                            miJsonObject.put("estado",cursor.getString(5));
                            miJsonObject.put("tipo",cursor.getString(6));
                            miJsonObject.put("color",cursor.getString(7));
                            miArray.put(miJsonObject);
                        }catch(Exception e)
                        {

                        }
                    }
                    while (cursor.moveToNext());
                }
                miConexion.close();
            }
            catch (Exception e) {
            }

        }catch (Exception e)
        {
        }
        return  miArray;
    }
}
