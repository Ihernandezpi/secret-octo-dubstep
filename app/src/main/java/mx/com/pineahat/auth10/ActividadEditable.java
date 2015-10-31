package mx.com.pineahat.auth10;

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


public class ActividadEditable extends ActionBarActivity implements TimePickerDialog.OnTimeSetListener,DatePickerDialog.OnDateSetListener{

    private String key;
    private boolean flagClosed=false;
    private boolean changed=false;
    private EditText titulo;
    private EditText descripcion;
    private Toolbar toolbar;
    private TextView tRecordar;
    private TextView tAgregarEquipo;
    private TextView tEquiposTi;
    private TextView  fecha;
    private TextView  hora;
    Integer color = Color.parseColor("#bdbdbd");
    private LayoutInflater inflater;
    final ColorPickerDialog colorPickerDialog = new ColorPickerDialog();
    DrawerLayout Drawer;
    ActionBarDrawerToggle mDrawerToggle;
    private DAOActividades miDaoActividades;
    private LinearLayout linearLayout;
    private ScrollView scrollView;
    private String colorSelect="";
    private DatePickerDialog datePickerDialog;
    private int year=0;
    private int monthOfYear=0;
    private int dayOfMonth=0;
    private RadialPickerLayout view;
    private int hourOfDay=0;
    private int minute=0;
    private TimePickerDialog tpd;
    private DatePickerDialog dpd;
    private String j;
    String idAsignacion;
    JSONArray jsonArrayEquipos =new JSONArray();
    ListCheckboxDialog a;
    DAOEquipos midaoEquipos;
    private ListView listaEquipos;
    ListAdapterEquipos myListAdapterEquipos=null;

    ArrayList<Equipo> equipos=new ArrayList<Equipo>();
    int number_coun=1;


    @Override
    protected void onResume() {
        super.onResume();
        equipos=new ArrayList<Equipo>();
        equipos=midaoEquipos.equipoArrayList(key);
        myListAdapterEquipos= new ListAdapterEquipos(ActividadEditable.this,R.layout.item_equipos,equipos);
        listaEquipos.setAdapter(myListAdapterEquipos);
        try {
            Bundle resp = a.getArguments();
            key=resp.getString("key");
            jsonArrayEquipos= (JSONArray) resp.get("JSONArrayEquipo");
        }catch (Exception e)
        {}

        // metodo para reconstruir los equipos
        equipos=new ArrayList<Equipo>();
        equipos=midaoEquipos.equipoArrayList(key);
        myListAdapterEquipos= new ListAdapterEquipos(ActividadEditable.this,R.layout.item_equipos,equipos);
        listaEquipos.setAdapter(myListAdapterEquipos);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad_editable);
        a= ListCheckboxDialog.newInstance(this);
        titulo=(EditText)findViewById(R.id.etxtTitulo);
        descripcion=(EditText)findViewById(R.id.etxtDescripcion);
        miDaoActividades = new DAOActividades(getApplicationContext());
        midaoEquipos=new DAOEquipos(getApplicationContext());
        Intent intent=getIntent();
        j = intent.getStringExtra("info");
        idAsignacion= intent.getStringExtra("idAsignacion");
        linearLayout=(LinearLayout) findViewById(R.id.layoutEditable);
        scrollView=(ScrollView) findViewById(R.id.scrollView1);
        toolbar =(Toolbar) findViewById(R.id.tool_bar);
        tRecordar= (TextView) findViewById(R.id.txtRecordarme);
        tAgregarEquipo= (TextView) findViewById(R.id.txtAgregarEquipo);
        tEquiposTi=(TextView) findViewById(R.id.txtEquipoTI);
        toolbar.setBackgroundColor(color);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        inflater = LayoutInflater.from(this);
        final View Recordar= tRecordar;
        final ViewGroup parent= (ViewGroup) Recordar.getParent();
        final int index=parent.indexOfChild(Recordar);
        final View vista = inflater.inflate(R.layout.editable_fecha, null);
        ImageView close = (ImageView) vista.findViewById(R.id.editablefechaclose);

