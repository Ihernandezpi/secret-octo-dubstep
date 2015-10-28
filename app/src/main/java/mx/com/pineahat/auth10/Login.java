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
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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
    Button miButton;
    ViewGroup miVertical;
    TextView txtOlvideContra;
    TextView espacio1;
    TextView espacio2;
    TextView espacio3;
    TextView espacio4;
    TextView espacio5;
    TextView espacio6;
    TextView espacio7;
    TextView txtCargando;
    private int t1,t2,t3,t4,t5,t6,t7,u,c,ch,b,ol;

    HttpConnectDownload connectDownload = new HttpConnectDownload();
    int tamanoLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        espacio1=(TextView)findViewById(R.id.espacio1);
        t1=espacio1.getHeight();
        espacio2=(TextView)findViewById(R.id.espacio2);
        t2=espacio2.getHeight();
        espacio3=(TextView)findViewById(R.id.espacio3);
        t3=espacio3.getHeight();
        espacio4=(TextView)findViewById(R.id.espacio4);
        t4=espacio4.getHeight();
        espacio5=(TextView)findViewById(R.id.espacio5);
        t5=espacio5.getHeight();
        espacio6=(TextView)findViewById(R.id.espacio6);
        t6=espacio6.getHeight();
        espacio7=(TextView)findViewById(R.id.espacio7);
        t7=espacio7.getHeight();
        txtCargando=(TextView)findViewById(R.id.txtCargando);

        accountManager = AccountManager.get(getApplicationContext());
        coordinatorLayoutView = findViewById(R.id.cordinatorLogin);
        miProgressBar = (ProgressBar) findViewById(R.id.progressBar);
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
        miButton =(Button)findViewById(R.id.buttonIniciar);
        miVertical = (ViewGroup) findViewById(R.id.verticalLayoutLogin);
        tamanoLayout=miVertical.getHeight();
        txtOlvideContra = (TextView)findViewById(R.id.textViewOlvide);
        u=txtUsuario.getHeight();
        u=txtPass.getHeight();
        ch=miCheckBox.getHeight();
        b=miButton.getHeight();
        ol=txtOlvideContra.getHeight();

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
            final Task miTask = new Task(usuario,contra);
            try {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        miTask.execute();
                    }
                });

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
                } else {
                    Snackbar.make(coordinatorLayoutView, "Error en el usuario o contraseña", Snackbar.LENGTH_LONG).show();
                    synchronized (this) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                detenerProgress();
                            }
                        });
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
                }
            return null;
        }

        @Override
        protected void onPostExecute (JSONArray array) {

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
            Intent intent = new Intent(getApplicationContext(), Principal.class);
            startActivity(intent);
            finish();
            super.onPostExecute(array);
        }
    }
    public void correrProgress() {
        collapse(txtUsuario);
        collapse(txtPass);
        collapse(miCheckBox);
        collapse(miButton);
        collapse(miButton);
        collapse(txtOlvideContra);
        collapse(espacio1);
        collapse(espacio2);
        collapse(espacio3);
        collapse(espacio4);
        collapse(espacio5);
        miProgressBar.setVisibility(View.VISIBLE);
        txtCargando.setVisibility(View.VISIBLE);
        espacio6.setVisibility(View.VISIBLE);
        espacio7.setVisibility(View.VISIBLE);
    }
    public void detenerProgress()
    {
        miProgressBar.setVisibility(View.GONE);
        txtCargando.setVisibility(View.GONE);
        espacio6.setVisibility(View.GONE);
        espacio7.setVisibility(View.GONE);
        expand(txtUsuario, u);
        expand(txtPass,c);
        expand(miCheckBox, ch);
        expand(miButton, b);
        expand(espacio1, t1);
        expand(espacio2,t2);
        expand(espacio3,t3);
        expand(espacio4,t4);
        expand(espacio5, t5);

    }

    public static void expand(final View v, final int height) {
        v.measure(height, RelativeLayout.LayoutParams.MATCH_PARENT);
        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1 ? height : (int)(targetHeight * interpolatedTime);
                v.requestLayout();
            }
            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };
        // 1dp/ms
        a.setDuration((int) (targetHeight / v.getContext().getResources().getDisplayMetrics().density));

        v.startAnimation(a);

    }

    public static void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();
        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if(interpolatedTime == 1)
                    v.setVisibility(View.GONE);
                else{
                    v.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }
            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };
        // 1dp/ms
        //a.setDuration((int)(initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        a.setDuration(10);
        v.startAnimation(a);
    }




}
