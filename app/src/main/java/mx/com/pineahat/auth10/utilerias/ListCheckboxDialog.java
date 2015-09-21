package mx.com.pineahat.auth10.utilerias;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Stephani on 20/09/2015.
 */
public class ListCheckboxDialog extends DialogFragment {
    CharSequence[] items;
    CharSequence[] itemsValores ;

    public ListCheckboxDialog( CharSequence[] itemsr,CharSequence[] itemsValoresr) {

    }

    @NonNull
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

        final ArrayList itemsSeleccionados = new ArrayList();



        items[0] = "Equipo 1";
        itemsValores[0] = "1";
        items[1] = "Equipo 2";
        itemsValores[1] = "2";
        items[2] = "Equipo 3";
        itemsValores[2] = "3";

        builder.setTitle("Equipos de TI")
                .setMultiChoiceItems(items, null, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        if (isChecked) {
                            // Guardar indice seleccionado
                            itemsSeleccionados.add(which);
                         /*   Toast.makeText(
                                    getActivity(),
                                    "Checks seleccionados:(" + itemsSeleccionados.size() + ")",
                                    Toast.LENGTH_SHORT)
                                    .show();*/
                            Toast.makeText(
                                    getActivity(),
                                    "Valor " + itemsValores[which] + " :)",
                                    Toast.LENGTH_SHORT)
                                    .show();
                        } else if (itemsSeleccionados.contains(which)) {
                            // Remover indice sin selección
                            itemsSeleccionados.remove(Integer.valueOf(which));
                        }
                    }
                });

        return builder.create();
    }

}

