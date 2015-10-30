package mx.com.pineahat.auth10.Actividades;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Ignacio on 29/10/2015.
 */
public class ManejoMultimedia {
    static final private String rutaImagenes = "/storage/emulated/0/Pictures/APA/";
    static final private String rutaArchivos = "/storage/emulated/0/Documents/APA/";

    public static void crearRutas()
    {
        File imagenes=  new File(rutaImagenes);
        File Archivos=  new File(rutaArchivos);
        if(!imagenes.exists())
        {
            imagenes.mkdir();
        }
        if(!Archivos.exists())
        {
            Archivos.mkdir();
        }
    }

    public static String insertar(File miFile,String nombre)
    {
        crearRutas();
        String extension = getExtension(miFile);
        String NombreCompleto=nombre+"."+extension;
        try {
            FileOutputStream out = new FileOutputStream(rutaArchivos+NombreCompleto);
            InputStream in = new FileInputStream(miFile);
            byte[] buffer = new byte[1024];
            int readBytes = 0;

            while ((readBytes = in.read(buffer)) != -1)
                out.write(buffer, 0, readBytes);

            in.close();
            out.close();
        } catch (IOException e) {
        }
        return NombreCompleto;

    }
    public static void eliminarArchivo(String nombreArchivo)
    {
        File miArchivo= new File(rutaArchivos+nombreArchivo);
        miArchivo.delete();
    }

    public static File getArchivo(String nombre)
    {
        File mifile=null;
        mifile= new File(rutaArchivos+nombre);
        return mifile;
    }
    public static File getImagen(String nombre)
    {
        File mifile=null;
        mifile= new File(rutaImagenes+nombre);
        return mifile;
    }


    public static String getExtension(File miFile)
    {
        String extension="";
        String temp="";
        for(int i = miFile.getName().length();i>=0;i--)
        {
            if(!(miFile.getName().charAt(i-1)+"").equals(".")) {
                temp += miFile.getName().charAt(i-1);
            }
            else
            {
                i=-1;
            }
        }
        for(int i = temp.length();i>0;i--)
        {
            extension+=temp.charAt(i-1);
        }

        return extension;
    }


}
