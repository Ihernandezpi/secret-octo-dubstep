package mx.com.pineahat.auth10.URL;

import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Ignacio on 30/10/2015.
 */
public class HttpConnectUploadFile {
    final private static String urllll="http://pineahat.com.mx/subir.php";
    private static int serverResponseCode = 0;

    public static int uploadFile(File miFile) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
        FileInputStream fis = new FileInputStream(miFile);
        //System.out.println(file.exists() + "!!");
        //InputStream in = resource.openStream();

        byte[] buf = new byte[1024];

            for (int readNum; (readNum = fis.read(buf)) != -1;) {
                bos.write(buf, 0, readNum); //no doubt here is 0
                //Writes len bytes from the specified byte array starting at offset off to this byte array output stream.
                System.out.println("read " + readNum + " bytes,");
            }
            byte[] bytes = bos.toByteArray();
        } catch (IOException ex) {
        }

        byte[] ba = bos.toByteArray();
        byte[] ba1 = Base64.encode(ba, Base64.DEFAULT);


       ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("base64",ba1.toString()));
        nameValuePairs.add(new BasicNameValuePair("ImageName", miFile.getName()));
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(urllll);
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);
            String st = EntityUtils.toString(response.getEntity());
            Log.v("log_tag", "In the try Loop" + st);

        } catch (Exception e) {
            Log.v("log_tag", "Error in http connection " + e.toString());
        }
        return 1;

    }
}


