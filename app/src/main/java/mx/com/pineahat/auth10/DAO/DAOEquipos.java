package mx.com.pineahat.auth10.DAO;

import android.content.Context;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.SQLData;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import mx.com.pineahat.auth10.Equipos.Integrantes;
import mx.com.pineahat.auth10.utilerias.Conexion;

/**
 * Created by Ignacio on 13/09/2015.
 */
public class DAOEquipos {
    private Context context;

    public DAOEquipos(Context context) {
        this.context = context;
    }


    /**
     * @param idActividad
     * @return
     */
    public JSONArray traerAlumnos(String idActividad)
    {
        JSONArray miLista = null;

        Conexion conexion = new Conexion(context);
        SQLiteDatabase bd = conexion.getBD();
        String query="select alum.idAlumno,p.nombre,p.apellidoP,p.apellidoM,alum.matricula from alumnos as alum INNER JOIN persona as p on(p.idPersona=alum.idPersona) where alum.idGrupo=(select asig.idGrupo from actividades as ac INNER JOIN asignacion as asig on (asig.idAsignacion=ac.idAsignacion) WHERE ac.idActividades='"+idActividad+"');";
        Cursor cursor = bd.rawQuery(query,null);
        if(cursor.moveToFirst())
        {
            miLista= new JSONArray();
            do {
                JSONObject alumno = new JSONObject();
                try {
                    alumno.put("idAlumno", cursor.getString(0));
                    alumno.put("nombre", cursor.getString(1));
                    alumno.put("apellidoP", cursor.getString(2));
                    alumno.put("apellidoM", cursor.getString(3));
                    alumno.put("matricula", cursor.getString(4));
                    alumno.put("estado", "fuera");
                    miLista.put(alumno);
                }catch (Exception e)
                {
                }
            }
            while (cursor.moveToNext());
        }
        conexion.close();
        bd.close();
        return miLista;
    }

    public void actualizarIntegrantes(ArrayList<Integrantes> miIntegrantes,String idEquipo)
    {


        Conexion conexion= new Conexion(context);
        SQLiteDatabase bd = conexion.getBD();
        String query = "delete from integrantes where integrantes.idEquiposActividades='"+idEquipo+"'";
        bd.execSQL(query);
        for (Integrantes integrante:miIntegrantes) {
            String primaryKey = generatePrimaryKeyIntegrante();
            DateFormat miDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar miCalendar = Calendar.getInstance();
            String fecha = miDateFormat.format(miCalendar.getTime());
            String query2 = "INSERT INTO integrantes(idIntegrantes, idEquiposActividades, idAlumno, fechaModi, estado) VALUES('" + primaryKey + "', '" + idEquipo + "', '"+integrante.getIdAlumno()+"', '"+fecha+"', 'Activo');";
            bd.execSQL(query2);
        }
        bd.close();
        conexion.close();
    }


    public String generarEquipo (String idActividad,String nombre)
    {
        Conexion conexion= new Conexion(context);
        SQLiteDatabase bd = conexion.getBD();
        String primaryKey=generatePrimaryKeyEquipo();
        DateFormat miDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar miCalendar =Calendar.getInstance();
        String fecha = miDateFormat.format(miCalendar.getTime());
        String query ="INSERT INTO equiposActividades (idEquiposActividades, idActividades, nombre, fechaModi, estado) VALUES ('"+primaryKey+"', '"+idActividad+"', '"+nombre+"', '"+fecha+"', 'Activo');";
        bd.execSQL(query);
        bd.close();
        conexion.close();
        return primaryKey;
    }



    private String generatePrimaryKeyEquipo() {
        String key = null;
        String serial = Build.SERIAL;
        Conexion con = new Conexion(context);
        SQLiteDatabase bd = con.getBD();
        Calendar calendar= Calendar.getInstance();
        Date miDate =calendar.getTime();
        key = serial+miDate.getTime();
        try
        {
            String query ="select idEquiposActividades from equiposActividades  where idEquiposActividades ='"+key+"'";
            Cursor resp=bd.rawQuery(query, null);
            if(resp.moveToFirst())
            {
                return generatePrimaryKeyEquipo();
            }
            else
            {
                return key;
            }

        } catch (Exception e)
        {

        }
        return key;
    }
    private String generatePrimaryKeyIntegrante() {
        String key = null;
        String serial = Build.SERIAL;
        Conexion con = new Conexion(context);
        SQLiteDatabase bd = con.getBD();
        Calendar calendar= Calendar.getInstance();
        Date miDate =calendar.getTime();
        key = serial+miDate.getTime();
        try
        {
            String query ="select idIntegrantes from integrantes where idIntegrantes ='"+key+"'";
            Cursor resp=bd.rawQuery(query, null);
            if(resp.moveToFirst())
            {
                return generatePrimaryKeyIntegrante();
            }
            else
            {
                return key;
            }

        } catch (Exception e)
        {

        }
        return key;
    }
}
