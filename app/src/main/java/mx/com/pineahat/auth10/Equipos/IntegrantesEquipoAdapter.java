package mx.com.pineahat.auth10.Equipos;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Vibrator;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import mx.com.pineahat.auth10.ActividadEditable;
import mx.com.pineahat.auth10.DAO.DAOActividades;
import mx.com.pineahat.auth10.MyFragment;
import mx.com.pineahat.auth10.R;

public class IntegrantesEquipoAdapter extends RecyclerView.Adapter<IntegrantesEquipoAdapter.ViewHolder> {
    private JSONArray mDataset;
    private Vibrator vibrator;
    private ViewGroup parent;
    private ArrayList<Integrantes> misIntegrantes;
    public int SUMA = 1;
    private EditText nomrbeRquipo;
    private String nomEquipo ="";

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View mTextView;
        public ViewHolder(View v) {
            super(v);
            mTextView = v;
        }
    }
    public IntegrantesEquipoAdapter(JSONArray myDataset,ArrayList<Integrantes> misIntegrantes) {
        this.mDataset = myDataset;
        this.misIntegrantes= misIntegrantes;
    }
    public IntegrantesEquipoAdapter(JSONArray myDataset,ArrayList<Integrantes> misIntegrantes,String nomEquipo) {
        this.mDataset = myDataset;
        this.misIntegrantes= misIntegrantes;
        this.nomEquipo=nomEquipo;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.parent = parent;
        View v;
        ViewHolder vh = null;
        if(viewType==1) {
             v = LayoutInflater.from(parent.getContext()).inflate(R.layout.integrante_equipo_adapter, parent, false);
             vh = new ViewHolder(v);
        }
        else if(viewType==0) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.nombre_equipo_adapter, parent, false);
            vh = new ViewHolder(v);
        }

        return vh;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if(position==0)
        {
            this.nomrbeRquipo =(EditText) holder.mTextView.findViewById(R.id.txtNombreEquipo);
            if(!nomEquipo.equals(""))
            {
                this.nomrbeRquipo.setText(nomEquipo);
            }
        }
        else {
            try {
                JSONObject alumno = mDataset.getJSONObject(position-SUMA);
                TextView nombre = (TextView)holder.mTextView.findViewById(R.id.txtNombreAlumno);
                nombre.setText(alumno.getString("nombre")+" "+alumno.getString("apellidoP"));
                //Despues asignar imagen
                final CheckBox miCheckBox =(CheckBox)holder.mTextView.findViewById(R.id.checkBoxIntegrante);
                if(!alumno.getString("estado").equals("fuera"))
                {
                    miCheckBox.setChecked(true);
                    addIntegrante(alumno.getString("idAlumno").toString());
                }
                miCheckBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String idAlumno="";
                        try {
                            idAlumno = mDataset.getJSONObject(position - SUMA).getString("idAlumno");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if(miCheckBox.isChecked())
                        {
                                Log.d("============","Se agregó "+idAlumno);
                                addIntegrante(idAlumno);

                        }
                        else
                        {
                                Log.d("============","Se Eliminó "+idAlumno);
                                deleteIntegrante(idAlumno);
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public int getItemCount() {
        return mDataset.length()+SUMA;
    }
    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return 0;
        return 1;
    }
    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    private void addIntegrante(String idAlumno)
    {
        Integrantes integrantes = new Integrantes(idAlumno);
        misIntegrantes.add(integrantes);

        for (Integrantes integrantess:misIntegrantes) {
            Log.d("==========",integrantess.getIdAlumno());
        }
    }
    private void deleteIntegrante(String idAlumno)
    {
        for (int i=0;i<misIntegrantes.size();i++) {
            Integrantes integrantes= misIntegrantes.get(i);
            if(integrantes.getIdAlumno().equals(idAlumno))
            {
                misIntegrantes.remove(i);
            }
        }
    }

    public ArrayList<Integrantes> getMisIntegrantes() {
        return misIntegrantes;
    }

    public String getNombre ()
    {
        return nomrbeRquipo.getText().toString();
    }

}
