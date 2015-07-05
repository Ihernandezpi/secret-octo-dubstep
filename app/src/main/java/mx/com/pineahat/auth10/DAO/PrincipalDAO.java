package mx.com.pineahat.auth10.DAO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import mx.com.pineahat.auth10.R;
import mx.com.pineahat.auth10.utilerias.Conexion;

/**
 * Created by Stephani on 02/07/2015.
 */
public class PrincipalDAO {

    public JSONArray grupos(String idProfesor, Context context)
    {
        JSONObject miJsonObject = null;
        JSONArray arrayJson= null;
        Conexion miConexion = new Conexion(context);
        SQLiteDatabase miDB = miConexion.getBD();
        //
        try {
            String consulta = "SELECT asignacion.idAsignacion, grupo.grado, grupo.grupo, materia.materia FROM asignacion,materia, grupo WHERE idProfesor="+idProfesor+" AND materia.idMateria = asignacion.idMateria AND grupo.idGrupo = asignacion.idGrupo;";
            Cursor cursor = miDB.rawQuery(consulta,null);
            if(cursor.moveToFirst())
            {
                arrayJson = new JSONArray();
                do
                {
                    miJsonObject= new JSONObject();
                    try {
                     //   miJsonObject.put("icono", R.mipmap.ic_action_group);
                        miJsonObject.put("idAsignacion",cursor.getString(0));
                        miJsonObject.put("grado", cursor.getString(1));
                        miJsonObject.put("grupo",cursor.getString(2));
                        miJsonObject.put("materia",cursor.getString(3));


                        arrayJson.put(miJsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                while (cursor.moveToNext());
                miConexion.close();
            }
        }
        catch(Exception e)
        {

        }
        return arrayJson;
    }
}
