package mx.com.pineahat.auth10.DAO;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import mx.com.pineahat.auth10.ActividadEditable;
import mx.com.pineahat.auth10.utilerias.Conexion;

/**
 * Created by ${Ignacio} on 21/07/2015.
 */
public class DAOActividades {
    private Context context;
    public DAOActividades (Context context)
    {
        this.context=context;
    }
    public String insertActividades(String idAsignacion)
    {
        Conexion conexion = new Conexion(context);
        SQLiteDatabase bd = conexion.getBD();
        String key = generatePrimaryKey();
        Calendar calendar= Calendar.getInstance();
        Date rightNow = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strDate = sdf.format(rightNow.getTime());
        try
        {
            String query ="INSERT INTO actividades VALUES ('"+key+"', "+idAsignacion+",'','','"+strDate+"','','', 'Activo', 1, '#bdbdbd');";
            bd.execSQL(query);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        finally {
            bd.close();
            conexion.close();
        }
        return key;

    }

    private String generatePrimaryKey() {
        String key = null;
        String serial = Build.SERIAL;
        Conexion con = new Conexion(context);
        SQLiteDatabase bd = con.getBD();
        Calendar calendar= Calendar.getInstance();
        Date miDate =calendar.getTime();
        key = serial+miDate.getTime();
        try
        {
            String query ="select idActividades from actividades where idActividades ='"+key+"'";
            Cursor resp=bd.rawQuery(query, null);
            if(resp.moveToFirst())
            {
                return generatePrimaryKey();
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


    public void actualizar(String key,String titulo, String descripcion, String colorSelect, int year, int monthOfYear, int dayOfMonth,  int hourOfDay, int minute,boolean flag) {
        Conexion miConexion = new Conexion(context);
        SQLiteDatabase bd = miConexion.getBD();
        DateFormat miDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar miCalendar =Calendar.getInstance();
        String fecha = miDateFormat.format(miCalendar.getTime());
        String realizacion ="";
        if (year!=0 & monthOfYear!=0 & dayOfMonth!=0&hourOfDay!=0&minute!=0&flag!=true)
        {
            miCalendar.set(year,monthOfYear-1,dayOfMonth,hourOfDay,minute,0);
            DateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String h =time.format(miCalendar.getTime());
            realizacion=miDateFormat.format(miCalendar.getTime());
        }
        try
        {
            String query="UPDATE actividades SET nombre ='"+titulo+"', descripcion='"+descripcion+"', fechaCreacion='"+fecha+"', \"fechaRealizacion\"='"+realizacion+"', \"fechaActualizacion\"='', \"estado\"='Activo', \"tipo\"=1, \"color\"='"+colorSelect+"' WHERE (\"idActividades\"='"+key+"')";
            bd.execSQL(query);

        }catch (Exception e)
        {
            e.printStackTrace();
        }
        finally {
            bd.close();
            miConexion.close();
        }
    }

    public void eliminar(JSONObject miJson) {
        Calendar calendar= Calendar.getInstance();
        Date rightNow = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strDate = sdf.format(rightNow.getTime());
        Conexion con = new Conexion(context);
        SQLiteDatabase bd = con.getBD();
        try
        {
            String query ="UPDATE actividades SET fechaCreacion='"+strDate+"', estado ='Terminado' WHERE (idActividades='"+miJson.getString("idActividades")+"')";
            bd.execSQL(query);

        }
        catch (Exception e)
        {

        }
        finally {
            bd.close();
            con.close();
        }

    }

    // Stephani Equipos

    public JSONArray equiposTI(String key,String idAsignacion)
    {
        JSONArray listaEquipos= null;
        Conexion conexion = new Conexion(context);
        SQLiteDatabase db= conexion.getBD();
        String query;
        if(key==null)
        {
            query="select ti.idEquiposti,ti.nombreEquipo,'Inactivo' as estado, NULL as idEquiposActividades from asignacion as asig \n" +
                    "inner join grupo as g on(g.idGrupo=asig.idGrupo)\n" +
                    "INNER JOIN equiposti as ti on(g.idGrupo=ti.idGrupo)\n" +
                    "where asig.idAsignacion='"+idAsignacion+"';";


        }
        else
        {
            query="SELECT eqti.idEquiposti, eqti.nombreEquipo, eqact.estado,eqact.idEquiposActividades\n" +
                    "FROM equiposActividades AS eqact\n" +
                    "INNER JOIN actividades AS act ON (act.idActividades=eqact.idActividades)\n" +
                    "INNER JOIN equiposti as eqti ON (eqti.idEquiposti=eqact.idEquipoTI)\n" +
                    "where act.idActividades='"+key+"';";
        }
        Cursor cursor= db.rawQuery(query,null);
        if(cursor.moveToFirst())
        {
            listaEquipos = new JSONArray();
            do{
                JSONObject equipo = new JSONObject();
                try
                {
                    equipo.put("idEquiposti",cursor.getString(0));
                    equipo.put("nombreEquipo",cursor.getString(1));
                    equipo.put("estado",cursor.getString(2));
                    if(equipo.get("estado").equals("Activo"))
                        equipo.put("checked", true);
                    else
                        equipo.put("checked", false);
                    if(cursor.isNull(3))
                        equipo.put("idEquiposActividades","null");
                    else
                        equipo.put("idEquiposActividades",cursor.getString(3));
                    listaEquipos.put(equipo);

                }
                catch (Exception e)
                {
                }
            }
            while(cursor.moveToNext());

        }
        conexion.close();
        db.close();
        return listaEquipos;
    }
}
