package mx.com.pineahat.auth10;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import mx.com.pineahat.auth10.ColorPickerSwatch.OnColorSelectedListener;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
import java.util.Calendar;



public class ActividadEditable extends ActionBarActivity
        implements
        TimePickerDialog.OnTimeSetListener,
        DatePickerDialog.OnDateSetListener{

    private Toolbar toolbar;
    private TextView tRecordar;
    private TextView  fecha;
    private TextView  hora;
    Integer color = Color.parseColor("#bdbdbd");
    private LayoutInflater inflater;
    final ColorPickerDialog colorPickerDialog = new ColorPickerDialog();

    DrawerLayout Drawer;
    ActionBarDrawerToggle mDrawerToggle;


   private LinearLayout linearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad_editable);

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
                Toast.makeText(getApplicationContext(), " close", Toast.LENGTH_SHORT).show();
                parent.removeView(vista);
                parent.addView(Recordar, index);
                fecha.setText("Dia");
                hora.setText("Hora");

            }
        });

       fecha= (TextView)vista.findViewById(R.id.textFecha);
        fecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        ActividadEditable.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );

                dpd.show(getFragmentManager(), "Datepickerdialog");
            }
        });
         hora=(TextView)vista.findViewById(R.id.textHora);
        hora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                TimePickerDialog tpd = TimePickerDialog.newInstance(
                        ActividadEditable.this,
                        now.get(Calendar.HOUR_OF_DAY),
                        now.get(Calendar.MINUTE),
                       false
                );
                tpd.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        Log.d("TimePicker", "Dialog was cancelled");
                    }
                });
                tpd.show(getFragmentManager(), "Timepickerdialog");
            }
        });

        tRecordar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parent.removeView(Recordar);
                parent.addView(vista, index);

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
                if (color == Color.parseColor("#bdbdbd")) //por default
                {
                    getSupportActionBar().setBackgroundDrawable(new
                            ColorDrawable(color));
                    linearLayout.setBackgroundColor(Color.parseColor("#eceff1"));

                } else if (color == Color.parseColor("#ec407a")) //rosa
                {
                    getSupportActionBar().setBackgroundDrawable(new
                            ColorDrawable(color));
                    linearLayout.setBackgroundColor(Color.parseColor("#ec407a"));

                } else if (color == Color.parseColor("#42a5f5")) //azul
                {
                    getSupportActionBar().setBackgroundDrawable(new
                            ColorDrawable(color));
                    linearLayout.setBackgroundColor(Color.parseColor("#42a5f5"));

                } else if (color == Color.parseColor("#ffeb3b")) //amarillo
                {
                    getSupportActionBar().setBackgroundDrawable(new
                            ColorDrawable(Color.parseColor("#ffc107")));
                    linearLayout.setBackgroundColor(Color.parseColor("#ffeb3b"));

                } else if (color == Color.parseColor("#7cb342")) //verdeolivo
                {
                    getSupportActionBar().setBackgroundDrawable(new
                            ColorDrawable(color));
                    linearLayout.setBackgroundColor(Color.parseColor("#7cb342"));

                } else if (color == Color.parseColor("#69f0ae")) //por default coral
                {
                    getSupportActionBar().setBackgroundDrawable(new
                            ColorDrawable(color));
                    linearLayout.setBackgroundColor(Color.parseColor("#69f0ae"));

                } else if (color == Color.parseColor("#9c27b0")) //por default
                {
                    getSupportActionBar().setBackgroundDrawable(new
                            ColorDrawable(color));
                    linearLayout.setBackgroundColor(Color.parseColor("#e1bee7"));

                } else if (color == Color.parseColor("#9e9e9e")) //por default
                {
                    getSupportActionBar().setBackgroundDrawable(new
                            ColorDrawable(color));
                    linearLayout.setBackgroundColor(Color.parseColor("#9e9e9e"));

                }

                //  Toast.makeText(getApplicationContext(), "selectedColor : " + color, Toast.LENGTH_SHORT).show();
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



                    colorPickerDialog.show(getSupportFragmentManager(), "colorpicker");




        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int monthOfYear, int dayOfMonth) {
        String date = "El "+dayOfMonth+"/"+monthOfYear+"/"+year;
        fecha.setText(date);
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
        String hourString = hourOfDay < 10 ? "0"+hourOfDay : ""+hourOfDay;
        String minuteString = minute < 10 ? "0"+minute : ""+minute;
        String time = "a las "+hourString+"h"+minuteString;
        hora.setText(time);
    }
}
