package mx.com.pineahat.auth10;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import mx.com.pineahat.auth10.DAO.DAOCarga;
import mx.com.pineahat.auth10.URL.HttpConnectDownload;
import mx.com.pineahat.auth10.utilerias.Conexion;


public class Login extends AppCompatActivity {
    public static boolean keepSession=true;
    AccountManager accountManager;
    EditText txtUsuario;
    EditText txtPass;
    Conexion conexion;
    CheckBox miCheckBox;
    View coordinatorLayoutView;
    ProgressBar miProgressBar;
    HttpConnectDownload connectDownload = new HttpConnectDownload();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        accountManager = AccountManager.get(getApplicationContext());
        coordinatorLayoutView = findViewById(R.id.cordinatorLogin);
        miProgressBar = (ProgressBar) findViewById(R.id.progress);
        Account[] cuentas = accountManager.getAccountsByType("mx.com.pineahat.auth10");
        if(cuentas.length>0)
        {
            Intent intent = new Intent(this, Principal.class);
            startActivity(intent);
            finish();
        }
        conexion= new Conexion(getApplicationContext());
        final SQLiteDatabase bd = conexion.getBD();
        bd.close();
        txtUsuario = (EditText)findViewById(R.id.ediTextUser);
        txtPass = (EditText)findViewById(R.id.editTextPass);
        miCheckBox=(CheckBox)findViewById(R.id.checkBoxNoCerrar);
        final Button miButton =(Button)findViewById(R.id.buttonIniciar);
        final ViewGroup miVertical = (ViewGroup) findViewById(R.id.verticalLayoutLogin);
        TextView txtOlvideContra = (TextView)findViewById(R.id.textViewOlvide);
        txtOlvideContra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(),"Comming Soon",Toast.LENGTH_SHORT).show();
            }
        });
        miButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 String usuario=txtUsuario.getText().toString();
                String contra=txtPass.getText().toString();
                if(!usuario.equals("") && !contra.equals("")) {
                    if (miCheckBox.isChecked()) {
                        keepSession = true;
                        iniciarSesion(usuario, contra);
                    } else
                    {
                        keepSession = false;
                        iniciarSesion(usuario, contra);
                    }
                }
                else
                {
                    Snackbar.make(coordinatorLayoutView,"Campos vacios",Snackbar.LENGTH_SHORT).show();
                }
                //miProgressBar.setVisibility(View.INVISIBLE);
            }
        });
    }
    private JSONArray iniciarSesion(String usuario, String contra) {
        JSONArray resp=null;
        ConnectivityManager connMgr = (ConnectivityManager)getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            Task miTask = new Task(usuario,contra);
            try {
                miTask.execute();
            }catch (Exception e)
            {
            }

        } else {
            Snackbar.make(coordinatorLayoutView,"No hay internet",Snackbar.LENGTH_SHORT).show();
        }

        return resp;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private boolean crearUsuario(String usuario,String contra,JSONArray array)
    {
        boolean flag=false;
        try {
            Account account = new Account(usuario, "mx.com.pineahat.auth10");
            Bundle miBundle = new Bundle();
            miBundle.putString("JSON", array.toString());

            Calendar calendar= Calendar.getInstance();
            Date rightNow = calendar.getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String strDate = sdf.format(rightNow.getTime());
            miBundle.putString("fechaSync",strDate);
            miBundle.putString("fechaSyncS", strDate);

            accountManager.addAccountExplicitly(account, contra, miBundle);

            //String myData = accountManager.getUserData(account, "JSON");
            //Toast.makeText(v.getContext(),myData,Toast.LENGTH_SHORT).show();
            flag=true;
        }catch (Exception e)
        {
        }
        return flag;
    }
    class Task extends AsyncTask<String, String,JSONArray>{
        String usuario;
        String contra;
        public Task(String usuario, String contra)
        {
            this.usuario=usuario;
            this.contra=contra;
        }

        @Override
        protected void onPreExecute() {
            correrProgress();
            super.onPreExecute();
        }

        protected JSONArray doInBackground(String... urls) {
            JSONArray resp=null;
            try {

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("usuario", this.usuario));
                nameValuePairs.add(new BasicNameValuePair("contra", this.contra));
                String res = connectDownload.webServiceConnection(getResources().getString(R.string.inicioURL),nameValuePairs);
                resp= new JSONArray(res);
                if (resp.length()!=0) {
                try {
                    JSONObject jsonObject = resp.getJSONObject(0);
                    String idProfe = jsonObject.getString("idProfesor");
                    TaskTrunck miTaskTrunck = new TaskTrunck(idProfe);
                    miTaskTrunck.execute();
                }
                catch (Exception e)
                {

                }
                boolean respuesta = crearUsuario(usuario, contra, resp);
                }
                else
                {
                    Snackbar.make(coordinatorLayoutView,"Error en el usuario o contraseña",Snackbar.LENGTH_SHORT).show();
                }
                } catch (Exception e) {
                e.printStackTrace();
                }
            return null;
        }

        @Override
        protected void onPostExecute(JSONArray array) {
            detenerProgress();
            super.onPostExecute(array);
        }
    }
    class TaskTrunck extends AsyncTask<String, String, JSONArray> {
        String idProfe;
        public TaskTrunck(String idProfe)
        {
            this.idProfe=idProfe;
        }
        protected JSONArray doInBackground(String... urls) {
            JSONArray resp=null;
            JSONObject jsonIn = new JSONObject();
            try {
                jsonIn.put("tipoAccion","inicio");
                jsonIn.put("idProfesor",this.idProfe);
                JSONArray miArray = new JSONArray();
                miArray.put(jsonIn);
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("json_in", miArray.toString()));
                String res = connectDownload.webServiceConnection(getResources().getString(R.string.syncURL),nameValuePairs);
                resp= new JSONArray(res);

                try {
                    DAOCarga carga= new DAOCarga(getBaseContext());
                    carga.trunck(resp);
                }catch (Exception e)
                {

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return resp;
        }

        @Override
        protected void onPostExecute(JSONArray array) {
            detenerProgress();
            Intent intent = new Intent(getApplicationContext(), Principal.class);
            startActivity(intent);
            finish();
            super.onPostExecute(array);
        }
    }
    public void correrProgress()
    {
        miProgressBar.setVisibility(View.VISIBLE);
    }
    public void detenerProgress()
    {
        miProgressBar.setVisibility(View.GONE);
    }

}
