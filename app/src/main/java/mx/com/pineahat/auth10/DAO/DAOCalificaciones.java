package mx.com.pineahat.auth10.DAO;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.json.JSONArray;
import org.json.JSONObject;

import mx.com.pineahat.auth10.utilerias.Conexion;

/**
 * Created by Ignacio on 21/09/2015.
 */
public class DAOCalificaciones {
    private Context context;

    public DAOCalificaciones(Context context) {
        this.context = context;
    }

    public JSONArray getIntegrantes(String idActividad)
    {
        JSONArray miJsonArray = null;
        Conexion conexion = new Conexion(context);
        SQLiteDatabase bd = conexion.getBD();
        String query ="select * from equiposActividades where idActividades='"+idActividad+"' and estado='Activo';";
        try
        {
            Cursor resp=bd.rawQuery(query,null);
            if(resp.moveToFirst())
            {
                miJsonArray= new JSONArray();
                do {
                    JSONObject equipo = new JSONObject();
                    equipo.put("idEquiposActividades",resp.getString(0));
                    equipo.put("idActividades",resp.getString(1));
                    equipo.put("nombre",resp.getString(2));
                    equipo.put("fechaModi",resp.getString(3));
                    equipo.put("estado",resp.getString(4));
                    //Traer alumnos
                    String idEquipo = resp.getString(0);
                    equipo.put("alumnos",traerAlumnosEquipos(idEquipo));
                    miJsonArray.put(equipo);
                }
                while(resp.moveToNext());
            }
        }
        catch (Exception e)
        {

        }
        bd.close();
        conexion.close();
        return miJsonArray;
    }
    private JSONArray traerAlumnosEquipos(String idEquipo)
    {
        JSONArray miJsonArray = null;
        Conexion con = new Conexion(context);
        SQLiteDatabase bd = con.getBD();
        try
        {
            String query ="select i.idIntegrantes,  i.calificacion, p.nombre, p.apellidoP from integrantes as i INNER JOIN alumnos as a on (a.idAlumno=i.idAlumno) INNER JOIN persona as p on (p.idPersona=a.idPersona) where i.idEquiposActividades='"+idEquipo+"' and i.estado='Activo';";
            Cursor resp = bd.rawQuery(query,null);
            if(resp.moveToFirst())
            {
                miJsonArray= new JSONArray();
                do {
                    JSONObject alumno = new JSONObject();
                    alumno.put("idIntegrantes",resp.getString(0));
                    alumno.put("calificacion",resp.getString(1));
                    alumno.put("nombre",resp.getString(2));
                    alumno.put("apellidoP",resp.getString(3));
                    miJsonArray.put(alumno);
                }while (resp.moveToNext());
            }
        }catch (Exception e)
        {

        }
        return miJsonArray;
    }
    public void putCalificacion (String idIntegrante, String calif)
    {
        SQLiteDatabase bd = new Conexion(context).getBD();
        String query ="update integrantes set calificacion='"+calif+"' where idIntegrantes='"+idIntegrante+"';";
        try
        {
            bd.execSQL(query);
        }catch (Exception e)
        {

        }
        bd.close();

    }
}