        listaEquipos= (ListView) findViewById(R.id.listViewEquipos);


        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flagClosed=true;
                Toast.makeText(getApplicationContext(), " close", Toast.LENGTH_SHORT).show();
                parent.removeView(vista);
                parent.addView(Recordar, index);
                fecha.setText("Dia");
                hora.setText("Hora");
                year=0;
                monthOfYear=0;
                dayOfMonth=0;
                hourOfDay=0;
                minute=0;
            }
        });

        fecha= (TextView)vista.findViewById(R.id.textFecha);


        fecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dpd.show(getFragmentManager(), "Datepickerdialog");
            }
        });
        hora=(TextView)vista.findViewById(R.id.textHora);
        hora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (j==null|| flagClosed==true)
                {
                    Calendar now = Calendar.getInstance();
                    tpd = TimePickerDialog.newInstance(
                            ActividadEditable.this,
                            now.get(Calendar.HOUR_OF_DAY),
                            now.get(Calendar.MINUTE),
                            false
                    );
                }
                else
                {
                    tpd = TimePickerDialog.newInstance(ActividadEditable.this,hourOfDay,minute,false);
                }

                tpd.show(getFragmentManager(), "Timepickerdialog");
            }
        });

        tRecordar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flagClosed=false;
                parent.removeView(Recordar);
                parent.addView(vista, index);
                Calendar now = Calendar.getInstance();
                year= Calendar.YEAR;
                monthOfYear= Calendar.MONTH;
                dayOfMonth= Calendar.DAY_OF_MONTH;
                hourOfDay= Calendar.HOUR_OF_DAY;
                minute= Calendar.MINUTE;
                hourOfDay= Calendar.HOUR_OF_DAY;
                minute= Calendar.MINUTE;
                dpd = DatePickerDialog.newInstance(
                        ActividadEditable.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                tpd = TimePickerDialog.newInstance(
                        ActividadEditable.this,
                        now.get(Calendar.HOUR_OF_DAY),
                        now.get(Calendar.MINUTE),
                        false
                );
            }
        });
        tAgregarEquipo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (key == null) {
                    key = miDaoActividades.insertActividades(idAsignacion);
                }
                Intent intent = new Intent(getBaseContext(), PrincipalCrearEquipo.class);
                intent.putExtra("tipo","nuevo");
                intent.putExtra("idActividad",key);
                startActivity(intent);
                /**
                 * Intent intent = new Intent(getBaseContext(), PrincipalCrearEquipo.class);
                 intent.putExtra("tipo","modificar");
                 intent.putExtra("idEquipo","10");
                 intent.putExtra("idActividad",key);
                 startActivity(intent);
                 */
                Toast.makeText(getApplicationContext(), "Agregar mx.com.pineahat.auth10.Equipo", Toast.LENGTH_SHORT).show();

            }
        });



        if (j==null)
        {
            //Nuevo
            colorSelect="#bdbdbd";
            Calendar now = Calendar.getInstance();

        }
        else
        {
            //MOdificar
            try {
                JSONObject miJsonObject= new JSONObject(j);
                key=miJsonObject.getString("idActividades");
                color=Color.parseColor(miJsonObject.getString("color"));
                colorSelect =miJsonObject.getString("color");
                getSupportActionBar().setBackgroundDrawable(new ColorDrawable(color));
                linearLayout.setBackgroundColor(color);
                scrollView.setBackgroundColor(color);
                titulo.setText(miJsonObject.getString("nombre"));
                descripcion.setText(miJsonObject.getString("descripcion"));
                if(!miJsonObject.getString("fechaRealizacion").equals("")  && !miJsonObject.getString("fechaRealizacion").equals("0000-00-00 00:00:00"))
                {
                    DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date miDate= format.parse(miJsonObject.getString("fechaRealizacion"));
                    Log.d("......................",miJsonObject.getString("fechaRealizacion"));
                    DateFormat formatd = new SimpleDateFormat("dd");
                    this.dayOfMonth=Integer.parseInt(formatd.format(miDate));
                    DateFormat formatm = new SimpleDateFormat("MM");
                    this.monthOfYear=Integer.parseInt(formatm.format(miDate));
                    DateFormat formaty = new SimpleDateFormat("yyyy");
                    this.year=Integer.parseInt(formaty.format(miDate));
                    this.hourOfDay=miDate.getHours();
                    this.minute=miDate.getMinutes();
                    dpd = DatePickerDialog.newInstance(ActividadEditable.this, this.year, this.monthOfYear, this.dayOfMonth);
                    tpd = TimePickerDialog.newInstance(ActividadEditable.this,this.hourOfDay,this.minute,false);
                    parent.removeView(Recordar);
                    parent.addView(vista, index);
                    DateFormat time = new SimpleDateFormat("HH:mm");
                    String h =time.format(miDate);
                    hora.setText(h);
                    DateFormat fec = new SimpleDateFormat("yyyy-MM-dd");
                    String f =time.format(miDate);
                    String decha = fec.format(miDate);
                    fecha.setText(decha);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        jsonArrayEquipos= miDaoActividades.equiposTI(key,idAsignacion);
        //agregar Items a la lista
        equipos=midaoEquipos.equipoArrayList(key);

        myListAdapterEquipos= new ListAdapterEquipos(ActividadEditable.this,R.layout.item_equipos,equipos);
        //mylistJsonArrayEquipos= new ListJsonArrayEquipos(ActividadEditable.this,jsonArrayEquipos);
        listaEquipos.setAdapter(myListAdapterEquipos);

        tEquiposTi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jsonArrayEquipos= miDaoActividades.equiposTI(key,idAsignacion);
                Bundle parametros = new Bundle();
                parametros.putString("JSONArrayEquipos", jsonArrayEquipos.toString());
                parametros.putString("key", key);
                parametros.putString("idAsignacion", idAsignacion);
                a.setArguments(parametros);
                a.show(getSupportFragmentManager(), "Dialog");


                // Cada vez que se clickea se reconstruyetodo
                // metodo para reconstruir los equipos
                equipos=new ArrayList<Equipo>();
                equipos=midaoEquipos.equipoArrayList(key);
                myListAdapterEquipos= new ListAdapterEquipos(ActividadEditable.this,R.layout.item_equipos,equipos);
                listaEquipos.setAdapter(myListAdapterEquipos);

            }
        });

        colorPickerDialog.initialize(R.string.dialog_title, new int[]{
                Color.parseColor("#bdbdbd"),
                Color.parseColor("#ec407a"),
                Color.parseColor("#42a5f5"),
                Color.parseColor("#ffeb3b"),
                Color.parseColor("#7cb342"),
                Color.parseColor("#69f0ae"),
                Color.parseColor("#9c27b0"),
                Color.parseColor("#9e9e9e")}, Color.parseColor("#bdbdbd"), 4, 2);
        colorPickerDialog.setOnColorSelectedListener(new OnColorSelectedListener() {
            @Override
            public void onColorSelected(int color) {
                changed=true;
                if (color == Color.parseColor("#bdbdbd")) //por default
                {
                    getSupportActionBar().setBackgroundDrawable(new
                            ColorDrawable(color));
                    linearLayout.setBackgroundColor(Color.parseColor("#eceff1"));
                    scrollView.setBackgroundColor(Color.parseColor("#eceff1"));
                    colorSelect="#eceff1";

                } else if (color == Color.parseColor("#ec407a")) //rosa
                {
                    getSupportActionBar().setBackgroundDrawable(new
                            ColorDrawable(color));
                    linearLayout.setBackgroundColor(Color.parseColor("#ec407a"));
                    scrollView.setBackgroundColor(Color.parseColor("#ec407a"));
                    colorSelect="#ec407a";

                } else if (color == Color.parseColor("#42a5f5")) //azul
                {
                    getSupportActionBar().setBackgroundDrawable(new
                            ColorDrawable(color));
                    linearLayout.setBackgroundColor(Color.parseColor("#42a5f5"));
                    scrollView.setBackgroundColor(Color.parseColor("#42a5f5"));
                    colorSelect="#42a5f5";

                } else if (color == Color.parseColor("#ffeb3b")) //amarillo
                {
                    getSupportActionBar().setBackgroundDrawable(new
                            ColorDrawable(Color.parseColor("#ffc107")));
                    linearLayout.setBackgroundColor(Color.parseColor("#ffeb3b"));
                    scrollView.setBackgroundColor(Color.parseColor("#ffeb3b"));
                    colorSelect="#ffeb3b";

                } else if (color == Color.parseColor("#7cb342")) //verdeolivo
                {
                    getSupportActionBar().setBackgroundDrawable(new
                            ColorDrawable(color));
                    linearLayout.setBackgroundColor(Color.parseColor("#7cb342"));
                    scrollView.setBackgroundColor(Color.parseColor("#7cb342"));
                    colorSelect="#7cb342";

                } else if (color == Color.parseColor("#69f0ae")) //por default coral
                {
                    getSupportActionBar().setBackgroundDrawable(new
                            ColorDrawable(color));
                    linearLayout.setBackgroundColor(Color.parseColor("#69f0ae"));
                    scrollView.setBackgroundColor(Color.parseColor("#69f0ae"));
                    colorSelect="#69f0ae";

                } else if (color == Color.parseColor("#9c27b0")) //por default
                {
                    getSupportActionBar().setBackgroundDrawable(new
                            ColorDrawable(color));
                    linearLayout.setBackgroundColor(Color.parseColor("#e1bee7"));
                    scrollView.setBackgroundColor(Color.parseColor("#e1bee7"));
                    colorSelect="#e1bee7";

                } else if (color == Color.parseColor("#9e9e9e")) //por default
                {
                    getSupportActionBar().setBackgroundDrawable(new
                            ColorDrawable(color));
                    linearLayout.setBackgroundColor(Color.parseColor("#9e9e9e"));
                    scrollView.setBackgroundColor(Color.parseColor("#9e9e9e"));
                    colorSelect="#9e9e9e";

                }
            }
        });
        titulo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                changed = true;
            }
        });
        descripcion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                changed=true;
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
        if (id == R.id.action_calificar) {
            if (key == null) {
                key = miDaoActividades.insertActividades(idAsignacion);
            }
            Intent intent = new Intent(getBaseContext(), PrincipalCalificaciones.class);
            intent.putExtra("idActividad",key);
            startActivity(intent);
            return true;
        }

        if(id==R.id.action_palette_color)
        {
            colorPickerDialog.show(getSupportFragmentManager(), "colorpicker");
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int monthOfYear, int dayOfMonth) {
        Calendar miCalendar = Calendar.getInstance();
        this.dayOfMonth=dayOfMonth;
        this.monthOfYear=monthOfYear;
        this.year=year;
        miCalendar.set(this.year,this.monthOfYear,this.dayOfMonth);
        DateFormat time = new SimpleDateFormat("yyyy-MM-dd");
        String h =time.format(miCalendar.getTime());
        fecha.setText(h);
        changed=true;
    }
    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
        Calendar miCalendar = Calendar.getInstance();
        this.hourOfDay = hourOfDay;
        this.minute=minute;
        miCalendar.set(0,0,0,this.hourOfDay,this.minute);
        DateFormat time = new SimpleDateFormat("HH:mm");
        String h =time.format(miCalendar.getTime());
        hora.setText(h);
        changed=true;
    }

    @Override
    protected void onPause() {
        if(changed) {
            if (key == null) {
                key = miDaoActividades.insertActividades(idAsignacion);
            }
            DAOActividades miDaoActividades = new DAOActividades(getApplicationContext());
            miDaoActividades.actualizar(key, titulo.getText().toString(), descripcion.getText().toString(), colorSelect, this.year, this.monthOfYear, this.dayOfMonth, this.hourOfDay, this.minute, flagClosed);
        }
        super.onPause();
    }

    public void avisar() {
        onResume();
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

            imageViewClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(daoEquipos.eliminarEquipo(equipos.get(position)))
                        equipos.remove(position);

                    myListAdapterEquipos.notifyDataSetChanged();
                    Toast.makeText(ActividadEditable.this, "Item Delete", Toast.LENGTH_SHORT).show();

                }
            });
            textViewEquipos.setText(equipos.get(position).getNombre().toString());
            return view;

        }
    }
}


