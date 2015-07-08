package mx.com.pineahat.auth10;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.melnykov.fab.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import mx.com.pineahat.auth10.DAO.DAOCardViewItem;

/**
 * Created by Stephani on 05/07/2015.
 */
public class MyFragment extends Fragment{
    public final static String ID_GRUPO = "key_text";

    private String mText;
    private RecyclerView miRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private AdapterCardView miAdapterCardView;
    private JSONArray mData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mText = getArguments().getString(ID_GRUPO);
        View v = inflater.inflate(R.layout.fragment, container, false);
        TextView tv = (TextView) v.findViewById(R.id.tv_fragment);
        FloatingActionButton miFloatingActionButton =(FloatingActionButton) v.findViewById(R.id.fab);
        miFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(v.getContext(),ActividadEditable.class);
                startActivity(intent);
            }
        });





        //tv.setText(mText);
        miRecyclerView= (RecyclerView) v.findViewById(R.id.my_recycler_view);
        miRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        miRecyclerView.setLayoutManager(mLayoutManager);

        //Carga del adapter
        final DAOCardViewItem miDaoCardViewItem= new DAOCardViewItem(getActivity().getApplicationContext(),mText);
        mData = miDaoCardViewItem.getActividades();
        if(mData!=null) {
            miAdapterCardView = new AdapterCardView(mData);
            miRecyclerView.setAdapter(miAdapterCardView);
            SwipeableRecyclerViewTouchListener swipeTouchListener =
                    new SwipeableRecyclerViewTouchListener(miRecyclerView,
                            new SwipeableRecyclerViewTouchListener.SwipeListener() {
                                @Override
                                public boolean canSwipe(int position) {
                                    return true;
                                }


                                @Override
                                public void onDismissedBySwipeLeft(RecyclerView miRecyclerView, int[] reverseSortedPositions) {
                                    for (int position : reverseSortedPositions) {
                                        try {
                                            ArrayList<String> list = new ArrayList<String>();
                                            JSONArray jsonArray = mData;
                                            int len = jsonArray.length();
                                            if (jsonArray != null) {
                                                for (int i=0;i<len;i++){
                                                    list.add(jsonArray.getString(i));
                                                }
                                            }
                                            list.remove(position);
                                            mData = new JSONArray();

                                            for(String json : list)
                                            {
                                                Log.i("****", "*************" + json);
                                                JSONObject miJsonObject= new JSONObject(json);
                                                mData.put(miJsonObject);
                                            }
                                            Log.i("****", "*************" + mData.toString());
                                            miRecyclerView.setHasFixedSize(true);
                                            mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
                                            miRecyclerView.setLayoutManager(mLayoutManager);
                                            miAdapterCardView=null;
                                            miAdapterCardView=new AdapterCardView(mData);
                                            miRecyclerView.setAdapter(miAdapterCardView);




                                        } catch (JSONException e) {

                                            e.printStackTrace();
                                        }

                                    }
                                }


                                @Override
                                public void onDismissedBySwipeRight(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                    for (int position : reverseSortedPositions) {
                                        try {
                                            ArrayList<String> list = new ArrayList<String>();
                                            JSONArray jsonArray = mData;
                                            int len = jsonArray.length();
                                            if (jsonArray != null) {
                                                for (int i=0;i<len;i++){
                                                    list.add(jsonArray.getString(i));
                                                }
                                            }
                                            list.remove(position);
                                            mData = new JSONArray();

                                            for(String json : list)
                                            {
                                                Log.i("****", "*************" + json);
                                                JSONObject miJsonObject= new JSONObject(json);
                                                mData.put(miJsonObject);
                                            }
                                            Log.i("****", "*************" + mData.toString());
                                            miRecyclerView.setHasFixedSize(true);
                                            mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
                                            miRecyclerView.setLayoutManager(mLayoutManager);
                                            miAdapterCardView=null;
                                            miAdapterCardView=new AdapterCardView(mData);
                                            miRecyclerView.setAdapter(miAdapterCardView);
                                        } catch (JSONException e) {

                                            e.printStackTrace();
                                        }

                                    }
                                }
                            });

            miRecyclerView.addOnItemTouchListener(swipeTouchListener);




        }
        else
        {
            tv.setText("Sin actividades");
        }





        return v;
    }

}
