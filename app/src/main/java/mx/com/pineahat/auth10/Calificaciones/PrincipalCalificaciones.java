package mx.com.pineahat.auth10.Calificaciones;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import org.json.JSONArray;

import mx.com.pineahat.auth10.Equipos.IntegrantesEquipoAdapter;
import mx.com.pineahat.auth10.R;

public class PrincipalCalificaciones extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mlayoutManager;
    private IntegrantesEquipoAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal_calificaciones);
        mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerIntegrantes);
        mRecyclerView.setHasFixedSize(true);
        mlayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mlayoutManager);
        JSONArray miJsonArray = new JSONArray();
        miJsonArray.put("");
        miJsonArray.put("");
        miJsonArray.put("");
        miJsonArray.put("");
        CalificacionesAdapter miCalificacionesAdapter = new CalificacionesAdapter(miJsonArray);
        mRecyclerView.setAdapter(miCalificacionesAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_principal_calificaciones, menu);
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
