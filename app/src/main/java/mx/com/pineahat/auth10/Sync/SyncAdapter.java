package mx.com.pineahat.auth10.Sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import mx.com.pineahat.auth10.DAO.DAOCarga;
import mx.com.pineahat.auth10.DAO.DAOSync;
import mx.com.pineahat.auth10.R;
import mx.com.pineahat.auth10.URL.HttpConnectDownload;

/**
 * Created by ${Ignacio} on 12/07/2015.
 */
public class SyncAdapter extends AbstractThreadedSyncAdapter {
    ContentResolver mContentResolver;
    Context context;
    HttpConnectDownload connectDownload = new HttpConnectDownload();
    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        mContentResolver = context.getContentResolver();
        this.context=context;
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        try {
            Log.d("*********************", "Sync");
            AccountManager miManager = AccountManager.get(context);
            Account a[] = miManager.getAccountsByType("mx.com.pineahat.auth10");
            String myData = miManager.getUserData(account, "JSON");
            String fecha = miManager.getUserData(account, "fechaSync");
            String dateS = "";
            dateS = miManager.getUserData(account, "fechaSyncS");

            //Aqui va tu log�ca de negocios
            DAOSync sync = new DAOSync(context);
            JSONArray jsonArray = sync.getActividades(fecha,dateS);
            Log.d("*********************", jsonArray.toString());
            Task miTask = new Task(jsonArray);
            JSONArray s = miTask.execute().get();

            //Asignar ultima fecha de actualizacion fechaSync
            if(s!=null) {
                for (int i =0;i<s.length();i++)
                {
                    switch (s.getJSONObject(i).getString("tipoAccion")) {
                        case "fechaActualizacion":
                            Calendar calendar = Calendar.getInstance();
                            Date rightNow = calendar.getTime();
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            String strDate = sdf.format(rightNow.getTime());
                            miManager.setUserData(a[0], "fechaSync", strDate);
                            miManager.setUserData(a[0], "fechaSyncS", s.getJSONObject(i).getString("fecha"));
                        break;
                        case "ultima_fecha":
                            Log.d("---------------------", "Insertando Actualizacion");
                            DAOCarga miCarga = new DAOCarga(context);
                            JSONObject objeto = s.getJSONObject(i);
                            objeto.remove("tipoAccion");
                            JSONArray array = new JSONArray();
                            array.put(objeto);
                            miCarga.trunck(array);
                            break;
                    }
                }

            }
        }catch (Exception e)
        {
            Log.d("*********************", "Error Sync");
            e.printStackTrace();
        }

    }
    class Task extends AsyncTask<String, String, JSONArray> {
        JSONArray miJsonArray;
        public Task(JSONArray miJsonArray)
        {
            this.miJsonArray=miJsonArray;
        }

        protected JSONArray doInBackground(String... urls) {
            JSONArray resp = null;
            try {

                List<NameValuePair> entityParams = new ArrayList<NameValuePair>();
                entityParams.add(new BasicNameValuePair("json_in", miJsonArray.toString()));
                String res = connectDownload.webServiceConnection(SyncAdapter.super.getContext().getResources().getString(R.string.syncURL), entityParams);
                JSONArray regreso= new JSONArray(res);
                Log.d("++++++++++++++++",regreso.toString());
                resp=regreso;
            } catch (Exception e) {
            e.printStackTrace();
            }

            return resp;
        }
        protected void onPostExecute(String feed) {

        }
    }
}
