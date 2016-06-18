package com.example.inderpreet.zeropressure;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
EditText name,email,mobile,password;
    Button register;
    Boolean mauth=false,eauth= false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        name = (EditText)findViewById(R.id.edt_reg_name);
        email=(EditText)findViewById(R.id.editText_email);
        email.addTextChangedListener(new TextChangeWatcher(email));
        mobile=(EditText)findViewById(R.id.editText_reg_mobile);
        mobile.addTextChangedListener(new TextChangeWatcher(mobile));
        password=(EditText)findViewById(R.id.editText_reg_pswd);
        password.addTextChangedListener(new TextChangeWatcher(password));
        register=(Button)findViewById(R.id.reg_btn_reg);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(name.getText()) || TextUtils.isEmpty(email.getText()) || TextUtils.isEmpty(mobile.getText()) || TextUtils.isEmpty(password.getText()))
                {
                    Toast.makeText(RegisterActivity.this,"Please fill out all the fields",Toast.LENGTH_SHORT).show();
                }
                else
                if(!(ValidateUserInfo.isEmailValid(email.getText().toString()) && ValidateUserInfo.isMobileValid(mobile.getText().toString())))
                {
                    Toast.makeText(RegisterActivity.this,"Invalid details",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    new AsyncRegister().execute();
                }
            }
        });
    }

    private class AsyncRegister extends AsyncTask<Void,Void,String>
    {
       String n,m,e,p;

        private ProgressDialog loading = new ProgressDialog(RegisterActivity.this);
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            n=name.getText().toString();
            e=email.getText().toString();
            m=mobile.getText().toString();
            p=password.getText().toString();
            loading.setTitle("Registering");
            loading.setMessage("Please Wait....");
            loading.setIndeterminate(true);
            loading.setCancelable(false);
            loading.show();
        }

        @Override
        protected String doInBackground(Void... pams) {
            HashMap<String,String> params = new HashMap<>();
            params.put(config.KEY_NAME,n);
            params.put(config.KEY_EMAIL, e);
            params.put(config.KEY_MOBILE, m);
            params.put(config.KEY_PASSWORD,p);
            requestHandler rh = new requestHandler();
            String response = rh.sendPostRequest(config.URL_ADD,params);
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            loading.dismiss();
            if(s.equals("Row exists"))
            {
                Toast.makeText(RegisterActivity.this,"User already exists !",Toast.LENGTH_SHORT).show();
            }
            else if(s.equals(""))
            {
                AlertDialog.Builder alrt = new AlertDialog.Builder(RegisterActivity.this);
                alrt.setMessage("You have been succesfully registered.");
                alrt.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        Intent i = new Intent(RegisterActivity.this,LoginActivity.class);
                        email.setText(null);
                        mobile.setText(null);
                        name.setText(null);
                        password.setText(null);
                        email.setError(null);
                        startActivity(i);
                    }

                });
                alrt.show();
            }

        }
    }



    private class TextChangeWatcher implements TextWatcher {
        private View view;
        public TextChangeWatcher(View view)
        {
            this.view = view;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s)
        {
            mauth=true;
            eauth= true;
            switch (view.getId()) {

                case R.id.editText_email:
                    eauth = true;
                    if (!ValidateUserInfo.isEmailValid(email.getText().toString())) {
                        email.setError("Email Invalid");
                        eauth = false;
                    } else eauth = true;
                    break;
                case R.id.editText_reg_mobile:
                    mauth = true;
                    if (!ValidateUserInfo.isMobileValid(mobile.getText().toString())) {
                        mobile.setError("Mobile no should be 10 digits long");
                        mauth = false;
                    } else mauth = true;
                    break;

            }

        }
    }




}
