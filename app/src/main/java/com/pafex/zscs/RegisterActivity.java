package com.pafex.zscs;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RegisterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText company_name,name,mobile_no,email,password;
    Button registerbtn;
    Connection connect;
    boolean isValid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        company_name = (EditText) findViewById(R.id.company_name);
        name = (EditText) findViewById(R.id.name);
        mobile_no = (EditText) findViewById(R.id.mobile_no);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        registerbtn = (Button) findViewById(R.id.registerbtn);

        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                isValid = SetValidation();
                if(isValid){
                    new RegisterActivity.registeruser().execute("");
                }

            }
        });
    }


    public boolean SetValidation() {

        // Check for a valid company name
        if (company_name.getText().toString().isEmpty()) {
            company_name.setError(getResources().getString(R.string.name_error));
            return false;
        }

        // Check for a valid name
        if (name.getText().toString().isEmpty()) {
            name.setError(getResources().getString(R.string.name_error));
            return false;
        }

        // Check for a valid mobile number
        if (mobile_no.getText().toString().isEmpty() | !Patterns.PHONE.matcher(mobile_no.getText().toString()).matches()) {
            mobile_no.setError(getResources().getString(R.string.mobile_error));
            return false;
        }

        // Check for a valid email address.
        if (email.getText().toString().isEmpty() | !Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
            email.setError(getResources().getString(R.string.email_error));
            return false;
        }

        // Check for a valid password.
        if (password.getText().toString().isEmpty() | password.getText().length() < 6) {
            password.setError(getResources().getString(R.string.password_error));
            return false;
        }

            return true;


    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    public class registeruser extends AsyncTask<String,String,String> {

        String z = null;
        Boolean isSuccess = false;

        @Override
        protected String doInBackground(String... strings) {

            try {
                ConnectionHelper connectionHelper = new ConnectionHelper();
                connect = connectionHelper.conclass();
                if (connect == null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(RegisterActivity.this, "Check Internet Connection", Toast.LENGTH_LONG).show();
                        }
                    });
                    z = "On Internet Connection";
                } else {

                        String sql = "INSERT INTO Users (PreFix,CompanyName,UserName,ContactNo,EmailId,UserPassword,BranchCompany,AccountType,RoleCode,ProfilePhotoPath,Role,HeadCompany) " +
                                "VALUES ('" + "ZUSR" + "','" + company_name.getText() + "','" + name.getText() + "','" + mobile_no.getText() + "','" + email.getText() + "','" + password.getText() + "','" + "ZSCS" + "','" + "Demo" + "','" + 0 + "','" + "/images/blankpic.png" + "','" + 0 + "','" + "ZSCS" + "')";
                        PreparedStatement stmt = connect.prepareStatement(sql);
                        stmt.executeUpdate();
                        stmt.close();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(RegisterActivity.this, "User is registered successfully", Toast.LENGTH_LONG).show();
                            }
                        });
                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();

                        return "User is registered successfully";

                    }
                }
            catch (SQLException e) {
                e.printStackTrace();
                return e.getMessage();
            } catch (Exception e) {
                isSuccess = false;
                Log.e("SQL Error : ", e.getMessage());
            }

            return z;
        }


        @Override
        protected void onPostExecute(String s) {

        }
    }
}
