package mx.com.pineahat.auth10.Actividades;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import mx.com.pineahat.auth10.Calificaciones.PrincipalCalificaciones;
import mx.com.pineahat.auth10.ColorPickerDialog;
import mx.com.pineahat.auth10.ColorPickerSwatch.OnColorSelectedListener;
import mx.com.pineahat.auth10.DAO.DAOActividades;
import mx.com.pineahat.auth10.DAO.DAOEquipos;
import mx.com.pineahat.auth10.Equipos.PrincipalCrearEquipo;


import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import mx.com.pineahat.auth10.R;
//  "Al Cesar lo que es del Cesar"
public class Actividades extends ActionBarActivity implements TimePickerDialog.OnTimeSetListener,DatePickerDialog.OnDateSetListener {

    //DAO
    private DAOActividades daoActividades;
    private DAOEquipos daoEquipos;
    private Actividad actividad= new Actividad();//Clase Actividad
    private LinearLayout linearLayoutP;//lenar que  aloja todo
    private Toolbar toolbar; //Toolbar
    private ScrollView scrollView;// Scroll que aloja el layout
    private LinearLayout  linearLayout; //Layout de la Actividad
    private TextView etxtTitulo; //EditText titulo de la actividad
    private TextView etxtDescripcion; ////EditText descripcion de la actividad
    private TextView txtRecordarme; // textView de Recordarme
    private LayoutInflater inflater; // Variable para inflar  row fecha y hora
    private TextView textFecha;
    private TextView textHora;
    //Dialogs de Fecha y Hora
    private  TimePickerDialog timePickerDialog;// TimePicker para Hora
    private DatePickerDialog datePickerDialog; //TimePicker para fecha

    private boolean flagClosed=false;

