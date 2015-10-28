package mx.com.pineahat.auth10.Actividades;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import mx.com.pineahat.auth10.DAO.DAOActividades;
import mx.com.pineahat.auth10.DAO.DAOEquipos;
import mx.com.pineahat.auth10.Equipo;

/**
 * Created by Stephani on 27/10/2015.
 */
public class ListAdapterGrupos extends ArrayAdapter<Grupos>
{
    ArrayList<Grupos> grupos;
    private TextView textViewGrupos;
    DAOActividades daoActividades;
    public ListAdapterGrupos(Context context,int textViewResourceId, ArrayList<Grupos> objects) {
        super(context,  textViewResourceId, objects);
        this.grupos=objects;
        daoActividades= new DAOActividades(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view= null;
      //  LayoutInflater inflater=getLayoutInflater();
        return view;


    }
}
