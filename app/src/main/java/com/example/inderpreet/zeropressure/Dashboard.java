package com.example.inderpreet.zeropressure;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class Dashboard extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
ImageView posimg;
    TextView posed,name;
    public static final String PREFS_NAME = "MyPrefsFile";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        posimg=(ImageView)findViewById(R.id.pos_imgvw);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        name = (TextView)findViewById(R.id.name_edt);
        posed = (TextView)findViewById(R.id.pos_edt);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                new AsyncPosition().execute();
            }
        });

        SharedPreferences settings = getSharedPreferences(PREFS_NAME,0);
        boolean firstStart = settings.getBoolean("firstStart",true);
        if(firstStart)
        {
            new AsyncPosition().execute();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);
    }

    private class AsyncPosition extends AsyncTask<Void,Void,String>
    {
        private ProgressDialog loading = new ProgressDialog(Dashboard.this);
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading.setMessage("Please Wait....");
            loading.setIndeterminate(true);
            loading.setCancelable(false);
            loading.show();
        }

        @Override
        protected String doInBackground(Void... params)
        {
            requestHandler rh = new requestHandler();
            String response = rh.sendGetRequest(config.URL_GET_POS);
            String x = response;
            String pos = x.substring(0,x.indexOf("-"));
            String id = x.substring((x.indexOf("-")+1),(x.length()-1));
            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(Dashboard.this);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("position", pos);
            editor.commit();
            editor.putString("id",id);
            editor.commit();
            editor.putString("firsttime","yes");
            editor.commit();
            if((!settings.getString("firsttime","0").equals("0")) && (settings.getString("stat","0").equals("0")))
            {
                startService(new Intent(Dashboard.this,TimeService.class));
                editor.putString("stat","1");
                editor.commit();

            }


            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            loading.dismiss();
            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(Dashboard.this);
            String pos = settings.getString("position","0");
            String uri = "@drawable/"+pos.toLowerCase();
            int imageRes = getResources().getIdentifier(uri,null,getPackageName());
            Drawable res = getResources().getDrawable(imageRes);
            name.setText("Hello "+settings.getString("name","0"));
            posimg.setImageDrawable(res);
            posed.setText("Position : On the "+pos.toUpperCase());

        }
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
       // getMenuInflater().inflate(R.menu.dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_learn_more)
        {
            Intent i = new Intent(Dashboard.this,LoggedActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_about) {
            Intent i = new Intent(Dashboard.this,AboutUs.class);
            startActivity(i);

        } else if (id == R.id.nav_reach) {
            Intent i = new Intent(Dashboard.this,ReachUs.class);
            startActivity(i);

        } else if (id == R.id.nav_logout)
        {
            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(Dashboard.this);
            SharedPreferences.Editor editor = settings.edit();
            editor.clear();
            editor.commit();
            Intent i = new Intent(Dashboard.this,LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
