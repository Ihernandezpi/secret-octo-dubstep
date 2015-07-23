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
                query.append("insert into "+table+" ( ");
                for (int x = 0;x<nombres.length();x++)
                {
                    if(x!=0) {
                        int a = nombres.length() - x;
                        if (a != 1)
                            query.append(", ");
                    }

                    if(!nombres.getString(x).equals("tabla")) {
                        query.append("\"" + nombres.getString(x) + "\"");

                    }

                }
                query.append(") values (");
                for (int x = 0;x<nombres.length();x++)
                {
                    if(x!=0) {
                        int a = nombres.length() - x;
                        if (a != 1)
                            query.append(", ");
                    }
                    if(!nombres.getString(x).equals("tabla")) {
                        query.append("\"" + object.getString(nombres.getString(x)) + "\"");

                    }

                }
                query.append(");");
//                bd.execSQL(query.toString());
                Log.d("****************",query.toString());
            }

        }catch (Exception e)
        {

        }
        finally {
            con.close();
            bd.close();
        }
    }
}
