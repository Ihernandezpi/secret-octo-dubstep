package mx.com.pineahat.auth10;
import android.*;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;

import mx.com.pineahat.auth10.DAO.DAOCarga;
import mx.com.pineahat.auth10.DAO.DAOSync;
import mx.com.pineahat.auth10.DAO.PrincipalDAO;
import mx.com.pineahat.auth10.utilerias.Conexion;


public class Principal extends ActionBarActivity
{

    Button load_img;
    ImageView img;
    Bitmap bitmap;
    ProgressDialog pDialog;
    de.hdodenhof.circleimageview.CircleImageView mCircleImageView;


    //First We declare Titles and Icons for our Navigation Drawable ListView
    //This Icons and titles are holded in an array as you can see
    // String TITLES[]={"1-A Dipositivos Moviles","9-A Desarrollo de Aplicaciones","Configuracion","Ayuda y comentarios","Cerrar Sesion"};
    String []TITLES;
    String []IDGRUPO;
    int []ICONS;
    // int ICONS[]={R.mipmap.ic_action_group,R.mipmap.ic_action_group,R.mipmap.ic_action_settings,R.mipmap.ic_action_help,R.mipmap.ic_closesession};

    //Similary we Create a String Resource for the name and email in the header view
    //And we also create a int resource for profile pricture in the header view
    String ID;
    String NAME;
    String EMAIL;
    int PROFILE= R.drawable.user;

    //Declaring Toolbar object
    private Toolbar toolbar;

    //Declaring RecyclerView
    RecyclerView mRecyclerView;
    //Declaring Adapter for Recycler View
    RecyclerView.Adapter mAdapter;
    //Declaring Layout Manager as a linear layout manager
    RecyclerView.LayoutManager mlayoutManager;
    //Declaring DrawerLayout
    DrawerLayout Drawer;


    //Declaring Action Bar Drawer Toggle
    ActionBarDrawerToggle mDrawerToggle;

