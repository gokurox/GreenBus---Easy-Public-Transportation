package com.example.gursimransingh.greenbus_evs_iiitd.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.gursimransingh.greenbus_evs_iiitd.R;
import com.example.gursimransingh.greenbus_evs_iiitd.fragments.BusSafety;
import com.example.gursimransingh.greenbus_evs_iiitd.fragments.DiscoverRoute_Fragment;
import com.example.gursimransingh.greenbus_evs_iiitd.fragments.ShareExperience_Fragment;
import com.example.gursimransingh.greenbus_evs_iiitd.fragments.TravelPlan_Fragment;
import com.example.gursimransingh.greenbus_evs_iiitd.sqlite.DatabaseHelper;
import com.example.gursimransingh.greenbus_evs_iiitd.sqlite.dataholder.UserInfo;

public class NavigatorActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigator);

        Toolbar toolbar = (Toolbar) findViewById (R.id.toolbar);
        setSupportActionBar (toolbar);

        //Set the fragment initially
        TravelPlan_Fragment fragment = new TravelPlan_Fragment();
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigator, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_sos) {
            DatabaseHelper db = DatabaseHelper.getInstance(getApplicationContext());
            UserInfo ui = db.ULI_getOnlineUser(getApplicationContext());

            try {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage("9654157119", null, "SOS Message sent from GreenBus by User " + ui.email, null, null);
//                Toast.makeText(getApplicationContext(), "SMS sent.", Toast.LENGTH_LONG).show();
            }

            catch (Exception e) {
//                Toast.makeText(getApplicationContext(), "SMS faild, please try again.", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }

            Toast.makeText (NavigatorActivity.this, "SOS MESSAGE HAS BEEN SENT !!", Toast.LENGTH_LONG).show();
            return true;
        }
        else if (id == R.id.menu_logout) {
            DatabaseHelper db = DatabaseHelper.getInstance(this);
            db.ULI_setAllToOffline();

            Intent switchToLoginActivity = new Intent(this, LoginActivity.class);
            startActivity(switchToLoginActivity);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        android.support.v4.app.Fragment fragment = null;
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        if (id == R.id.nav_plantravel)
        {
            fragment = new TravelPlan_Fragment();
        }
        else if (id == R.id.nav_discoverRoute)
        {
            fragment = new DiscoverRoute_Fragment();
        }
        else if (id == R.id.nav_shareExperience)
        {
            fragment = new ShareExperience_Fragment();
        }
        else if (id == R.id.nav_busSafety)
        {
            fragment = new BusSafety();
        }

        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
