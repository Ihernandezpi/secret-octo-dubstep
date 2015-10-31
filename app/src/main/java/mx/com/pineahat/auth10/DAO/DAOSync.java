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
    private String date, dateS;
    private JSONArray infoProfe;

    public DAOSync(Context context, String myData) {
        this.context = context;
        try {
            this.infoProfe = new JSONArray(myData);
        }catch (Exception e)
        {

        }
    }
    /**
     * Trae todas las actividades que se deben subir aal servidor
     * @param date fecha del telefono de ultima actualización
     * @param dateS Fecha del server de ultima actualización
     * @return JSONAray con todas las actividades.
     */
    public JSONArray getActividades (String date, String dateS)
    {
         this.date = date;
         this.dateS = dateS;
        JSONArray miArray = new JSONArray();
        Conexion miConexion = new Conexion(context);
        SQLiteDatabase bd = miConexion.getBD();
        try
        {
            String query ="select * from actividades where datetime(fechaCreacion) > datetime('"+date+"');";
            Cursor resp = bd.rawQuery(query,null);
            if(resp.moveToFirst())
            {
                do{
                    if(resp.getString(7).equals("Activo") || resp.getString(7).equals("Papelera") || resp.getString(7).equals("Terminado")) {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("nombreTabla","actividades");
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

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally {
            bd.close();
            miConexion.close();
        }
        return  getEquiposActividades(miArray);
    }

    private JSONArray getEquiposActividades (JSONArray actividades)
    {
        String query="select * from equiposActividades where datetime(fechaModi) > datetime('"+this.date+"');";
        Conexion con = new Conexion(this.context);
        SQLiteDatabase bd = con.getBD();
        try
        {

            Cursor miCursor = bd.rawQuery(query,null);
            if(miCursor.moveToFirst())
            {
                do {
                    JSONObject equipos = new JSONObject();
                    equipos.put("nombreTabla","equiposactividades");
                    equipos.put("tipoAccion", "actualizar");
                    equipos.put("idEquiposActividades",miCursor.getString(0));
                    equipos.put("idActividades",miCursor.getString(1));
                    equipos.put("nombre",miCursor.getString(2));
                    equipos.put("fechaModi",miCursor.getString(3));
                    equipos.put("estado",miCursor.getString(4));
                    if(miCursor.isNull(5))
                        equipos.put("idEquipoTI","");
                    else
                        equipos.put("idEquipoTI",miCursor.getString(5));

                    if(miCursor.isNull(6))
                        equipos.put("fechaActualizacion","");
                    else
                        equipos.put("fechaActualizacion",miCursor.getString(6));


                    actividades.put(equipos);

                }while (miCursor.moveToNext());
            }

        }catch (Exception e )
        {
            Log.d("Agregar equipos: ", e.getMessage());
        }
        finally {
            bd.close();
            con.close();
        }
        return getIntegrantesEquipos(actividades);
    }

    private JSONArray getIntegrantesEquipos(JSONArray equiposActividades)
    {
        String query="select * from integrantes where datetime(fechaModi) > datetime('"+this.date+"');";
        Conexion con = new Conexion(this.context);
        SQLiteDatabase bd = con.getBD();
        try
        {

            Cursor miCursor = bd.rawQuery(query,null);
            if(miCursor.moveToFirst())
            {
                do {
                    JSONObject integrantes = new JSONObject();
                    integrantes.put("nombreTabla","integrantes");
                    integrantes.put("tipoAccion", "actualizar");
                    integrantes.put("idIntegrantes",miCursor.getString(0));
                    integrantes.put("idEquiposActividades",miCursor.getString(1));
                    integrantes.put("idAlumno",miCursor.getString(2));
                    if(miCursor.isNull(3))
                        integrantes.put("calificacion"," ");
                    else
                    integrantes.put("calificacion",miCursor.getString(3));
                    integrantes.put("fechaModi",miCursor.getString(4));
                    integrantes.put("estado",miCursor.getString(5));
                    if(miCursor.isNull(6))
                        integrantes.put("fechaActualizacion"," ");
                    else
                        integrantes.put("fechaActualizacion",miCursor.getString(6));
                    equiposActividades.put(integrantes);

                }while (miCursor.moveToNext());
            }

        }catch (Exception e )
        {
            Log.d("Agregar equipos: ",e.getMessage());
        }
        finally {
            bd.close();
            con.close();
        }

        return putJsonDispositivo(equiposActividades);
    }
    private JSONArray putJsonDispositivo (JSONArray paquete)
    {
        try {
            JSONObject infoDispositivo = new JSONObject();
            infoDispositivo.put("tipoAccion", "dispositivo");
            infoDispositivo.put("ultimaFecha", dateS);
            infoDispositivo.put("idProfesor", infoProfe.getJSONObject(0).getString("idProfesor"));
            paquete.put(infoDispositivo);
        }catch (Exception e)
        {
            Log.d("TIpo Acción dispo",e.getMessage());
        }
        return fechaSync(paquete);
    }
    private JSONArray fechaSync (JSONArray paquete)
    {
        try {
            JSONObject infoDispositivo = new JSONObject();
            infoDispositivo.put("tipoAccion", "fechaSync");
            paquete.put(infoDispositivo);
        }catch (Exception e)
        {
            Log.d("TIpo Acción dispo",e.getMessage());
        }
        return paquete;
    }
}