    AccountManager miAccountManager;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);



        /*Assing the toolbar object to the view
        and setting the ActionBar to our Tool_bar
         */
        miAccountManager =  (AccountManager) this.getSystemService(ACCOUNT_SERVICE);
        Account [] account = miAccountManager.getAccountsByType("mx.com.pineahat.auth10");
        String myData = miAccountManager.getUserData(account[0], "JSON");
        /**
         * VErificar cuando no mantenga la sesión
         */
        try
        {
            JSONArray jsonArray = new JSONArray(myData);
            ID=jsonArray.getJSONObject(0).getString("idPersona");
            NAME=jsonArray.getJSONObject(0).getString("nombre")+" " +jsonArray.getJSONObject(0).getString("apellidoP");
            EMAIL=jsonArray.getJSONObject(0).getString("apellidoM");
            PrincipalDAO principalDAO = new PrincipalDAO();
            JSONArray jsonarrayP = null;
            jsonarrayP =principalDAO.grupos(jsonArray.getJSONObject(0).getString("idProfesor"),getApplicationContext());

            if(jsonarrayP!=null)
            {
                final String []TITLES = new String[jsonarrayP.length()+3];
                int []ICONS= new int[jsonarrayP.length()+3];
                final String []IDGRUPO= new String[jsonarrayP.length()+3];

                for(int i=0; i<jsonarrayP.length(); i++)
                {
                    JSONObject miJsonObject = ((JSONObject) jsonarrayP.get(i));
                    TITLES[i]=miJsonObject.get("grado").toString()+"-"+miJsonObject.get("grupo")+" "+ miJsonObject.get("materia");
                    ICONS[i]=R.mipmap.ic_action_group;
                    IDGRUPO[i]=miJsonObject.get("idAsignacion").toString();
                }
                TITLES[jsonarrayP.length()]="Papelera";
                ICONS[jsonarrayP.length()]=R.mipmap.ic_papelera;
                IDGRUPO[jsonarrayP.length()]=" IDCONFIG";

                TITLES[jsonarrayP.length()+1]="Ayuda y Comentarios";
                ICONS[jsonarrayP.length()+1]=R.mipmap.ic_action_help;
                IDGRUPO[jsonarrayP.length()+1]="IDHELP";

                TITLES[jsonarrayP.length()+2]="Salir";
                ICONS[jsonarrayP.length()+2]=R.mipmap.ic_closesession;
                IDGRUPO[jsonarrayP.length()+2]="IDCLOSE";

                toolbar =(Toolbar)findViewById(R.id.tool_bar);
                //Setting toolbar as the ActionBar
                setSupportActionBar(toolbar);


                mRecyclerView=(RecyclerView) findViewById(R.id.RecyclerView);

                mRecyclerView.setHasFixedSize(true);
                mAdapter=new MyAdapter(TITLES,IDGRUPO,ICONS,NAME,EMAIL,PROFILE,this,ID);

                mRecyclerView.setAdapter(mAdapter);
                mCircleImageView=(de.hdodenhof.circleimageview.CircleImageView)findViewById(R.id.cicleView);


                //Inflando Fragmen por default

                Fragment fragment = new MyFragment();
                Bundle args = new Bundle();
                String tipo="Actividades";
                args.putString(MyFragment.ID_GRUPO, IDGRUPO[0]);
                args.putString(MyFragment.TYPE, tipo);
                fragment.setArguments(args);

                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame, fragment).commit();
                getSupportActionBar().setTitle(TITLES[0]);

                //////////
                final GestureDetector mGestureDetector = new GestureDetector(Principal.this, new GestureDetector.SimpleOnGestureListener() {

                    @Override public boolean onSingleTapUp(MotionEvent e) {
                        return true;
                    }

                });



                mRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
                    @Override
                    public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
                        View child = recyclerView.findChildViewUnder(motionEvent.getX(),motionEvent.getY());

                        Intent intent = null;

                        if(child!=null && mGestureDetector.onTouchEvent(motionEvent)){

                            if (recyclerView.getChildPosition(child)!=0) {
                                Drawer.closeDrawers();
                                //Toast.makeText(Principal.this, "The Item Clicked is: " + recyclerView.getChildPosition(child), Toast.LENGTH_SHORT).show();
                                if (recyclerView.getChildPosition(child)<recyclerView.getChildCount()-3)
                                {
                                    Fragment fragment = new MyFragment();
                                    Bundle args = new Bundle();
                                    args.putString(MyFragment.ID_GRUPO, IDGRUPO[recyclerView.getChildPosition(child)-1]);
                                    String tipo="Actividades";
                                    args.putString(MyFragment.TYPE, tipo);
                                    fragment.setArguments(args);

                                    FragmentManager fragmentManager = getSupportFragmentManager();
                                    fragmentManager.beginTransaction()
                                            .replace(R.id.content_frame, fragment).commit();
                                    getSupportActionBar().setTitle(TITLES[recyclerView.getChildPosition(child) - 1]);
                                }
                                if(recyclerView.getChildPosition(child)==recyclerView.getChildCount()-3)
                                {
                                    String id =IDGRUPO[recyclerView.getChildPosition(child) - 1];
                                    String tipo="Papelera";
                                    Fragment fragment = new MyFragment();
                                    Bundle args = new Bundle();
                                    args.putString(MyFragment.ID_GRUPO, IDGRUPO[recyclerView.getChildPosition(child)-1]);
                                    args.putString(MyFragment.TYPE, tipo);
                                    fragment.setArguments(args);
                                    FragmentManager fragmentManager = getSupportFragmentManager();
                                    fragmentManager.beginTransaction()
                                            .replace(R.id.content_frame, fragment).commit();
                                    getSupportActionBar().setTitle(TITLES[recyclerView.getChildPosition(child) - 1]);

                                }
                                if(recyclerView.getChildPosition(child)==recyclerView.getChildCount()-2)
                                {
                                    Toast.makeText(Principal.this, "Esto es Ayuda y Comentarios con ID" + IDGRUPO[recyclerView.getChildPosition(child)-1], Toast.LENGTH_SHORT).show();
                                }
                                if(recyclerView.getChildPosition(child)==recyclerView.getChildCount()-1)
                                {
                                    Account [] arAccounts = miAccountManager.getAccountsByType("mx.com.pineahat.auth10");
                                    if(arAccounts.length>=1) {
                                        miAccountManager.removeAccount(arAccounts[0],null,null);
                                    }
                                    //Limpiar base de datos
                                    Conexion miConexion = new Conexion(getApplicationContext());
                                    miConexion.limpiarBD();
                                    Intent intent2 = new Intent(getApplicationContext(), Login.class);
                                    startActivity(intent2);
                                    finish();
                                }

                            }
                            else
                            {


                            }
                            return true;

                        }


                        return false;
                    }

                    @Override
                    public void onTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {

                    }
                });

                mlayoutManager=new LinearLayoutManager(this);
                mRecyclerView.setLayoutManager(mlayoutManager);

                Drawer = (DrawerLayout) findViewById(R.id.DrawerLayout);        // Drawer object Assigned to the view
                mDrawerToggle = new ActionBarDrawerToggle(this,Drawer,toolbar,R.string.open,R.string.close){

                    @Override
                    public void onDrawerOpened(View drawerView) {
                        super.onDrawerOpened(drawerView);
                        // code here will execute once the drawer is opened( As I dont want anything happened whe drawer is
                        // open I am not going to put anything here)
                        //Toast.makeText(getApplicationContext(),"Abierto",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onDrawerClosed(View drawerView) {
                        super.onDrawerClosed(drawerView);
                        //Toast.makeText(getApplicationContext(),"cerrado",Toast.LENGTH_SHORT).show();
                    }



                }; // Drawer Toggle Object Made

                Drawer.setDrawerListener(mDrawerToggle); // Drawer Listener set to the Drawer toggle
                mDrawerToggle.syncState();               // Finally we set the drawer toggle sync State

            }

        }catch(Exception e)
        {

        }
        //Attaching the layout to the toolbar object


    }

    @Override
    protected void onDestroy() {
        if(!Login.keepSession)
        {
            Account [] arAccounts = miAccountManager.getAccountsByType("mx.com.pineahat.auth10");
            if(arAccounts.length>=1) {
                miAccountManager.removeAccount(arAccounts[0],null,null);
            }
        }
        super.onDestroy();

    }
}
