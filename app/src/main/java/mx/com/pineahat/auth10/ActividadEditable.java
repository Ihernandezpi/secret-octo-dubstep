package mx.com.pineahat.auth10;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import mx.com.pineahat.auth10.ColorPickerSwatch.OnColorSelectedListener;
import mx.com.pineahat.auth10.DAO.DAOActividades;
import mx.com.pineahat.auth10.Equipos.PrincipalCrearEquipo;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class ActividadEditable extends ActionBarActivity implements TimePickerDialog.OnTimeSetListener,DatePickerDialog.OnDateSetListener{

    private String key;
    private boolean flagClosed=false;
    private boolean changed=false;
    private EditText titulo;
    private EditText descripcion;
    private Toolbar toolbar;
    private TextView tRecordar;
    private TextView  fecha;
    private TextView  hora;
    Integer color = Color.parseColor("#bdbdbd");
    private LayoutInflater inflater;
    final ColorPickerDialog colorPickerDialog = new ColorPickerDialog();
    DrawerLayout Drawer;
    ActionBarDrawerToggle mDrawerToggle;
    private DAOActividades miDaoActividades;
    private LinearLayout linearLayout;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad_editable);
        titulo=(EditText)findViewById(R.id.etxtTitulo);
        descripcion=(EditText)findViewById(R.id.etxtDescripcion);
        miDaoActividades = new DAOActividades(getApplicationContext());
        Intent intent=getIntent();
        j = intent.getStringExtra("info");
        idAsignacion= intent.getStringExtra("idAsignacion");
        linearLayout=(LinearLayout) findViewById(R.id.layoutEditable);
        toolbar =(Toolbar) findViewById(R.id.tool_bar);
        tRecordar= (TextView) findViewById(R.id.txtRecordarme);
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
                year=now.YEAR;
                monthOfYear=now.MONTH;
                dayOfMonth=now.DAY_OF_MONTH;
                hourOfDay=now.HOUR_OF_DAY;
                minute=now.MINUTE;
                hourOfDay=now.HOUR_OF_DAY;
                minute=now.MINUTE;
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
                    colorSelect="#eceff1";

                } else if (color == Color.parseColor("#ec407a")) //rosa
                {
                    getSupportActionBar().setBackgroundDrawable(new
                            ColorDrawable(color));
                    linearLayout.setBackgroundColor(Color.parseColor("#ec407a"));
                    colorSelect="#ec407a";

                } else if (color == Color.parseColor("#42a5f5")) //azul
                {
                    getSupportActionBar().setBackgroundDrawable(new
                            ColorDrawable(color));
                    linearLayout.setBackgroundColor(Color.parseColor("#42a5f5"));
                    colorSelect="#42a5f5";

                } else if (color == Color.parseColor("#ffeb3b")) //amarillo
                {
                    getSupportActionBar().setBackgroundDrawable(new
                            ColorDrawable(Color.parseColor("#ffc107")));
                    linearLayout.setBackgroundColor(Color.parseColor("#ffeb3b"));
                    colorSelect="#ffeb3b";

                } else if (color == Color.parseColor("#7cb342")) //verdeolivo
                {
                    getSupportActionBar().setBackgroundDrawable(new
                            ColorDrawable(color));
                    linearLayout.setBackgroundColor(Color.parseColor("#7cb342"));
                    colorSelect="#7cb342";

                } else if (color == Color.parseColor("#69f0ae")) //por default coral
                {
                    getSupportActionBar().setBackgroundDrawable(new
                            ColorDrawable(color));
                    linearLayout.setBackgroundColor(Color.parseColor("#69f0ae"));
                    colorSelect="#69f0ae";

                } else if (color == Color.parseColor("#9c27b0")) //por default
                {
                    getSupportActionBar().setBackgroundDrawable(new
                            ColorDrawable(color));
                    linearLayout.setBackgroundColor(Color.parseColor("#e1bee7"));
                    colorSelect="#e1bee7";

                } else if (color == Color.parseColor("#9e9e9e")) //por default
                {
                    getSupportActionBar().setBackgroundDrawable(new
                            ColorDrawable(color));
                    linearLayout.setBackgroundColor(Color.parseColor("#9e9e9e"));
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
        if (id == R.id.action_settings) {
            return true;
        }
        if(id==R.id.action_add_team)
        {
            //Este es para nuevo
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
}
