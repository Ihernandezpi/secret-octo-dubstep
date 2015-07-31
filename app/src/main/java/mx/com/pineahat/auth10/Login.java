package mx.com.pineahat.auth10;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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
import mx.com.pineahat.auth10.utilerias.Conexion;


public class Login extends AppCompatActivity {
    public static boolean keepSession=true;
    AccountManager accountManager;
    EditText txtUsuario;
    EditText txtPass;
    Conexion conexion;
    CheckBox miCheckBox;
    View coordinatorLayoutView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        accountManager = AccountManager.get(getApplicationContext());
        coordinatorLayoutView = findViewById(R.id.cordinatorLogin);
        Account[] cuentas = accountManager.getAccountsByType("mx.com.pineahat.auth10");
        if(cuentas.length>0)
        {
            Intent intent = new Intent(this, Principal.class);
            startActivity(intent);
            finish();
        }
        conexion= new Conexion(getApplicationContext());
        SQLiteDatabase bd = conexion.getBD();
        bd.close();
        txtUsuario = (EditText)findViewById(R.id.ediTextUser);
        txtPass = (EditText)findViewById(R.id.editTextPass);
        miCheckBox=(CheckBox)findViewById(R.id.checkBoxNoCerrar);
        Button miButton =(Button)findViewById(R.id.buttonIniciar);
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
                        JSONArray resp = iniciarSesion(usuario, contra);
                        if (resp!=null) {
                            try {
                                JSONObject jsonObject = resp.getJSONObject(0);
                                String idProfe = jsonObject.getString("idProfesor");
                                TaskTrunck miTaskTrunck = new TaskTrunck(idProfe);
                                JSONArray a = miTaskTrunck.execute().get();
                            }
                            catch (Exception e)
                            {

                            }
                            boolean respuesta = crearUsuario(usuario, contra, resp);
                            Intent intent = new Intent(v.getContext(), Principal.class);
                            startActivity(intent);
                            finish();

                        }
                        else
                        {
                            Snackbar.make(coordinatorLayoutView,"Error en el usuario o contraseña",Snackbar.LENGTH_SHORT).show();
                        }
                    } else {
                        keepSession = false;
                        JSONArray resp = iniciarSesion(usuario, contra);
                        if (resp!=null) {
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
                            Intent intent = new Intent(v.getContext(), Principal.class);
                            startActivity(intent);
                            finish();
                        }
                        else
                        {
                            Snackbar.make(coordinatorLayoutView,"Error en el usuario o contraseña",Snackbar.LENGTH_SHORT).show();
                        }

                    }
                }
                else
                {
                    Snackbar.make(coordinatorLayoutView,"Campos vacios",Snackbar.LENGTH_SHORT).show();
                }

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
                resp = miTask.execute().get();
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
    class Task extends AsyncTask<String, String,JSONArray> {
        String usuario;
        String contra;
        public Task(String usuario, String contra)
        {
            this.usuario=usuario;
            this.contra=contra;
        }
        protected JSONArray doInBackground(String... urls) {
            JSONArray resp=null;
            try {
                HttpClient Client = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://pineahat.com.mx/WSA/TI9/inicio");
                //HttpPost httpPost = new HttpPost("http://192.168.0.4:8080/WSA/TI9/inicio");
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("usuario", this.usuario));
                nameValuePairs.add(new BasicNameValuePair("contra", this.contra));
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
                HttpClient Client = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://pineahat.com.mx/WSA/TI9/actividades");
                //HttpPost httpPost = new HttpPost("http://192.168.0.4:8080/WSA/TI9/actividades");
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("jsonIn", miArray.toString()));
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse SetServerString =Client.execute(httpPost);
                resp= new JSONArray(EntityUtils.toString(SetServerString.getEntity()));
                try {
                    DAOCarga carga= new DAOCarga(getBaseContext());
                    carga.trunck(resp);
                }catch (Exception e)
                {

                }
            } catch (Exception e) {
            }
            return resp;
        }
        protected void onPostExecute(String feed) {

        }
    }
}
