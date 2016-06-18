package com.example.inderpreet.zeropressure;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {
Button regbtn,loginbtn;
    EditText email_ed,pswd_ed;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        SharedPreferences set = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
        //SharedPreferences set = this.getSharedPreferences("myPrefs",MODE_WORLD_READABLE);
        String email = set.getString("email",null);
        String pswd = set.getString("pswd",null);
        if(email!=null && pswd!=null)
        {

                    new AsyncLogin().execute(set.getString("email", "0"), set.getString("pswd", "0"));

        }

        email_ed=(EditText)findViewById(R.id.editText_email);
        pswd_ed=(EditText)findViewById(R.id.editText_pswd);
        loginbtn=(Button)findViewById(R.id.login_button);
        regbtn=(Button)findViewById(R.id.reg_btn);
        regbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(i);
            }
        });






        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(email_ed.getText()) || TextUtils.isEmpty(pswd_ed.getText()))
                {
                    Toast.makeText(LoginActivity.this,"Please fill out all the fields",Toast.LENGTH_SHORT).show();
                }
                else if(!(ValidateUserInfo.isEmailValid(email_ed.getText().toString()) && ValidateUserInfo.isPasswordValid(pswd_ed.getText().toString())))
                {
                    Toast.makeText(LoginActivity.this,"Invalid details",Toast.LENGTH_SHORT).show();

                }
                else
                {
                    new AsyncLogin().execute(email_ed.getText().toString(),pswd_ed.getText().toString());
                }

            }
        });
    }

    private class AsyncLogin extends AsyncTask<String,Void,String>
    {

        private ProgressDialog loading = new ProgressDialog(LoginActivity.this);
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            loading.setMessage("Please Wait....");
            loading.setIndeterminate(true);
            loading.setCancelable(false);
            loading.show();
        }

        @Override
        protected String doInBackground(String... params) {
            HashMap<String,String> prms = new HashMap<>();
            requestHandler rh = new requestHandler();
            prms.put("email", params[0]);
            prms.put("password",params[1]);
            String response = rh.sendPostRequest(config.URL_LOGIN,prms);
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            loading.dismiss();
            if(s.equals("check"))
            {
                Toast.makeText(LoginActivity.this,"Invalid email/password",Toast.LENGTH_SHORT).show();
            }
            else if(s.equals(""))
            {
                Toast.makeText(LoginActivity.this,"Unable to connect, Check your internet connection",Toast.LENGTH_SHORT).show();
            }
            else
            {
                SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("name", s);
                editor.commit();
                if(settings.getString("logout",null)!=null)
                {
                    editor.remove("logout");
                    editor.commit();
                }
                editor.putString("logout","n");
                editor.commit();
                if(settings.getString("email","0").isEmpty() || settings.getString("email","0").equals("0"))
                {
                    if(settings.getString("pswd","0").isEmpty() || settings.getString("pswd","0").equals("0"))
                    {
                        editor.putString("email", email_ed.getText().toString());
                        editor.commit();
                        editor.putString("pswd", pswd_ed.getText().toString());
                        editor.commit();
                    }

                }

                if(!settings.getString("firsttime","0").equals(("0")))
                {
                    startService(new Intent(LoginActivity.this,TimeService.class));
                }
                Intent i = new Intent(LoginActivity.this,Dashboard.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
        }
    }



}
