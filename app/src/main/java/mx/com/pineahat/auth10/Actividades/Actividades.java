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
import mx.com.pineahat.auth10.Equipo;
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

import mx.com.pineahat.auth10.ListCheckboxDialog;
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
    //Equipos
    JSONArray jsonArrayEquipos =new JSONArray();
    private TextView tEquiposTi;
    ListCheckBoxDialog listCheckboxDialogEquipos;
    DAOEquipos midaoEquipos;
    private ListView listaEquipos;
    ListAdapterEquipos myListAdapterEquipos=null;
    ArrayList<Equipo> equipos=new ArrayList<Equipo>();



    public void avisar() {
        onResume();
    }
    @Override
    protected void onResume() {
        super.onResume();
        equipos=new ArrayList<Equipo>();
        equipos=daoEquipos.equipoArrayList(actividad.getIdActividad());
        myListAdapterEquipos= new ListAdapterEquipos(Actividades.this,R.layout.item_equipos,equipos);
        listaEquipos.setAdapter(myListAdapterEquipos);
        setListViewHeightBasedOnChildren(listaEquipos);
        try {
            Bundle resp = listCheckboxDialogEquipos.getArguments();
            actividad.setIdActividad(resp.getString("key"));
            jsonArrayEquipos= (JSONArray) resp.get("JSONArrayEquipo");
        }catch (Exception e)
        {}

        // metodo para reconstruir los equipos
        equipos=new ArrayList<Equipo>();
        equipos=daoEquipos.equipoArrayList(actividad.getIdActividad());
        myListAdapterEquipos= new ListAdapterEquipos(Actividades.this,R.layout.item_equipos,equipos);
        listaEquipos.setAdapter(myListAdapterEquipos);
        setListViewHeightBasedOnChildren(listaEquipos);
    }
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
                actividad.setYear(dateNow.get(Calendar.YEAR));
                actividad.setMonthOfYear(dateNow.get(Calendar.MONTH)+1);
                actividad.setDayOfMonth(dateNow.get(Calendar.DAY_OF_MONTH));
                actividad.setHourOfDay(dateNow.get(Calendar.HOUR_OF_DAY));
                actividad.setMinute( dateNow.get(Calendar.MINUTE));
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
                actividad.setYear(0);
                actividad.setMonthOfYear(0);
                actividad.setDayOfMonth(0);
                actividad.setHourOfDay(0);
                actividad.setMinute(0);
                actividad.setChanged(true);
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
                    datePickerDialog=DatePickerDialog.newInstance(Actividades.this,actividad.getYear(),actividad.getMonthOfYear()-1,actividad.getDayOfMonth());
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
                    actividad.setChanged(false);

                }
            }
        }
        catch (Exception e)
        {
            actividad.setChanged(false);
        }
        //Equipos
        listCheckboxDialogEquipos=ListCheckBoxDialog.newInstance(this);
        tEquiposTi=(TextView) findViewById(R.id.txtEquipoTI);
        listaEquipos= (ListView) findViewById(R.id.listViewEquipos);
        jsonArrayEquipos=daoActividades.equiposTI(actividad.getIdActividad(), actividad.getIdAsignacion());
        equipos=daoEquipos.equipoArrayList(actividad.getIdActividad());
        myListAdapterEquipos= new ListAdapterEquipos(Actividades.this,R.layout.item_equipos,equipos);
        listaEquipos.setAdapter(myListAdapterEquipos);
        setListViewHeightBasedOnChildren(listaEquipos);
        // Si no hay equipos de TI deshabilita el TEXTVIEW
        if(jsonArrayEquipos.length()==0)
        {
            tEquiposTi.setVisibility(View.INVISIBLE);
        }
        tEquiposTi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(getApplicationContext(), "Item Delete", Toast.LENGTH_SHORT).show();
                jsonArrayEquipos = daoActividades.equiposTI(actividad.getIdActividad(), actividad.getIdAsignacion());
                Bundle parametros = new Bundle();
                parametros.putString("JSONArrayEquipos", jsonArrayEquipos.toString());
                parametros.putString("key", actividad.getIdActividad());
                parametros.putString("idAsignacion", actividad.getIdAsignacion());
                listCheckboxDialogEquipos.setArguments(parametros);
                listCheckboxDialogEquipos.show(getSupportFragmentManager(), "Dialog");
                // metodo para reconstruir los equipos
                equipos = new ArrayList<Equipo>();
                equipos = daoEquipos.equipoArrayList(actividad.getIdActividad());
                myListAdapterEquipos = new ListAdapterEquipos(Actividades.this, R.layout.item_equipos, equipos);
                listaEquipos.setAdapter(myListAdapterEquipos);
                setListViewHeightBasedOnChildren(listaEquipos);

            }
        });
        listaEquipos.setScrollContainer(false);
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
        if(id==android.R.id.home)
        {
            finish();
        }
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
        actividad.setMonthOfYear(monthOfYear + 1);
        actividad.setYear(year);
        actividad.setChanged(true);
        actividad.setHourOfDay(miCalendar.get(Calendar.HOUR_OF_DAY));
        actividad.setMinute(miCalendar.get(Calendar.MINUTE));
        miCalendar.set(actividad.getYear(), actividad.getMonthOfYear(), actividad.getDayOfMonth());
        DateFormat time= new SimpleDateFormat("yyyy-MM-dd");
      //  textFecha.setText(time.format(miCalendar.getTime()));
        textFecha.setText(""+actividad.getYear()+"-"+ actividad.getMonthOfYear()+"-"+actividad.getDayOfMonth());
        textHora.setText(""+actividad.getHourOfDay()+":"+actividad.getMinute());

    }

    @Override
    public void onTimeSet(RadialPickerLayout radialPickerLayout, int hourOfDay, int minute) {
        Calendar miCalendar = Calendar.getInstance();
        actividad.setHourOfDay(hourOfDay);
        actividad.setMinute(minute);
        actividad.setChanged(true);
        miCalendar.set(0, 0, 0, actividad.getHourOfDay(), actividad.getMinute());
        DateFormat time=new SimpleDateFormat("HH:mm");
        textHora.setText(time.format(miCalendar.getTime()));
        textFecha.setText("" + actividad.getYear() + "-" + actividad.getMonthOfYear() + "-" + actividad.getDayOfMonth());
    }
    public class ListAdapterEquipos extends ArrayAdapter<Equipo>
    {
        ArrayList<Equipo> equipos;
        private TextView textViewEquipos;
        DAOEquipos daoEquipos;

        public ListAdapterEquipos(Context context,int textViewResourceId, ArrayList<Equipo> objects) {
            super(context,  textViewResourceId, objects);
            this.equipos=objects;
            daoEquipos= new DAOEquipos(context);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view=null;
            LayoutInflater inflater=getLayoutInflater();
            view=inflater.inflate(R.layout.item_equipos,parent,false);
            TextView textViewEquipos= (TextView) view.findViewById(R.id.textEquipo);
            ImageView imageViewClose=(ImageView) view.findViewById(R.id.equipoClose);
            textViewEquipos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //Toast.makeText(Actividades.this, "Item "+equipos.get(position).getNombre(), Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(getBaseContext(), PrincipalCrearEquipo.class);
                    intent.putExtra("tipo","modificar");
                    intent.putExtra("idActividad",actividad.getIdActividad());
                    intent.putExtra("idEquipo",equipos.get(position).getIdEquiposActividades());
                    startActivity(intent);


                }
            });
            imageViewClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(daoEquipos.eliminarEquipo(equipos.get(position)))
                        equipos.remove(position);

                    myListAdapterEquipos.notifyDataSetChanged();
                    Toast.makeText(Actividades.this, "Item Delete", Toast.LENGTH_SHORT).show();
                   avisar();

                }
            });

            textViewEquipos.setText(equipos.get(position).getNombre().toString());
            return view;

        }
    }
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
}
