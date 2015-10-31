package mx.com.pineahat.auth10.Equipos;

import android.content.Context;
import android.os.Vibrator;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

import mx.com.pineahat.auth10.DAO.DAOEquipos;
import mx.com.pineahat.auth10.R;

public class IntegrantesEquipoAdapter extends RecyclerView.Adapter<IntegrantesEquipoAdapter.ViewHolder> {
    private JSONArray mDataset;
    private Vibrator vibrator;
    private ViewGroup parent;
    private ArrayList<Integrantes> misIntegrantes;
    public int SUMA = 1;
    private EditText nomrbeRquipo;
    private String nomEquipo ="";
    private boolean flag=false;
    private String idEquipo=null;
    DAOEquipos miDaoEquipos;
    private String idActividad;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View mTextView;
        public ViewHolder(View v) {
            super(v);
            mTextView = v;
        }
    }
    public IntegrantesEquipoAdapter(JSONArray myDataset,ArrayList<Integrantes> misIntegrantes,String idEquipo, Context context, String idActividad) {
        miDaoEquipos= new DAOEquipos(context);
        this.mDataset = myDataset;
        this.misIntegrantes= misIntegrantes;
        this.idEquipo=idEquipo;
        this.idActividad=idActividad;
    }
    public IntegrantesEquipoAdapter(JSONArray myDataset,ArrayList<Integrantes> misIntegrantes,String nomEquipo,String idEquipo,Context context,String idActividad) {
        miDaoEquipos= new DAOEquipos(context);
        this.mDataset = myDataset;
        this.misIntegrantes= misIntegrantes;
        this.nomEquipo=nomEquipo;
        this.idEquipo=idEquipo;
        this.idActividad=idActividad;
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
            this.nomrbeRquipo.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    flag=true;

                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
        }
        else {
            try {
                final JSONObject alumno = mDataset.getJSONObject(position-SUMA);
                TextView nombre = (TextView)holder.mTextView.findViewById(R.id.txtNombreAlumno);
                nombre.setText(alumno.getString("nombre")+" "+alumno.getString("apellidoP"));
                //Despues asignar imagen
                final CheckBox miCheckBox =(CheckBox)holder.mTextView.findViewById(R.id.checkBoxIntegrante);
                if(!alumno.getString("estado").equals("fuera"))
                {
                    miCheckBox.setChecked(true);
//                    addIntegrante(alumno);
                }
                else
                {
                    miCheckBox.setChecked(false);
                    deleteIntegrante(alumno);
                }
                miCheckBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                       flag=true;
                        String idAlumno="";
                        try {
                            idAlumno = mDataset.getJSONObject(position - SUMA).getString("idAlumno");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if(miCheckBox.isChecked())
                        {
                            flag=true;
                            Log.d("============","Se agregó "+idAlumno);
                            addIntegrante(alumno);
                        }
                        else
                        {
                            flag=true;
                            Log.d("============","Se Eliminó "+idAlumno);
                            deleteIntegrante(alumno);
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


    private void addIntegrante(JSONObject alumno) {

        if (this.idEquipo == null) {
            this.idEquipo = miDaoEquipos.generarEquipo(this.idActividad, getNombre());
        }
        miDaoEquipos.cambiarNombre(getNombre(),idEquipo);
        miDaoEquipos.agregarIntegrante(alumno,idEquipo);

    }
    private void deleteIntegrante(JSONObject alumno)
    {
        if (this.idEquipo == null) {
            this.idEquipo = miDaoEquipos.generarEquipo(this.idActividad, getNombre());
        }
        miDaoEquipos.cambiarNombre(getNombre(),idEquipo);
        miDaoEquipos.eliminarIntegrante(alumno,idEquipo);
    }

    public ArrayList<Integrantes> getMisIntegrantes() {
        return misIntegrantes;
    }

    public String getNombre ()
    {
        return nomrbeRquipo.getText().toString();
    }

    public boolean getFlag ()
    {
        return this.flag;
    }

}

