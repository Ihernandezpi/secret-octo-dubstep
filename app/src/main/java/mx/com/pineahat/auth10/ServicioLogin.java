package mx.com.pineahat.auth10;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class ServicioLogin extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        Authenticator miAuthenticator = new Authenticator(this);
        return miAuthenticator.getIBinder();
    }
}
