package mx.com.pineahat.auth10.Calificaciones;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;

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
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView= LayoutInflater.from(parent.getContext()).inflate(R.layout.calificaciones_item_alumnos,parent,false);
        TextView nombre = (TextView) convertView.findViewById(R.id.txtNombreAlumno);
        TextView calificacion = (TextView) convertView.findViewById(R.id.txtNombreAlumno);
        try {
            nombre.setText(miJsonArray.getJSONObject(position).getString("nombre"));
            calificacion.setText(miJsonArray.getJSONObject(position).getString("calificacion"));
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return convertView;
    }

    @Override
    public int getCount() {
        return miJsonArray.length();
    }
}
