package com.pafex.zscs;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class LoginActivity extends AppCompatActivity {
    EditText emaillogin,passwordlogin;
    Button loginbtn,regbtn;
    Connection connect;
    boolean isValid;
    public static final String fileName = "login" ;
    public static final String Email = "emailId";
    public static final String Password = "password";
    SharedPreferences sharedpreferences;
    String email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences prefs = getSharedPreferences(fileName, MODE_PRIVATE);
        boolean isLoggedIn= prefs.getBoolean("isLoggedIn", false);

        if(isLoggedIn){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    finish();
            return;
        }
        setContentView(R.layout.activity_login);

        emaillogin = (EditText)findViewById(R.id.emaillogin);
        passwordlogin = (EditText)findViewById(R.id.passwordlogin);
        loginbtn = (Button)findViewById(R.id.loginbtn);
        regbtn = (Button)findViewById(R.id.regbtn);

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isValid = SetValidation();
                if(isValid)
                    new LoginActivity.checkLogin().execute("");

                SharedPreferences.Editor editor = getSharedPreferences(fileName, MODE_PRIVATE).edit();
                editor.putString(Email, emaillogin.getText().toString());
                editor.putString(Password, passwordlogin.getText().toString());
                editor.putBoolean("isLoggedIn", true);
//any other detail you want to save
                editor.apply();
            }
        });

        regbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    public boolean SetValidation() {

        // Check for a valid email address.
        if (emaillogin.getText().toString().isEmpty() | !Patterns.EMAIL_ADDRESS.matcher(emaillogin.getText().toString()).matches()) {
            emaillogin.setError(getResources().getString(R.string.email_error));
            return false;
        }

        // Check for a valid password.
        if (passwordlogin.getText().toString().isEmpty() | passwordlogin.getText().length() < 6) {
            passwordlogin.setError(getResources().getString(R.string.password_error));
            return false;
        }
            return true;
    }

    public class checkLogin extends AsyncTask<String, String, String> {

        String z = null;
        Boolean isSuccess = false;


        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(String s) {

        }

        @Override
        protected String doInBackground(String... strings) {

            ConnectionHelper connectionHelper = new ConnectionHelper();
            connect = connectionHelper.conclass();
            if(connect == null){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(LoginActivity.this,"Check Internet Connection",Toast.LENGTH_LONG).show();
                    }
                });
                z = "On Internet Connection";
            }
            else {
                try {
                    String sql = "SELECT * FROM Users WHERE EmailId = '" + emaillogin.getText() + "' AND UserPassword = '" + passwordlogin.getText() + "' ";
                    Statement stmt = connect.createStatement();
                    ResultSet rs = stmt.executeQuery(sql);

                    if (rs.next()) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(LoginActivity.this, "Login Success", Toast.LENGTH_LONG).show();
                            }
                        });
                        z = "Success";

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("email_login",emaillogin.getText().toString());
                        startActivity(intent);
                        finish();

                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(LoginActivity.this, "Check email or password", Toast.LENGTH_LONG).show();
                            }
                        });

                        emaillogin.setText("");
                        passwordlogin.setText("");
                    }
                } catch (Exception e) {
                    isSuccess = false;
                    Log.e("SQL Error : ", e.getMessage());
                }
            }
            return z;
        }
    }

/*
    @Override
    protected void onStart() {
        super.onStart();
        if (email != null && password != null) {
            Intent i = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(i);
        }
    }
*/

}
