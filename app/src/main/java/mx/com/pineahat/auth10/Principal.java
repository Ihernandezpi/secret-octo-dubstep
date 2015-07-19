package mx.com.pineahat.auth10;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import mx.com.pineahat.auth10.DAO.PrincipalDAO;



public class Principal extends ActionBarActivity
        {
    //First We declare Titles and Icons for our Navigation Drawable ListView
    //This Icons and titles are holded in an array as you can see
   // String TITLES[]={"1-A Dipositivos Moviles","9-A Desarrollo de Aplicaciones","Configuracion","Ayuda y comentarios","Cerrar Sesion"};
    String []TITLES;
    String []IDGRUPO;
    int []ICONS;
   // int ICONS[]={R.mipmap.ic_action_group,R.mipmap.ic_action_group,R.mipmap.ic_action_settings,R.mipmap.ic_action_help,R.mipmap.ic_closesession};

    //Similary we Create a String Resource for the name and email in the header view
    //And we also create a int resource for profile pricture in the header view

    String NAME;
    String EMAIL;
    int PROFILE=R.drawable.fany;

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
                TITLES[jsonarrayP.length()]="Configuracion";
                ICONS[jsonarrayP.length()]=R.mipmap.ic_action_settings;
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
                mAdapter=new MyAdapter(TITLES,IDGRUPO,ICONS,NAME,EMAIL,PROFILE,this);

                mRecyclerView.setAdapter(mAdapter);

                //Inflando Fragmen por default

                Fragment fragment = new MyFragment();
                Bundle args = new Bundle();
                args.putString(MyFragment.ID_GRUPO, IDGRUPO[0]);
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
                                    Toast.makeText(Principal.this, "The Item Clicked is a group and his ID: "+ IDGRUPO[recyclerView.getChildPosition(child)-1] , Toast.LENGTH_SHORT).show();
                                   // Toast.makeText(Principal.this, "SON " + recyclerView.getChildCount(), Toast.LENGTH_SHORT).show();
                                    Fragment fragment = new MyFragment();
                                    Bundle args = new Bundle();
                                    args.putString(MyFragment.ID_GRUPO, " Esto es un Fragment El ID del grupo es "+IDGRUPO[recyclerView.getChildPosition(child)-1]);
                                    fragment.setArguments(args);

                                    FragmentManager fragmentManager = getSupportFragmentManager();
                                    fragmentManager.beginTransaction()
                                            .replace(R.id.content_frame, fragment).commit();
                                    getSupportActionBar().setTitle(TITLES[recyclerView.getChildPosition(child) - 1]);
                                }
                                if(recyclerView.getChildPosition(child)==recyclerView.getChildCount()-3)
                                {
                                    Toast.makeText(Principal.this, "Esto es configuracion con ID" + IDGRUPO[recyclerView.getChildPosition(child) - 1], Toast.LENGTH_SHORT).show();

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
                        Toast.makeText(getApplicationContext(),"Abierto",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onDrawerClosed(View drawerView) {
                        super.onDrawerClosed(drawerView);
                        Toast.makeText(getApplicationContext(),"cerrado",Toast.LENGTH_SHORT).show();
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

}
