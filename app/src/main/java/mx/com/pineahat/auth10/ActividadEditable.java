package mx.com.pineahat.auth10;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


public class ActividadEditable extends ActionBarActivity {

    private Toolbar toolbar;
    private TextView tRecordar;
    private TextView tRecordarClose;
    Integer color = Color.parseColor("#bdbdbd");
    private LayoutInflater inflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad_editable);

        toolbar =(Toolbar) findViewById(R.id.tool_bar);
        tRecordar= (TextView) findViewById(R.id.txtRecordarme);
        toolbar.setBackgroundColor(color);
        setSupportActionBar(toolbar);
        getSupportActionBar().setIcon(R.mipmap.ic_action_return);
        getSupportActionBar().setTitle("");
        inflater = LayoutInflater.from(this);


        final View Recordar= tRecordar;
        final ViewGroup parent= (ViewGroup) Recordar.getParent();
        final int index=parent.indexOfChild(Recordar);
        final View vista = inflater.inflate(R.layout.editable_fecha,null);


        ImageView close = (ImageView) vista.findViewById(R.id.editablefechaclose);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), " close", Toast.LENGTH_SHORT).show();
                parent.removeView(vista);
                parent.addView(Recordar, index);

            }
        });

        TextView fecha= (TextView)vista.findViewById(R.id.textFecha);
        fecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext()," Fecha", Toast.LENGTH_SHORT).show();
            }
        });

        tRecordar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            parent.removeView(Recordar);
                parent.addView(vista,index);
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_actividad_editable, menu);
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
        if(id==R.id.action_add_team)
        {

                    Toast.makeText(getApplicationContext()," Add TEAM", Toast.LENGTH_SHORT).show();

        }
        if(id==R.id.action_palette_color)
        {
            final CharSequence[] items = {"Red", "Green", "Blue"};
            Toast.makeText(getApplicationContext(), "Palette Color", Toast.LENGTH_SHORT).show();
            new AlertDialog.Builder(this)
                    .setTitle("Color de Actividad")
                    .setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(getApplicationContext(), items[which], Toast.LENGTH_SHORT).show();
                        }
                    })
                    .show();


        }
        return super.onOptionsItemSelected(item);
    }
}
