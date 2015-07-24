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

/**
 * Created by ${Ignacio} on 12/07/2015.
 */
public class SyncAdapter extends AbstractThreadedSyncAdapter {
    ContentResolver mContentResolver;
    Context context;
    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        mContentResolver = context.getContentResolver();
        this.context=context;
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {

      try
      {
        ConnectivityManager connMgr = (ConnectivityManager)context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            Log.d("*********************", "Hay conexion");
            AccountManager miManager = AccountManager.get(context);
            Account a[] = miManager.getAccountsByType("mx.com.pineahat.auth10");
            String myData = miManager.getUserData(account, "JSON");
            String fecha = miManager.getUserData(account, "fechaSync");

            //Aqui va tu logíca de negocios
            DAOSync sync = new DAOSync(context);
            JSONArray jsonArray = sync.getActividades(fecha);
            Log.d("*********************", jsonArray.toString());

            Task miTask = new Task(jsonArray);
            JSONArray resp = miTask.execute().get();

            //Asignar ultima fecha de actualizacion fechaSync
            Calendar calendar= Calendar.getInstance();
            Date rightNow = calendar.getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String strDate = sdf.format(rightNow.getTime());
            miManager.setUserData(a[0],"fechaSync",strDate);
        }}
      catch (Exception e)
      {
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
                HttpClient Client = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://pineahat.com.mx/WSA/TI9/actividades");
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("jsonIn", miJsonArray.toString()));
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse SetServerString =Client.execute(httpPost);
                resp= new JSONArray(EntityUtils.toString(SetServerString.getEntity()));
            } catch (Exception e) {
            }
            return resp;
        }
        protected void onPostExecute(String feed) {

        }
    }
}
