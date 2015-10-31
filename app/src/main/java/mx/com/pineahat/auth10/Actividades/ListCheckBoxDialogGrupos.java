package mx.com.pineahat.auth10.Actividades;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import mx.com.pineahat.auth10.DAO.DAOActividades;
import mx.com.pineahat.auth10.DAO.DAOEquipos;
import mx.com.pineahat.auth10.R;

/**
 * Created by Stephani on 27/10/2015.
 */
public class ListCheckBoxDialogGrupos extends DialogFragment{
    private DAOActividades daoActividades;
    public  Actividades actividades;
    JSONArray JSONArrayGrupos;
    Actividad actividad;
    private String idProfesor;


    public static ListCheckBoxDialogGrupos newInstance(Actividades actividades)
    {
        ListCheckBoxDialogGrupos miListCheckBoxDialogGrupos= new ListCheckBoxDialogGrupos();
        miListCheckBoxDialogGrupos.actividades=actividades;
        return miListCheckBoxDialogGrupos;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        return createMultipleListDialog();
    }

    public AlertDialog createMultipleListDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        daoActividades = new DAOActividades(getActivity().getApplicationContext());

        try
        {
            JSONArrayGrupos= new JSONArray(super.getArguments().getString("JSONArrayGrupos"));
            actividad= new Actividad();
        }
        catch (Exception e)
        {

        }
        idProfesor=super.getArguments().getString("idProfesor");

        CharSequence[] items = new CharSequence[JSONArrayGrupos.length()];
        boolean[]itemsChecked= new boolean[JSONArrayGrupos.length()];

        for (int i = 0; i < JSONArrayGrupos.length(); i++) {
            try {
                JSONObject jsonObject = JSONArrayGrupos.getJSONObject(i);

                items[i]=jsonObject.get("grado").toString()+" "+jsonObject.get("grupo").toString() +" "+ jsonObject.get("materia").toString();
              //  items[i]=jsonObject.get("grupo").toString();
                //items[i]=jsonObject.get("materia").toString();
                //itemsChecked[i]=false;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        final ArrayList itemsSeleccionados = new ArrayList();

        builder.setTitle(getString(R.string.Grupos_al_que_desea_copiar))
                .setMultiChoiceItems(items, itemsChecked, new DialogInterface.OnMultiChoiceClickListener() {
                    @TargetApi(Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        if (isChecked) {
                            // Guardar indice seleccionado
                            itemsSeleccionados.add(which);
                            // Toast.makeText(getActivity(),"Checks seleccionados:(" + itemsSeleccionados.size() + ")",Toast.LENGTH_SHORT).show();
                            onChecked(which, true);

                        } else {
                            // Remover indice sin seleccion
                            itemsSeleccionados.remove(Integer.valueOf(which));
                            onChecked(which, false);
                        }
                    }
                })
                .setPositiveButton(getString(R.string.aceptar), new DialogInterface.OnClickListener(
                ) {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       // Bundle miBundle= new Bundle();
                       // miBundle.putString("grupos", JSONArrayGrupos.toString());
                       // ListCheckBoxDialogGrupos.super.setArguments(miBundle);
                        ListCheckBoxDialogGrupos.super.getArguments().putString("grupos",JSONArrayGrupos.toString());
                        actividades.copias();
                        //actividades
                      // Toast.makeText(getActivity(),"Aceptar",Toast.LENGTH_SHORT).show(); //actividades.avisar();
                    }
                })
                .setNegativeButton(getString(R.string.cancelar), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getActivity(),"Cancelar",Toast.LENGTH_SHORT).show();

                    }
                });

        return builder.create();
    }
    void onChecked(int which,boolean check)
    {
        try
        {
            JSONObject jsonObject = JSONArrayGrupos.getJSONObject(which);
            String idAsignacion =jsonObject.getString("idAsignacion").toString();
            String grado=jsonObject.getString("grado").toString();
            String grupo =jsonObject.getString("grupo").toString();
            String materia=jsonObject.getString("materia").toString();

                jsonObject.put("idAsignacion",idAsignacion);
                jsonObject.put("grado",grado);
                jsonObject.put("grupo",grupo);
                jsonObject.put("materia",materia);
            if(check==true)
                jsonObject.put("checked",true);
            else
                jsonObject.put("checked",false);
            JSONArrayGrupos.put(which,jsonObject);
            //Toast.makeText(getActivity(), jsonObject.getString("idAsignacion"),Toast.LENGTH_SHORT).show();

       }
        catch (Exception e)
        {

        }
    }

}
