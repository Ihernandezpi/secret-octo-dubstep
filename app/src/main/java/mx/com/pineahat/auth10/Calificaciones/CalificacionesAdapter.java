package mx.com.pineahat.auth10.Calificaciones;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.AbsListView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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



    public static class ViewHolder extends RecyclerView.ViewHolder {
        public int numeroElementos;
        public View miView;
        public boolean flag=false;
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
    public void onBindViewHolder(final ViewHolder holder, int position) {


        /**
         * Inicialización Inicio
         */
        CardView miCardView = (CardView) holder.miView.findViewById(R.id.card_view);
        final ImageButton miImageButton = (ImageButton) holder.miView.findViewById(R.id.imageButtonExpand);
        TextView nombreEquipo =(TextView) holder.miView.findViewById(R.id.txtNombreEquipo);

        //Partes Ocultas
        final View layoutOculto =(View) holder.miView.findViewById(R.id.view_hidden);
        ListView listaAlumnos =(ListView) holder.miView.findViewById(R.id.listAlumnosCalificacion);

        /**
         * Inicialización Fin
         */
        try {
            nombreEquipo.setText(mDataset.getJSONObject(position).getString("nombre"));
            JSONArray lista = mDataset.getJSONObject(position).getJSONArray("alumnos");
            CalificacionesAlumnosAdapter miCalificacionesAlumnosAdapter = new CalificacionesAlumnosAdapter(holder.miView.getContext(),0,lista);
            listaAlumnos.setAdapter(miCalificacionesAlumnosAdapter);
            holder.numeroElementos=lista.length()*60;

        }catch (Exception e)

        {
        }

        miCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!holder.flag) {
                    holder.flag = true;
                    miImageButton.setImageResource(R.mipmap.ic_expand_less_black);
                    expand(layoutOculto, holder.numeroElementos);

                }else
                {
                    holder.flag = false;
                    miImageButton.setImageResource(R.mipmap.ic_expand_more_black);
                    collapse(layoutOculto);

                }

            }
        });
    }

    @Override
    public int getItemCount() {
        if (mDataset==null)
            return 0;
        return mDataset.length();
    }


    public static void expand(final View v, final int height) {
        v.measure(height, RelativeLayout.LayoutParams.MATCH_PARENT);
        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1 ? height : (int)(targetHeight * interpolatedTime);
                v.requestLayout();

            }
            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };
        // 1dp/ms
        a.setDuration((int) (targetHeight / v.getContext().getResources().getDisplayMetrics().density));

        v.startAnimation(a);

    }

    public static void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();
        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if(interpolatedTime == 1)
                    v.setVisibility(View.GONE);
                else{
                    v.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);
                    //v.requestLayout();
                }
            }
            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };
        // 1dp/ms
        a.setDuration((int)(initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }
}
