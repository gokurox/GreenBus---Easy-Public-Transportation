package com.example.gursimransingh.greenbus_evs_iiitd.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.gursimransingh.greenbus_evs_iiitd.R;
import com.example.gursimransingh.greenbus_evs_iiitd.sqlite.DatabaseHelper;
import com.example.gursimransingh.greenbus_evs_iiitd.sqlite.dataholder.UserInfo;


public class SplashScreen extends AppCompatActivity {
    private final int SPLASH_TIMEOUT = 1500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        DatabaseHelper db = DatabaseHelper.getInstance (this);
        boolean alreadyLoggedIn = false;
        UserInfo userInfo = db.ULI_getOnlineUser(this);

        db.getData_Bus_Coordinates();
        db.getData_Feedback();

        if (userInfo == null)
        {
            // Goto Login Screen
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent switchToLoginActivity = new Intent (SplashScreen.this, LoginActivity.class);
                    startActivity (switchToLoginActivity);
                    finish();
                }
            }, SPLASH_TIMEOUT);
        }
        else
        {
            Toast.makeText (this, "User is already Logged In: " + userInfo.name, Toast.LENGTH_SHORT);

            // Goto Navigator Activity
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent switchToNavigatorActivity = new Intent (SplashScreen.this, NavigatorActivity.class);
                    startActivity (switchToNavigatorActivity);
                    finish();
                }
            }, SPLASH_TIMEOUT);
        }
    }
}
