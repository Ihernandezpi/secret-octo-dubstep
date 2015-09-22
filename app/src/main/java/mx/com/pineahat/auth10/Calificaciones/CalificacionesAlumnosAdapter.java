package mx.com.pineahat.auth10.Calificaciones;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import mx.com.pineahat.auth10.DAO.DAOCalificaciones;
import mx.com.pineahat.auth10.Equipos.Integrantes;
import mx.com.pineahat.auth10.R;

/**
 * Created by Ignacio on 19/09/2015.
 */
public class CalificacionesAlumnosAdapter extends ArrayAdapter
{

    public JSONArray miJsonArray;
    public CalificacionesAlumnosAdapter(Context context, int resource, JSONArray miJsonArray) {
        super(context, resource);
        this.miJsonArray=miJsonArray;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView= LayoutInflater.from(parent.getContext()).inflate(R.layout.calificaciones_item_alumnos,parent,false);
        final TextView nombre = (TextView) convertView.findViewById(R.id.txtNombreAlumno);
        final EditText calificacion = (EditText) convertView.findViewById(R.id.txtCalificacion);
        final DAOCalificaciones miDaoCalificaciones = new DAOCalificaciones(getContext());
            for (int i =0; i<miJsonArray.length();i++) {

                try {
                    nombre.setText(miJsonArray.getJSONObject(position).getString("nombre"));
                    try {
                        calificacion.setText(miJsonArray.getJSONObject(position).getString("calificacion"));
                    }catch (Exception e)
                    {}
                    calificacion.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            int numero = 0;
                            double decimal = 0.0;
                            try {
                                numero = Integer.parseInt(charSequence.toString());
                            } catch (Exception e) {

                            }
                            try {
                                decimal = Double.parseDouble(charSequence.toString());
                            } catch (Exception e) {
                            }
                            if (numero > 10 || decimal > 10) {
                                calificacion.setTextKeepState(10 + "");
                            }
                            if ((decimal == 10.0) & charSequence.length() > 4) {
                                calificacion.setTextKeepState(10.0 + "");
                            } else {
                                if ((decimal < 10) & charSequence.length() > 3) {
                                    calificacion.setTextKeepState(charSequence.subSequence(0, 3));
                                }
                            }
                            try {
                                miJsonArray.getJSONObject(position).put("calificacion", calificacion.getText());
                                miDaoCalificaciones.putCalificacion(miJsonArray.getJSONObject(position).getString("idIntegrantes"), calificacion.getText().toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable editable) {

                        }
                    });




                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

        return convertView;
    }

    @Override
    public int getCount() {
        return miJsonArray.length();
    }

}
