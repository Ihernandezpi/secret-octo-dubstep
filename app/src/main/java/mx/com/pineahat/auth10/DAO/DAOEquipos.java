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

import mx.com.pineahat.auth10.Equipo;
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



    public String generatePrimaryKeyEquipo() {
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




    public void insertarEquiposActividadesTI(String key,JSONArray jsonArrayEquipos)
    {
        Conexion conexion = new Conexion(context);
        SQLiteDatabase bd=conexion.getBD();

        Calendar calendar= Calendar.getInstance();
        Date rightNow = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strDate = sdf.format(rightNow.getTime());
        JSONArray integrantes;
        JSONArray alumnos;
        try
        {
            for (int i=0; i<jsonArrayEquipos.length();i++)
            {


                JSONObject mijsonObject= jsonArrayEquipos.getJSONObject(i);
                String primaryKey;
                if(mijsonObject.get("idEquiposActividades").toString().equals("null"))
                {
                    primaryKey=generatePrimaryKeyEquipo();

                }
                else
                {
                    primaryKey=mijsonObject.get("idEquiposActividades").toString();
                }

                if(mijsonObject.getBoolean("checked")==true)
                {
                    String query ="INSERT OR REPLACE INTO equiposActividades VALUES ('"+primaryKey+"','"+key+"', '"+mijsonObject.get("nombreEquipo").toString()+"', '"+strDate+"', 'Activo', "+mijsonObject.get("idEquiposti").toString()+");";
                    bd.execSQL(query);
                    // cada vez que un equipo se agregue de ti tambien se agregaran sus integranteso se mofifican sus integrantes
                    try
                    {
                        integrantes= new JSONArray();
                        alumnos= new JSONArray();
                        // se verifica si en ls tabla Integrantes existen alumnos relacionados con esa equiposActividades para ello se manda el idEquiposActivdades
                        integrantes=getIntegrantesActividad(primaryKey);
                        //integrantes=getIntegrantesEquipoTI(mijsonObject.get("idEquiposti").toString());
                        //Si no existen registros entonces se van a agregar los alumnos correspondientes a cada equipo ti con estado activo
                        if(integrantes==null)
                        {
                            // en este jsonArray se van a almacenar los alumnos de cierto equipo integrador
                            alumnos=getIntegrantesEquipoTI(mijsonObject.get("idEquiposti").toString());
                            // se hace el for para  para insertar cada alumno en la tabla Integrantes deacuerdo a un EquiposActividad
                            for(int j=0; j<alumnos.length();j++)
                            {
                                JSONObject integrante= alumnos.getJSONObject(j);
                                query="insert into integrantes values ('"+generatePrimaryKeyIntegrante()+"','"+primaryKey+"','"+integrante.get("idAlumno")+"','10','"+strDate+"','Activo')";
                                bd.execSQL(query);

                            }}
                        else
                        {
                            //pero si ya hay registros en la tabla integrantes se toman se toman los datos para modificarlos
                            for(int j=0; j<integrantes.length();j++)
                            {
                                JSONObject integrante= integrantes.getJSONObject(j);
                                query="insert or replace into integrantes values ('"+integrante.get("idIntegrantes").toString()+"','"+integrante.get("idEquiposActividades").toString()+"','"+integrante.get("idAlumno").toString()+"','"+integrante.get("calificacion").toString()+"','"+strDate+"','Activo')";
                                bd.execSQL(query);

                            }
                        }

                    }catch (Exception e)
                    {

                    }


                    // se tiene qe insertar por cada equipos los integrantes de cada equipo en la tabla integrantes en este caso su estado es Activo
                }
                else
                {
                    String query ="INSERT OR REPLACE INTO equiposActividades VALUES ('"+primaryKey+"','"+key+"', '"+mijsonObject.get("nombreEquipo").toString()+"', '"+strDate+"', 'Inactivo', "+mijsonObject.get("idEquiposti").toString()+");";
                    bd.execSQL(query);
                    // se tiene que insertar por cada equipo los integrantes de cada equipos en la tabla integrantes en este caso su estado es Inactivo

                }

            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        finally {
            bd.close();
            conexion.close();
        }

    }
    public  JSONArray getIntegrantesActividad(String idEquiposActividades)
    {
        JSONArray miJsonArray = null;
        Conexion conexion = new Conexion(context);
        SQLiteDatabase bd = conexion.getBD();
        String query ="select * from integrantes where idEquiposActividades='"+idEquiposActividades+"';";
        try
        {
            Cursor resp=bd.rawQuery(query,null);
            if(resp.moveToFirst())
            {
                miJsonArray= new JSONArray();
                do {
                    JSONObject integrante = new JSONObject();
                    integrante.put("idIntegrantes",resp.getString(0));
                    integrante.put("idEquiposActividades", resp.getString(1));
                    integrante.put("idAlumno", resp.getString(2));
                    integrante.put("calificacion", resp.getString(3));
                    integrante.put("fechaModi", resp.getString(4));
                    integrante.put("estado", resp.getString(5));
                    miJsonArray.put(integrante);
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

    public  boolean eliminarEquipo(Equipo equipoObject)
    {
        Equipo equipo= equipoObject;
        Conexion conexion = new Conexion(context);
        SQLiteDatabase bd=conexion.getBD();

        Calendar calendar= Calendar.getInstance();
        Date rightNow = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strDate = sdf.format(rightNow.getTime());

        String query ="UPDATE equiposActividades\n" +
                "set estado='Inactivo'\n" +
                "where idEquiposActividades='"+equipoObject.getIdEquiposActividades()+"';";
        try
        {
            bd.execSQL(query);
            return  true;
        }
        catch (Exception e)
        {
            return false;
        }


    }

    public JSONArray getIntegrantesEquipoTI(String idEquipoTI)
    {
        JSONArray miJsonArray = null;
        Conexion conexion = new Conexion(context);
        SQLiteDatabase bd = conexion.getBD();
        String query ="select * from alumnos where idEquiposti='"+idEquipoTI+"';";
        try
        {
            Cursor resp=bd.rawQuery(query,null);
            if(resp.moveToFirst())
            {
                miJsonArray= new JSONArray();
                do {
                    JSONObject equipo = new JSONObject();
                    equipo.put("idAlumno",resp.getString(0));
                    equipo.put("matricula",resp.getString(1));
                    equipo.put("idPersona",resp.getString(2));
                    equipo.put("idGrupo",resp.getString(3));
                    equipo.put("idEquiposti",resp.getString(4));
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
    public ArrayList<Equipo> equipoArrayList(String idActividades)
    {
        ArrayList<Equipo> arrayEquipos;
        Conexion conexion= new Conexion(context);
        SQLiteDatabase bd = conexion.getBD();
        arrayEquipos= new ArrayList<Equipo>();
        String query ="SELECT idEquiposActividades, nombre, fechaModi,estado, idEquipoTI FROM equiposActividades   where idActividades='"+idActividades+"' and estado='Activo';";
        try
        {

            Cursor resp=bd.rawQuery(query,null);
            if(resp.moveToFirst())
            {
                do {
                    Equipo equipo = new Equipo();
                    equipo.setIdEquiposActividades(resp.getString(0));
                    equipo.setNombre(resp.getString(1));
                    equipo.setFechaModi(resp.getString(2));
                    equipo.setEstado(resp.getString(3));
                    equipo.setIdEquipoTI(resp.getString(4));
                    arrayEquipos.add(equipo);
                }
                while(resp.moveToNext());
            }
        }catch (Exception e)
        {
/**/
        }

        return arrayEquipos;
    }


}
