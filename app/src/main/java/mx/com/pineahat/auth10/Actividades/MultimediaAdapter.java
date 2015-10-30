package mx.com.pineahat.auth10.Actividades;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;

import mx.com.pineahat.auth10.DAO.DAOArchivos;
import mx.com.pineahat.auth10.R;

/**
 * Created by Ignacio on 28/10/2015.
 */
public class MultimediaAdapter extends BaseAdapter {

    public JSONArray miJsonArray;
    public Actividades origen;
    public MultimediaAdapter(Context context, int resource,JSONArray miJsonArray,Actividades origen) {
        this.miJsonArray=miJsonArray;
        this.origen=origen;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        try
        {
            String tipo = miJsonArray.getJSONObject(position).getString("tipo");
            switch (tipo)
            {
                case "Archivo":
                    convertView= LayoutInflater.from(parent.getContext()).inflate(R.layout.archivoitem,parent,false);
                    final ImageView cerrarA = (ImageView)convertView.findViewById(R.id.imgCerrar);
                    final View finalConvertView = convertView;
                    cerrarA.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            try {
                                ManejoMultimedia.eliminarArchivo(miJsonArray.getJSONObject(position).getString("nombreArchivo"));
                                DAOArchivos daoArchivos = new DAOArchivos(view.getContext());
                                daoArchivos.eliminarArchivo(miJsonArray.getJSONObject(position).getString("idArchivo"));
                                //removerItem(position);
                                //notifyDataSetChanged();
                                origen.avisar();
                            } catch (Exception e) {
                            }

                        }
                    });
                    TextView txtArchivo = (TextView) finalConvertView.findViewById(R.id.txtArchivo);

                    try
                    {
                        final File file= new File(miJsonArray.getJSONObject(position).getString("nombreArchivo"));
                        txtArchivo.setText("Archivo adjunto"+"."+ManejoMultimedia.getExtension(file));
                        txtArchivo.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                try {
                                Intent newIntent = new Intent(Intent.ACTION_VIEW);
                                    String tipo = getTipoIntent(ManejoMultimedia.getExtension(file));
                                newIntent.setDataAndType(Uri.fromFile(ManejoMultimedia.getArchivo(miJsonArray.getJSONObject(position).getString("nombreArchivo"))), tipo);
                                newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    origen.startActivity(newIntent);
                                } catch (ActivityNotFoundException e) {
                                    Toast.makeText(origen, "No se puede abrir el archivo", Toast.LENGTH_LONG).show();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }catch (Exception e)
                    {

                    }
                    break;
                case "Imagen":
                    convertView= LayoutInflater.from(parent.getContext()).inflate(R.layout.imagenitem,parent,false);
                    ImageView cerrarI = (ImageView)convertView.findViewById(R.id.imgCerrar);
                    ImageView imagenPrincipal =(ImageView)convertView.findViewById(R.id.imagenPrincipal);
                    cerrarI.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            try {
                                ManejoMultimedia.eliminarArchivo(miJsonArray.getJSONObject(position).getString("nombreArchivo"));
                                DAOArchivos daoArchivos = new DAOArchivos(view.getContext());
                                daoArchivos.eliminarArchivo(miJsonArray.getJSONObject(position).getString("idArchivo"));
                               // removerItem(position);
                                //notifyDataSetChanged();
                                origen.avisar();
                            } catch (Exception e) {

                            }


                        }

                    });
                    try {
                        final File imagen = ManejoMultimedia.getImagen(miJsonArray.getJSONObject(position).getString("nombreArchivo"));
                        imagenPrincipal.setImageBitmap(BitmapFactory.decodeFile(imagen.getAbsolutePath()));
                        imagenPrincipal.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent();
                                intent.setAction(Intent.ACTION_VIEW);
                                intent.setDataAndType(Uri.parse("file://" + imagen.getAbsolutePath()), "image/*");
                                origen.startActivity(intent);
                            }
                        });
                    }catch (Exception e)
                    {

                    }
                    break;
            }
        }catch (Exception e)
        {

        }


        return convertView;
    }

    private JSONArray removerItem(int position) {
        JSONArray nueva = new JSONArray();
        for(int i=0;i<miJsonArray.length();i++)
        {
            if(i!=position)
                try {
                    nueva.put(miJsonArray.get(i));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
        }
        return nueva;

    }

    @Override
    public int getCount() {
        return miJsonArray.length()+1;
    }

    @Override
    public Object getItem(int i) {
        try {
            return miJsonArray.getJSONObject(i);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    private String getTipoIntent(String extension)
    {
         MimeTypeMap myMime = MimeTypeMap.getSingleton();
        String tipo = myMime.getMimeTypeFromExtension(extension);
        return  tipo;
    }


}
