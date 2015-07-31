package mx.com.pineahat.auth10.DAO;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import mx.com.pineahat.auth10.utilerias.Conexion;

/**
 * Created by ${Ignacio} on 20/07/2015.
 */
public class DAOCarga {
    private Context context;
    public DAOCarga(Context context)
    {
        this.context=context;
    }
    public void trunck(JSONArray array)
    {
        Conexion con = new Conexion(context);
        SQLiteDatabase bd = con.getBD();
        try
        {
            for (int i=0;i<array.length();i++)
            {
                JSONObject object = array.getJSONObject(i);
                String table = object.getString("tabla");
                JSONArray nombres = object.names();
                StringBuffer query = new StringBuffer();
                int contador =0;
                query.append("insert or replace into " + table + " ( ");
                String [] nom = new String [nombres.length()-1];
                for (int x = 0;x<nombres.length();x++ )
                {

                    if(!nombres.getString(x).equals("tabla")) {
                        nom[contador] = nombres.getString(x);
                        contador++;
                    }
                }
                for (int x = 0;x<nom.length;x++)
                {
                    if(x!=0)
                    {
                        query.append(", ");
                    }
                    query.append("\"" + nom[x] + "\"");
                }
                query.append(") values (");

                for (int x = 0;x<nom.length;x++)
                {
                    if(x!=0)
                    {
                        query.append(", ");
                    }
                    query.append("\"" + object.getString(nom[x]) + "\"");

                }
                query.append(");");
                bd.execSQL(query.toString());
                Log.d("****************", query.toString());
            }

        }catch (Exception e)
        {
            e.printStackTrace();
        }
        finally {
            con.close();
            bd.close();
        }
    }
}
