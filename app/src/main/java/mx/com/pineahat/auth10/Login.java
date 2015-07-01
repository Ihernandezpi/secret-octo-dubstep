package mx.com.pineahat.auth10;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
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
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import mx.com.pineahat.auth10.utilerias.Conexion;


public class Login extends AccountAuthenticatorActivity {
    AccountManager accountManager;
    EditText txtUsuario;
    EditText txtPass;
    Conexion conexion;
    CheckBox miCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        accountManager = AccountManager.get(getApplicationContext());
        Account[] cuentas = accountManager.getAccountsByType("mx.com.pineahat.auth10");
        if(cuentas.length>0)
        {
            Toast.makeText(this,"Ya existe una cuenta",Toast.LENGTH_SHORT).show();
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
                if (miCheckBox.isChecked())
                {
                    String usuario=txtUsuario.getText().toString();
                    String contra=txtUsuario.getText().toString();
                    JSONArray resp = iniciarSesion(usuario, contra);
                    if(resp!=null)
                    {
                        boolean respuesta = crearUsuario(usuario,contra,resp);
                    }
                }
                else
                {
                    String usuario=txtUsuario.getText().toString();
                    String contra=txtUsuario.getText().toString();
                    JSONArray resp = iniciarSesion(usuario, contra);
                    if(resp!=null)
                    {

                    }
                }
            }
        });
    }
    private JSONArray iniciarSesion(String usuario, String contra) {
        JSONArray resp=null;
        ConnectivityManager connMgr = (ConnectivityManager)getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            Toast.makeText(this.getApplicationContext(),"No hay internet",Toast.LENGTH_SHORT).show();
            Task miTask = new Task(usuario,contra);
            try {
                resp = miTask.execute().get();
            }catch (Exception e)
            {
            }

        } else {
            Toast.makeText(this.getApplicationContext(),"No hay internet",Toast.LENGTH_SHORT).show();
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
            accountManager.addAccountExplicitly(account, contra, miBundle);

            //String myData = accountManager.getUserData(account, "id");
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
                HttpPost httpPost = new HttpPost("http://pineahat.com.mx/WS/inicio");
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
}
