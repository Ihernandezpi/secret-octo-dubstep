package mx.com.pineahat.auth10;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Handler;
import android.os.Parcelable;
import android.os.SystemClock;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jensdriller.libs.undobar.UndoBar;
import com.melnykov.fab.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import mx.com.pineahat.auth10.Actividades.Actividades;
import mx.com.pineahat.auth10.DAO.DAOCardViewItem;

/**
 * Created by Stephani on 05/07/2015.
 */
public class MyFragment extends Fragment{
    private boolean flag =true;
    public final static String ID_GRUPO = "key_text";
    public final static String TYPE="KEY_TYPE";

    private String mText;
    private String tipo;
    private RecyclerView miRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private AdapterCardView miAdapterCardView;
    private JSONArray mData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mText = getArguments().getString(ID_GRUPO);
        tipo = getArguments().getString(TYPE);
        View v = inflater.inflate(R.layout.fragment, container, false);
        TextView tv = (TextView) v.findViewById(R.id.tv_fragment);

            FloatingActionButton miFloatingActionButton = (FloatingActionButton) v.findViewById(R.id.fab);
        if(tipo.equals("Actividades")) {
            miFloatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), Actividades.class);
                    intent.putExtra("idAsignacion", mText);
                    startActivity(intent);
                   /* Intent intent = new Intent(v.getContext(), ActividadEditable.class);
                    intent.putExtra("idAsignacion", mText);
                    startActivity(intent);*/
                }
            });
        }
        else
        {
            miFloatingActionButton.setVisibility(View.INVISIBLE);
        }

        final View coordinatorLayoutView = v.findViewById(R.id.snackbarPosition);



        //tv.setText(mText);
        miRecyclerView= (RecyclerView) v.findViewById(R.id.my_recycler_view);
        miRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        miRecyclerView.setLayoutManager(mLayoutManager);

        //Carga del adapter
        final DAOCardViewItem miDaoCardViewItem= new DAOCardViewItem(getActivity().getApplicationContext(),mText);
        if(tipo.equals("Actividades"))
            mData = miDaoCardViewItem.getActividades();
        else if (tipo.equals("Papelera"))
            mData = miDaoCardViewItem.getPapelera();

        if(mData!=null) {
            miAdapterCardView = new AdapterCardView(mData,MyFragment.this);
            miRecyclerView.setAdapter(miAdapterCardView);
            SwipeableRecyclerViewTouchListener swipeTouchListener =
                    new SwipeableRecyclerViewTouchListener(miRecyclerView,
                            new SwipeableRecyclerViewTouchListener.SwipeListener() {
                                @Override
                                public boolean canSwipe(int position) {
                                    return true;
                                }

                                @Override
                                public void onDismissedBySwipeLeft(final RecyclerView miRecyclerView, int[] reverseSortedPositions) {
                                    for (final int position : reverseSortedPositions) {
                                        String id = null;
                                        try {
                                            id = mData.getJSONObject(position).getString("idActividades");
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        final String finalId = id;
                                        if (tipo.equals("Actividades")) {
                                            moverPapelera(coordinatorLayoutView,miDaoCardViewItem,finalId,position,getView());
                                        }
                                        else
                                        {
                                            moverActividades(coordinatorLayoutView,miDaoCardViewItem,finalId,position, getView());
                                        }
                                    }
                                }
                                @Override
                                public void onDismissedBySwipeRight(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                    for (final int position : reverseSortedPositions) {
                                        String id = null;
                                        try {
                                            id = mData.getJSONObject(position).getString("idActividades");
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        final String finalId = id;
                                        if (tipo.equals("Actividades")) {
                                            moverPapelera(coordinatorLayoutView,miDaoCardViewItem,finalId,position, getView());
                                        }
                                        else
                                        {
                                            moverActividades(coordinatorLayoutView,miDaoCardViewItem,finalId,position, getView());
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

    private void moverPapelera(View coordinatorLayoutView,final DAOCardViewItem miDaoCardViewItem,final String finalId, int position, View v)
    {
        Snackbar.make(coordinatorLayoutView, "Movido a papelera", Snackbar.LENGTH_LONG).setActionTextColor(getResources().getColor(R.color.ripple)).setAction("Regresar", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    miDaoCardViewItem.returnActividades(finalId);
                    mData = null;
                    mData = miDaoCardViewItem.getActividades();
                    miRecyclerView.setHasFixedSize(true);
                    mLayoutManager = new LinearLayoutManager(v.getContext());
                    miRecyclerView.setLayoutManager(mLayoutManager);
                    miAdapterCardView = null;
                    miAdapterCardView = new AdapterCardView(mData,MyFragment.this);
                    miRecyclerView.setAdapter(miAdapterCardView);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).show();

        try {
            miDaoCardViewItem.moveTrash(mData.getJSONObject(position).getString("idActividades"));
            ArrayList<String> list = new ArrayList<String>();
            JSONArray jsonArray = mData;
            int len = jsonArray.length();
            if (jsonArray != null) {
                for (int i = 0; i < len; i++) {
                    list.add(jsonArray.getString(i));
                }
            }
            list.remove(position);
            mData = new JSONArray();

            for (String json : list) {
                JSONObject miJsonObject = new JSONObject(json);
                mData.put(miJsonObject);
            }
            miRecyclerView.setHasFixedSize(true);
            mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
            miRecyclerView.setLayoutManager(mLayoutManager);
            miAdapterCardView = null;
            miAdapterCardView = new AdapterCardView(mData,MyFragment.this);
            miRecyclerView.setAdapter(miAdapterCardView);
        } catch (JSONException e) {

            e.printStackTrace();
        }
    }
    private void moverActividades(View coordinatorLayoutView,final DAOCardViewItem miDaoCardViewItem,final String finalId, final int position, View v)
    {
        Snackbar.make(coordinatorLayoutView, "Movido a Actividades", Snackbar.LENGTH_LONG).setActionTextColor(getResources().getColor(R.color.ripple)).setAction("Regresar", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    miDaoCardViewItem.moveTrash(finalId);
                    mData = null;
                    mData = miDaoCardViewItem.getPapelera();
                    miRecyclerView.setHasFixedSize(true);
                    mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
                    miRecyclerView.setLayoutManager(mLayoutManager);
                    miAdapterCardView = null;
                    miAdapterCardView = new AdapterCardView(mData,MyFragment.this);
                    miRecyclerView.setAdapter(miAdapterCardView);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).show();

        try {
            miDaoCardViewItem.returnActividades(finalId);
            ArrayList<String> list = new ArrayList<String>();
            JSONArray jsonArray = mData;
            int len = jsonArray.length();
            if (jsonArray != null) {
                for (int i = 0; i < len; i++) {
                    list.add(jsonArray.getString(i));
                }
            }
            list.remove(position);
            mData = new JSONArray();

            for (String json : list) {
                JSONObject miJsonObject = new JSONObject(json);
                mData.put(miJsonObject);
            }
            miRecyclerView.setHasFixedSize(true);
            mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
            miRecyclerView.setLayoutManager(mLayoutManager);
            miAdapterCardView = null;
            miAdapterCardView = new AdapterCardView(mData,this);
            miRecyclerView.setAdapter(miAdapterCardView);
        } catch (Exception e) {

            e.printStackTrace();
        }
    }
    public void refresh () {
        final DAOCardViewItem miDaoCardViewItem = new DAOCardViewItem(getActivity().getApplicationContext(), mText);
        if (tipo.equals("Actividades"))
            mData = miDaoCardViewItem.getActividades();
        else if (tipo.equals("Papelera"))
            mData = miDaoCardViewItem.getPapelera();

        if (mData != null) {
            miRecyclerView.setHasFixedSize(true);
            mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
            miRecyclerView.setLayoutManager(mLayoutManager);
            miAdapterCardView = null;
            miAdapterCardView = new AdapterCardView(mData,this);
            miRecyclerView.setAdapter(miAdapterCardView);
        }
    }

    @Override
    public void onResume() {
        callAsynchronousTask();
        super.onResume();
    }

    @Override
    public void onStart() {
        refresh();
        super.onStart();
    }
    public void callAsynchronousTask() {
        final Handler handler = new Handler();
        Timer timer = new Timer();
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            refresh();
                        } catch (Exception e)
                        {

                        }
                    }
                });
            }
        };
        timer.schedule(doAsynchronousTask, 0, 15000); //execute in every 50000 ms
    }

}
