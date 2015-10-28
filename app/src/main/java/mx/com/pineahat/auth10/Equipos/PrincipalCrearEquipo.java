package mx.com.pineahat.auth10.Equipos;

import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

import mx.com.pineahat.auth10.Actividades.Actividades;
import mx.com.pineahat.auth10.DAO.DAOEquipos;
import mx.com.pineahat.auth10.MyAdapter;
import mx.com.pineahat.auth10.R;

public class PrincipalCrearEquipo extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mlayoutManager;
    private ArrayList<Integrantes> misIntegrantes;
    private String idActividad;
    private String idEquipo=null;
    private Toolbar toolbar;
    Integer color = Color.parseColor("#3F51B5");
    private IntegrantesEquipoAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal_crear_equipo);
        toolbar =(Toolbar) findViewById(R.id.tool_bar);
        toolbar.setBackgroundColor(color);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerIntegrantes);
        mRecyclerView.setHasFixedSize(true);
        mlayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mlayoutManager);
        String tipo = getIntent().getStringExtra("tipo");
        DAOEquipos miDaoEquipos = new DAOEquipos(this);
        misIntegrantes = new ArrayList<Integrantes>();
        this.idActividad = getIntent().getStringExtra("idActividad");

        if(tipo.equals("nuevo")) {

            this.idActividad = getIntent().getStringExtra("idActividad");
            JSONArray miJsonArray = miDaoEquipos.traerAlumnos(idActividad);
            if(miJsonArray!=null) {
                mAdapter = new IntegrantesEquipoAdapter(miJsonArray,misIntegrantes);
                mRecyclerView.setAdapter(mAdapter);
            }
            else
            {
                //No hay Alumnos
                Toast.makeText(this,"No hay alumnos",Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            this.idEquipo = getIntent().getStringExtra("idEquipo");
            JSONArray miJsonArray = miDaoEquipos.getIntegrantesEquipo(idEquipo);
            String nombreEquipo= miDaoEquipos.getNombre(idEquipo);
            if(miJsonArray!=null) {
                mAdapter = new IntegrantesEquipoAdapter(miJsonArray,misIntegrantes,nombreEquipo);
                mRecyclerView.setAdapter(mAdapter);
            }
            else
            {
                this.idActividad = getIntent().getStringExtra("idActividad");
                miJsonArray = miDaoEquipos.traerAlumnos(idActividad);
                if(miJsonArray!=null) {
                    mAdapter = new IntegrantesEquipoAdapter(miJsonArray,misIntegrantes,nombreEquipo);
                    mRecyclerView.setAdapter(mAdapter);
                }
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_principal_crear_equipo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        mRecyclerView.setLayoutManager(mlayoutManager);
        if(id==android.R.id.home)
        {
            procesar();
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private String crearEquipo(String idActividad,String nombre)
    {
        DAOEquipos miDaoEquipos = new DAOEquipos(this);
        String idEquipo = miDaoEquipos.generarEquipo(idActividad,nombre);
        return idEquipo;
    }
    public static Actividades actividades;
    @Override
    protected void onDestroy() {
        procesar();
        actividades.avisar();
        super.onDestroy();

    }
    private void procesar() {


        if (mAdapter.getFlag()) {
            try {
                String nombre = mAdapter.getNombre();
                if (!nombre.equals("") || misIntegrantes.size() != 0) {
                    if (this.idEquipo == null) {
                        this.idEquipo = crearEquipo(this.idActividad, nombre);
                    }
                    DAOEquipos daoEquipos = new DAOEquipos(this);
                    daoEquipos.actualizarIntegrantes(misIntegrantes, this.idEquipo, nombre);
                    }
            } catch (Exception e) {

            }
        }

    }




}
