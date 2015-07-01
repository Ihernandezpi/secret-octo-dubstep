package mx.com.pineahat.auth10.utilerias;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Conexion extends SQLiteOpenHelper {
	
	private Context contexto;
	private static String NombreBD="uth.db";
	
	public Conexion(Context context) {
		super(context, NombreBD, null, 1);
		contexto=context;
	}

	private void copiarBD ()
	{
		if (!new File("/data/data/" +contexto.getPackageName() + "/"+NombreBD).exists()) {
	        try {
	            FileOutputStream out = new FileOutputStream("data/data/"+ contexto.getPackageName() + "/"+NombreBD);
	            InputStream in = contexto.getAssets().open(NombreBD);
	            byte[] buffer = new byte[1024];
	            int readBytes = 0;

	            while ((readBytes = in.read(buffer)) != -1)
	                out.write(buffer, 0, readBytes);

	            in.close();
	            out.close();
	        } catch (IOException e) {
	        }
	    }
		
	}
	
	public SQLiteDatabase getBD()
	{
		copiarBD();
		SQLiteDatabase miDatabase = SQLiteDatabase.openOrCreateDatabase("/data/data/" + contexto.getPackageName() + "/" + NombreBD, null);
		return miDatabase;	
		
	}
	private void borrarBd()
	{
		File miFile=new File("/data/data/" +contexto.getPackageName() + "/"+NombreBD);
		if (miFile.exists())
		{
			miFile.delete();
		}
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}

	
	
}