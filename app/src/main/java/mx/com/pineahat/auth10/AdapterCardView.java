package mx.com.pineahat.auth10;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import mx.com.pineahat.auth10.Actividades.Actividades;
import mx.com.pineahat.auth10.DAO.DAOActividades;


public class AdapterCardView extends RecyclerView.Adapter<AdapterCardView.ViewHolder> {
    private JSONArray mDataset;
    private Vibrator vibrator;
    private ViewGroup parent;
    private MyFragment root;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View mTextView;
        public ViewHolder(View v) {
            super(v);
            mTextView = v;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public AdapterCardView(JSONArray myDataset, MyFragment root) {
        this.mDataset = myDataset;
        this.root=root;

    }

    // Create new views (invoked by the layout manager)
    @Override
    public AdapterCardView.ViewHolder onCreateViewHolder(ViewGroup parent,int viewType) {
        // create a new view
        this.parent = parent;
        vibrator=(Vibrator) parent.getContext().getSystemService(Context.VIBRATOR_SERVICE);
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_item_card_view, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(AdapterCardView.ViewHolder holder, final int position) {

        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        CardView miCardView = (CardView) holder.mTextView.findViewById(R.id.card_view);
        miCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    JSONObject miJson = mDataset.getJSONObject(position);
                    Intent intent = new Intent(v.getContext(), Actividades.class);
                   // Intent intent = new Intent(v.getContext(), ActividadEditable.class);
                    intent.putExtra("info", miJson.toString());
                    intent.putExtra("idAsignacion",miJson.getString("idAsignacion"));
                    v.getContext().startActivity(intent);
                } catch (Exception e) {
                }
            }
        });
        miCardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View v) {
                try {
                    vibrator.vibrate(1000);
                    final JSONObject miJson = mDataset.getJSONObject(position);
                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    final DAOActividades miDaoActividades = new DAOActividades(v.getContext());
                    builder.setTitle("Borrar actividad permanentemente?");
                    builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            miDaoActividades.eliminar(miJson);
                            root.refresh();
                            dialog.dismiss();
                        }
                    });
                    builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();

                } catch (Exception e) {
                }
                return true;
            }
        });

        try
        {

                ((LinearLayout)holder.mTextView.findViewById(R.id.linearColor)).setBackgroundColor(Color.parseColor(mDataset.getJSONObject(position).getString("color")));


            //((LinearLayout)holder.mTextView.findViewById(R.id.linearColor)).setBackgroundColor(Color.parseColor(mDataset.getJSONObject(position).getString("color")));
            ((TextView) holder.mTextView.findViewById(R.id.infoText)).setText(mDataset.getJSONObject(position).getString("nombre"));
            ((TextView) holder.mTextView.findViewById(R.id.descripcion)).setText(mDataset.getJSONObject(position).getString("descripcion"));
            String fecha = mDataset.getJSONObject(position).getString("fechaCreacion");
            DateFormat miDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = miDateFormat.parse(fecha);
            DateFormat miFormat = new SimpleDateFormat("cccdd LLL, hh:mm a");
            String fechaF =miFormat.format(date);
             ((TextView) holder.mTextView.findViewById(R.id.txtFecha)).setText(fechaF);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length();
    }
}
