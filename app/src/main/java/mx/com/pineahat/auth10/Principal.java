package mx.com.pineahat.auth10;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;


public class Principal extends ActionBarActivity {
    //First We declare Titles and Icons for our Navigation Drawable ListView
    //This Icons and titles are holded in an array as you can see
    String TITLES[]={"1-A Dipositivos Moviles","9-A Desarrollo de Aplicaciones","Configuracion","Ayuda y comentarios","Cerrar Sesion"};
    int ICONS[]={R.mipmap.ic_action_group,R.mipmap.ic_action_group,R.mipmap.ic_action_settings,R.mipmap.ic_action_help,R.mipmap.ic_closesession};

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




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        /*Assing the toolbar object to the view
        and setting the ActionBar to our Tool_bar
         */
        AccountManager miAccountManager = AccountManager.get(getApplicationContext());
        Account [] account = miAccountManager.getAccountsByType("mx.com.pineahat.auth10");
        String myData = miAccountManager.getUserData(account[0], "JSON");
        try
        {
            JSONArray jsonArray = new JSONArray(myData);
            NAME=jsonArray.getJSONObject(0).getString("nombre")+" " +jsonArray.getJSONObject(0).getString("apellidoP");
            EMAIL=jsonArray.getJSONObject(0).getString("apellidoM");

        }catch(Exception e)
        {
        }
        //Attaching the layout to the toolbar object
        toolbar =(Toolbar)findViewById(R.id.tool_bar);
        //Setting toolbar as the ActionBar
        setSupportActionBar(toolbar);

        mRecyclerView=(RecyclerView) findViewById(R.id.RecyclerView);

        mRecyclerView.setHasFixedSize(true);
        mAdapter=new MyAdapter(TITLES,ICONS,NAME,EMAIL,PROFILE,this);

        mRecyclerView.setAdapter(mAdapter);

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
