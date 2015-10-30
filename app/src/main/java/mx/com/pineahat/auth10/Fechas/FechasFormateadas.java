package mx.com.pineahat.auth10.Fechas;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Ignacio on 29/10/2015.
 */
public class FechasFormateadas {
    public static String getFecha()
    {
        Calendar calendar= Calendar.getInstance();
        Date rightNow = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strDate = sdf.format(rightNow.getTime());
        return strDate;
    }
}