    final ColorPickerDialog colorPickerDialog = new ColorPickerDialog(); //Color
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividades);
        //DAO
        daoActividades= new DAOActividades(getApplicationContext());
        daoEquipos = new DAOEquipos(getApplicationContext());
        Intent datosActividad= getIntent(); // Valriable Intento que recupera el ID de la asignacion y otros datos  del grupo
        actividad.setActividadInformacion(datosActividad.getStringExtra("info"));// asignando valores al objeto Actividad
        actividad.setIdAsignacion(datosActividad.getStringExtra("idAsignacion"));
        //Views de la actividad
        toolbar = (Toolbar) findViewById(R.id.tool_bar); //ToolBar
        linearLayoutP= (LinearLayout) findViewById(R.id.layoutActividadP);
        linearLayout= (LinearLayout) findViewById(R.id.layoutActividad); //LinearLayout
        scrollView=(ScrollView) findViewById(R.id.scrollViewActividad);//ScrollLayout
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(actividad.getColor())));//Colores Actividad
        linearLayoutP.setBackgroundColor(Color.parseColor(actividad.getColor()));
        scrollView.setBackgroundColor(Color.parseColor(actividad.getColor()));
        linearLayout.setBackgroundColor(Color.parseColor(actividad.getColor()));
        etxtTitulo= (TextView) findViewById(R.id.etxtTitulo);
        etxtTitulo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                actividad.setTitulo(etxtTitulo.getText().toString());
            }
        });
        etxtDescripcion= (TextView) findViewById(R.id.etxtDescripcion);
        etxtDescripcion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                actividad.setDescripcion(etxtDescripcion.getText().toString());
            }
        });
        //Inflar y remplazar el Textview de Recordarme
        inflater = LayoutInflater.from(this);
        txtRecordarme= (TextView) findViewById(R.id.txtRecordarme);
        final View Recordar= txtRecordarme;
        final ViewGroup parentRecordar= (ViewGroup) Recordar.getParent();
        final int index= parentRecordar.indexOfChild(Recordar);
        final View vista= inflater.inflate(R.layout.editable_fecha, null);
        txtRecordarme.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                flagClosed = false;
                parentRecordar.removeView(Recordar);
                parentRecordar.addView(vista, index);
                //Inicializar TimePickerDialo y DatePickerDialog
                Calendar dateNow= Calendar.getInstance();
                actividad.setYear(dateNow.YEAR);
                actividad.setMonthOfYear(dateNow.MONTH);
                actividad.setDayOfMonth(dateNow.DAY_OF_MONTH);
                actividad.setHourOfDay(dateNow.HOUR_OF_DAY);
                actividad.setMinute(dateNow.MINUTE);
                datePickerDialog=DatePickerDialog.newInstance(
                        Actividades.this,
                        dateNow.get(Calendar.YEAR),
                        dateNow.get(Calendar.MONTH),
                        dateNow.get(Calendar.DAY_OF_MONTH)
                );
                timePickerDialog=TimePickerDialog.newInstance(
                        Actividades.this,
                        dateNow.get(Calendar.HOUR_OF_DAY),
                        dateNow.get(Calendar.MINUTE),
                        false
                );
            }
        });
        // Boton Cerra de recordar
        ImageView close= (ImageView) vista.findViewById(R.id.editablefechaclose);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flagClosed= true;
                parentRecordar.removeView(vista);
                parentRecordar.addView(Recordar, index);
                textFecha.setText("Dia");
                textHora.setText("Hora");
            }
        });
        //TextView Remplazables Dia y hora
        textFecha= (TextView)vista.findViewById(R.id.textFecha);
        textFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show(getFragmentManager(),"DatetimePicker");
            }
        });


        textHora = (TextView)vista.findViewById(R.id.textHora);
        textHora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerDialog.show(getFragmentManager(), "Timepickerdialog");
            }
        });



        colorPickerDialog.initialize(R.string.dialog_title, new int[]{  //Inicializando la paleta de colores
                Color.parseColor("#e0e0e0"),//Color por Default
                Color.parseColor("#ff9800"),
                Color.parseColor("#ffeb3b"),
                Color.parseColor("#cddc39"),
                Color.parseColor("#ff80ab"),
                Color.parseColor("#42a5f5"),
                Color.parseColor("#00e676"),
                Color.parseColor("#90a4ae")}, Color.parseColor("#e0e0e0"), 4, 2);

        colorPickerDialog.setOnColorSelectedListener(new OnColorSelectedListener() { //Asignando color a la Actividad
            @Override
            public void onColorSelected(int color) {
                getSupportActionBar().setBackgroundDrawable(new ColorDrawable(color));//Asignando el color al Toolbar
                linearLayoutP.setBackgroundColor(color);
                scrollView.setBackgroundColor(color);
                linearLayout.setBackgroundColor(color);
                actividad.setColor("#" + Integer.toHexString(color).substring(2));// Asignando la propiedad color a la clase Actividad


            }
        });

        //Actividad Existente
        try
        {
            if(actividad.getActividadInformacion()!=null)
            {
                etxtTitulo.setText(actividad.getTitulo());
                etxtDescripcion.setText(actividad.getDescripcion());
                if(actividad.getFechaRealizacion()!="" && actividad.getFechaRealizacion()!="0000-00-00 00:00:00")
                {
                    DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date miDate= format.parse(actividad.getFechaRealizacion());
                    DateFormat formatd = new SimpleDateFormat("dd");
                    actividad.setDayOfMonth(Integer.parseInt(formatd.format(miDate)));
                    DateFormat formatm = new SimpleDateFormat("MM");
                    actividad.setMonthOfYear(Integer.parseInt(formatm.format(miDate)));
                    DateFormat formaty = new SimpleDateFormat("yyyy");
                    actividad.setYear(Integer.parseInt(formaty.format(miDate)));
                    actividad.setHourOfDay(miDate.getHours());
                    actividad.setMinute(miDate.getMinutes());
                    datePickerDialog=DatePickerDialog.newInstance(Actividades.this,actividad.getYear(),actividad.getMonthOfYear(),actividad.getDayOfMonth());
                    timePickerDialog= TimePickerDialog.newInstance(Actividades.this,actividad.getHourOfDay(),actividad.getMinute(),false);
                    parentRecordar.removeView(Recordar);
                    parentRecordar.addView(vista,index);
                    DateFormat time = new SimpleDateFormat("HH:mm");
                    String h =time.format(miDate);
                    textHora.setText(h);
                    DateFormat fec = new SimpleDateFormat("yyyy-MM-dd");
                    String f =time.format(miDate);
                    String decha = fec.format(miDate);
                    textFecha.setText(decha);

                }
            }
        }
        catch (Exception e)
        {

        }
    }
    @Override
    protected void onPause() {
        if(actividad.isChanged())
        {
            if(actividad.getIdActividad()==null)
            {
                actividad.setIdActividad(daoActividades.insertActividades(actividad.getIdAsignacion()));
            }
            daoActividades.actualizar(actividad.getIdActividad(),actividad.getTitulo(),actividad.getDescripcion(),actividad.getColor()+"",actividad.getYear(),actividad.getMonthOfYear(),actividad.getDayOfMonth(),actividad.getHourOfDay(),actividad.getMinute(),flagClosed);
        }
        super.onPause();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_actividades, menu);
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
        if (id== R.id.action_palette_color)
        {
            colorPickerDialog.show(getSupportFragmentManager(), "colorpicker");
            return true;
        }
        if (id == R.id.action_add_team)
        {
            if(actividad.getIdActividad()==null)
            {
                actividad.setIdActividad(daoActividades.insertActividades(actividad.getIdAsignacion()));

            }
            Intent intent = new Intent(getBaseContext(), PrincipalCrearEquipo.class);
            intent.putExtra("tipo","nuevo");
            intent.putExtra("idActividad",actividad.getIdActividad());
            startActivity(intent);
            return  true;
        }
        if(id == R.id.action_calificar)
        {
            if(actividad.getIdActividad()==null)
            {
                actividad.setIdActividad(daoActividades.insertActividades(actividad.getIdAsignacion()));

            }
            Intent intent = new Intent(getBaseContext(), PrincipalCalificaciones.class);
            intent.putExtra("idActividad",actividad.getIdActividad());
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int monthOfYear, int dayOfMont) {
        Calendar miCalendar = Calendar.getInstance();
        actividad.setDayOfMonth(dayOfMont);
        actividad.setMonthOfYear(monthOfYear);
        actividad.setYear(year);
        miCalendar.set(actividad.getYear(), actividad.getMonthOfYear(), actividad.getDayOfMonth());
        DateFormat time= new SimpleDateFormat("yyyy-MM-dd");
        textFecha.setText(time.format(miCalendar.getTime()));

    }

    @Override
    public void onTimeSet(RadialPickerLayout radialPickerLayout, int hourOfDay, int minute) {
        Calendar miCalendar = Calendar.getInstance();
        actividad.setHourOfDay(hourOfDay);
        actividad.setMinute(minute);
        miCalendar.set(0, 0, 0, actividad.getHourOfDay(), actividad.getMinute());
        DateFormat time=new SimpleDateFormat("HH:mm");
        textHora.setText(time.format(miCalendar.getTime()));
    }
}
