package mx.com.pineahat.auth10.Splash;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import mx.com.pineahat.auth10.R;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        AccountManager miAccountManager = AccountManager.get(getBaseContext());
        Account[] arAccounts = miAccountManager.getAccountsByType("mx.com.pineahat.auth10");
        if(arAccounts.length>=1) {
            miAccountManager.removeAccount(arAccounts[0],null,null);
        }

    }

}
