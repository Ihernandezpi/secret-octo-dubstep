package mx.com.pineahat.auth10;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.text.Layout;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

import mx.com.pineahat.auth10.ActividadEditable;
import mx.com.pineahat.auth10.DAO.DAOActividades;
import mx.com.pineahat.auth10.DAO.DAOEquipos;


/**
 * Created by Stephani on 20/09/2015.
 */
public class ListCheckboxDialog extends DialogFragment {

    Bundle miBundle;
    private DAOActividades miDaoActividades;
    private  DAOEquipos midaoEquipos;
    public ActividadEditable miActividadEditable;
    JSONArray jsonArrayEquipos;
    private String key;
    String idAsignacion;


    public static ListCheckboxDialog newInstance(ActividadEditable actividadEditable)
    {
        ListCheckboxDialog miListCheckboxDialog= new ListCheckboxDialog();
        miListCheckboxDialog.miActividadEditable=actividadEditable;
        return miListCheckboxDialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        return createMultipleListDialog();
    }

    /**
     * Crea un diálogo con una lista de checkboxes
     * de selección multiple
     *
     * @return Diálogo
     */
    public AlertDialog createMultipleListDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        miDaoActividades = new DAOActividades(getActivity().getApplicationContext());
        midaoEquipos= new DAOEquipos(getActivity().getBaseContext());
        try
        {
            jsonArrayEquipos= new JSONArray(super.getArguments().getString("JSONArrayEquipos"));
        }
        catch (Exception e)
        {

        }
        key=super.getArguments().getString("key");
        idAsignacion=super.getArguments().getString("idAsignacion");
        CharSequence[] items = new CharSequence[jsonArrayEquipos.length()];
        boolean[]itemsChecked= new boolean[jsonArrayEquipos.length()];

        for (int i = 0; i < jsonArrayEquipos.length(); i++) {
            try {
                JSONObject jsonObject = jsonArrayEquipos.getJSONObject(i);

                items[i]=jsonObject.get("nombreEquipo").toString();
                itemsChecked[i]=Boolean.parseBoolean(jsonObject.get("checked").toString());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        final ArrayList itemsSeleccionados = new ArrayList();

        builder.setTitle("Equipos TI")
                .setMultiChoiceItems(items, itemsChecked, new DialogInterface.OnMultiChoiceClickListener() {
                    @TargetApi(Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        if (isChecked) {
                            // Guardar indice seleccionado
                            itemsSeleccionados.add(which);
                            Toast.makeText(
                                    getActivity(),
                                    "Checks seleccionados:(" + itemsSeleccionados.size() + ")",
                                    Toast.LENGTH_SHORT)
                                    .show();
                            onChecked(which,true);

                        } else {
                            // Remover indice sin selección
                            itemsSeleccionados.remove(Integer.valueOf(which));
                            onChecked(which,false);
                        }
                    }
                })
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener(
                ) {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (key == null) {
                            key = miDaoActividades.insertActividades(idAsignacion);
                            midaoEquipos.insertarEquiposActividadesTI(key,jsonArrayEquipos);
                        }
                        else
                        {
                            midaoEquipos.insertarEquiposActividadesTI(key,jsonArrayEquipos);
                        }
                        Toast.makeText(
                                getActivity(),
                                "Key "+ key,
                                Toast.LENGTH_SHORT)
                                .show();
                        ListCheckboxDialog.super.getArguments().putString("key", key);

                        miActividadEditable.avisar();

                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        return builder.create();
    }
    void onChecked(int which,boolean check)
    {
        try
        {
            JSONObject jsonObject = jsonArrayEquipos.getJSONObject(which);
            String idEquipo=jsonObject.get("idEquiposti").toString();
            String Equipo=jsonObject.get("nombreEquipo").toString();
            jsonObject.put("idEquiposti", idEquipo);
            jsonObject.put("nombreEquipo", Equipo);
            if (check==true)
                jsonObject.put("checked", true);
            else
                jsonObject.put("checked", false);
            jsonArrayEquipos.put(which,jsonObject);



        }
        catch (Exception e)
        {

        }
    }

}