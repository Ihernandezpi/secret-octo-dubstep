package mx.com.pineahat.auth10.DAO;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;

import mx.com.pineahat.auth10.Fechas.FechasFormateadas;
import mx.com.pineahat.auth10.utilerias.Conexion;

/**
 * Created by Ignacio on 29/10/2015.
 */
public class DAOArchivos {
    Context context;

    public DAOArchivos(Context context) {
        this.context = context;
    }

    public void insertarArchivo (String idActividades,String nombreArchivo,String tipo)
    {
        Conexion con = new Conexion(context);
        SQLiteDatabase bd = con.getBD();
        try
        {
            String query="INSERT INTO archivos VALUES ('"+createPrimaryKeyArchivos()+"', '"+idActividades+"', '"+nombreArchivo+"', '"+tipo+"', 'Activo', '"+ FechasFormateadas.getFecha()+"', '0000-00-00 00:00:00');";
            bd.execSQL(query);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public void eliminarArchivo(String idArchivo)
    {
        Conexion con = new Conexion(context);
        SQLiteDatabase bd = con.getBD();
        try
        {
            String query="update archivos set estado='Eliminado', fechaCreacion='"+FechasFormateadas.getFecha()+"' where idArchivo='"+idArchivo+"';";
            bd.execSQL(query);
        }catch (Exception e)
        {

        }
        bd.close();
        con.close();
    }
    public JSONArray getMultimedia(String idActividad)
    {
        JSONArray miJsonArray =null;
        Conexion con = new Conexion(context);
        SQLiteDatabase bd = con.getBD();
        try
        {
            String query="select * from archivos as a  where a.idActividades='"+idActividad+"' and a.estado!='Eliminado'  ORDER BY tipo DESC;";
            Cursor resp = bd.rawQuery(query,null);
            if(resp.moveToFirst())
            {
                miJsonArray= new JSONArray();
                do {
                    JSONObject miJsonObject = new JSONObject();
                    miJsonObject.put("idArchivo",resp.getString(0));
                    miJsonObject.put("idActividades",resp.getString(1));
                    miJsonObject.put("nombreArchivo",resp.getString(2));
                    miJsonObject.put("tipo",resp.getString(3));
                    miJsonObject.put("estado",resp.getString(4));
                    miJsonObject.put("fechaCreacion",resp.getString(5));
                    miJsonObject.put("fechaActualizacion",resp.getString(6));
                    miJsonArray.put(miJsonObject);
                }while (resp.moveToNext());
            }

        }catch (Exception e)
        {
            e.printStackTrace();
        }
        bd.close();
        con.close();

        return miJsonArray;
    }

    public boolean existeArchivo(String idActividad)
    {
        boolean respuesta = false;
        Conexion con = new Conexion(context);
        SQLiteDatabase bd = con.getBD();
        try
        {
            String query="select * from archivos as a  where a.idActividades='"+idActividad+"' and a.tipo='Archivo' and a.estado='Activo';";
            Cursor resp = bd.rawQuery(query,null);
            if(resp.moveToFirst())
                respuesta=true;
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        bd.close();
        con.close();

        return respuesta;
    }

    private String createPrimaryKeyArchivos() {
        String key = null;
        String serial = Build.SERIAL;
        Conexion con = new Conexion(context);
        SQLiteDatabase bd = con.getBD();
        Calendar calendar= Calendar.getInstance();
        Date miDate =calendar.getTime();
        key = serial+miDate.getTime();
        return key;
    }

    public boolean existeImagen(String idActividad) {

        boolean respuesta = false;
        Conexion con = new Conexion(context);
        SQLiteDatabase bd = con.getBD();
        try
        {
            String query="select * from archivos as a  where a.idActividades='"+idActividad+"' and a.tipo='Imagen' and a.estado='Activo';";
            Cursor resp = bd.rawQuery(query,null);
            if(resp.moveToFirst())
                respuesta=true;
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        bd.close();
        con.close();

        return respuesta;

    }
}

