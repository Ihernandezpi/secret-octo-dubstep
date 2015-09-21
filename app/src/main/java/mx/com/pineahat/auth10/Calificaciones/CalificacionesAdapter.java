package mx.com.pineahat.auth10.Calificaciones;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import org.json.JSONArray;

import java.util.ArrayList;

import mx.com.pineahat.auth10.Equipos.Integrantes;
import mx.com.pineahat.auth10.R;

/**
 * Created by Ignacio on 18/09/2015.
 */
public class CalificacionesAdapter extends RecyclerView.Adapter<CalificacionesAdapter.ViewHolder> {
    private JSONArray mDataset;
    private ViewGroup parent;
    private boolean flag=false;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View miView;
        public ViewHolder(View v) {
            super(v);
            miView = v;
        }
    }

    public CalificacionesAdapter(JSONArray myDataset) { this.mDataset = myDataset; }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.parent=parent;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.calificaciones_item,parent,false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CardView miCardView = (CardView) holder.miView.findViewById(R.id.card_view);
        ImageButton miImageButton = (ImageButton) holder.miView.findViewById(R.id.imageButtonExpand);
        miCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        //Generar lista de alumnos


    }

    @Override
    public int getItemCount() {
        return mDataset.length();
    }


}
