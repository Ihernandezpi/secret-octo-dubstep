package mx.com.pineahat.auth10;

import android.graphics.Color;
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


public class AdapterCardView extends RecyclerView.Adapter<AdapterCardView.ViewHolder> {
    private JSONArray mDataset;

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
    public AdapterCardView(JSONArray myDataset) {
        this.mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public AdapterCardView.ViewHolder onCreateViewHolder(ViewGroup parent,int viewType) {
        // create a new view
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
                try
                {
                    JSONObject miJson =mDataset.getJSONObject(position);
                    Toast.makeText(v.getContext(), "Inflar activity "+ miJson.getString("nombre"), Toast.LENGTH_SHORT).show();
                }
                catch (Exception e)
                {
                }
            }
        });

        try
        {
             //miCardView.setBackgroundColor(Color.parseColor(mDataset.getJSONObject(position).getString("color")));
            ((TextView) holder.mTextView.findViewById(R.id.infoText)).setText(mDataset.getJSONObject(position).getString("nombre"));
            ((TextView) holder.mTextView.findViewById(R.id.descripcion)).setText(mDataset.getJSONObject(position).getString("descripcion"));
            ((TextView) holder.mTextView.findViewById(R.id.txtFecha)).setText(mDataset.getJSONObject(position).getString("fechaCreacion"));
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
