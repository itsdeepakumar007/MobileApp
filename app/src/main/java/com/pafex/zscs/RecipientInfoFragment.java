package com.pafex.zscs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.textfield.TextInputEditText;
import com.shuhart.stepview.StepView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecipientInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecipientInfoFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Spinner country;
    View rootView;
    private Connection connect;
    private PreparedStatement stmt;
    private ResultSet rs;
    boolean isValid;
    private String country_selectedValue;
    private Button pincode_go;
    private TextInputEditText pincode, state, city, company_name, email, phone_no, address1, address2, address3;
    private String city_str, state_str,pincode_str;
    private StepView stepView;
    private String state_id, city_id;

    public RecipientInfoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RecipientInfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RecipientInfoFragment newInstance(String param1, String param2) {
        RecipientInfoFragment fragment = new RecipientInfoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_recipient_info, container, false);
        company_name = (TextInputEditText)rootView.findViewById(R.id.company_name);
        email = (TextInputEditText)rootView.findViewById(R.id.email);
        phone_no = (TextInputEditText)rootView.findViewById(R.id.phone_no);
        address1 = (TextInputEditText)rootView.findViewById(R.id.address_1);
        address2 = (TextInputEditText)rootView.findViewById(R.id.address_2);
        address3 = (TextInputEditText)rootView.findViewById(R.id.address_3);
        country = (Spinner)rootView.findViewById(R.id.country);
        state = (TextInputEditText) rootView.findViewById(R.id.state);
        city = (TextInputEditText)rootView.findViewById(R.id.city);
        pincode = (TextInputEditText)rootView.findViewById(R.id.pincode);
        pincode_go = (Button)rootView.findViewById(R.id.pincode_go);
        boolean getValue = false;
        if (getArguments() != null) {
            getValue = getArguments().getBoolean("Boolean_Value1");
        }
        if(getValue) {

        }else{
            Bundle bundle = this.getArguments();
            city_str = bundle.getString("city", "");
            state_str = bundle.getString("state", "");
            pincode_str = bundle.getString("pincode","");
        }
        showSetpView0();
        new RecipientInfoFragment.checkCountry().execute("");
        pincode_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isValid = SetValidation();
                if (isValid) {
                    String pin = pincode.getText().toString();
                    String backStateName = this.getClass().getName();
                    AppCompatActivity activity = (AppCompatActivity) view.getContext();
                    Fragment fragment = new PincodeFragment_Recipient();
                    Bundle bundle = new Bundle();
                    bundle.putString("pincode",pin);
                    fragment.setArguments(bundle);
                    FragmentManager fragmentManager = activity.getSupportFragmentManager();
                    boolean fragmentPopped = fragmentManager.popBackStackImmediate(backStateName, 0);
                    if (!fragmentPopped) {
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.frame, fragment);
                        fragmentTransaction.addToBackStack(backStateName);
                        fragmentTransaction.commit();
                    }
                }
            }
        });
        state.setText(state_str);
        city.setText(city_str);
        pincode.setText(pincode_str);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    getActivity().getSupportFragmentManager().popBackStack("shipperInfoFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    return true;
                }
                return false;
            }
        });
    }


    private void showSetpView0()
    {
        final StepView stepView = rootView.findViewById(R.id.step_view);
        stepView.setSteps(new ArrayList<String>() {{
            add("Consignee Information \n(Shipper)");
            add("Consignee Information \n(Recipient)");
            add("Shipment \nInformation");
            add("Total Cost");
        }});
        stepView.go(1, true);
        rootView.findViewById(R.id.next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isValid = SetValidation1();
                if(isValid){
                    new RecipientInfoFragment.checkState().execute();
                    new RecipientInfoFragment.checkCity().execute();
                    SharedPreferences preferences = getActivity().getSharedPreferences("Shipping_Prefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("R_Company Name",company_name.getText().toString());
                    editor.putString("R_Email",email.getText().toString());
                    editor.putString("R_Phone Number",phone_no.getText().toString());
                    editor.putString("R_Address 1",address1.getText().toString());
                    editor.putString("R_Address 2",address2.getText().toString());
                    editor.putString("R_Address 3",address3.getText().toString());
                    editor.putString("R_Pincode",pincode.getText().toString());
                    editor.putString("R_State",state.getText().toString());
                    editor.putString("R_City",city.getText().toString());
                    editor.putString("R_State ID",state_id);
                    editor.putString("R_City ID",city_id);
                    editor.apply();
                    String backStateName = this.getClass().getName();
                    AppCompatActivity activity = (AppCompatActivity) rootView.getContext();
                    Fragment fragment = new ShipmentInfoFragment();
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("Boolean_Value1",true);
                    fragment.setArguments(bundle);
                    FragmentManager fragmentManager = activity.getSupportFragmentManager();
                    boolean fragmentPopped = fragmentManager.popBackStackImmediate (backStateName, 0);
                    if(!fragmentPopped) {
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.frame, fragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }
                }
            }
        });
        rootView.findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    String backStateName = this.getClass().getName();
                    AppCompatActivity activity = (AppCompatActivity) rootView.getContext();
                    Fragment fragment = new ShipperInfoFragment();
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("Boolean_Value",true);
                    fragment.setArguments(bundle);
                    FragmentManager fragmentManager = activity.getSupportFragmentManager();
                    boolean fragmentPopped = fragmentManager.popBackStackImmediate (backStateName, 0);
                    if(!fragmentPopped) {
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.frame, fragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }
            }
        });
    }


    private boolean SetValidation() {

        String regex = "^[1-9]{1}[0-9]{2}\\s{0,1}[0-9]{3}$";
        if (pincode.getText().toString().isEmpty() | !pincode.getText().toString().matches(regex)) {
            pincode.setError(getResources().getString(R.string.pincode_error));
            return false;
        }

        return true;
    }

    private boolean SetValidation1() {
        String regex = "^[1-9]{1}[0-9]{2}\\s{0,1}[0-9]{3}$";
        if (pincode.getText().toString().isEmpty() | !pincode.getText().toString().matches(regex)) {
            pincode.setError(getResources().getString(R.string.pincode_error));
            return false;
        }

// Check for a valid mobile number
        if (phone_no.getText().toString().isEmpty() | !Patterns.PHONE.matcher(phone_no.getText().toString()).matches()) {
            phone_no.setError(getResources().getString(R.string.mobile_error));
            return false;
        }

        if (company_name.getText().toString().isEmpty()) {
            company_name.setError(getResources().getString(R.string.company_name_error));
            return false;
        }

        if (email.getText().toString().isEmpty() | !Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
            email.setError(getResources().getString(R.string.email_error));
            return false;
        }

        if (address1.getText().toString().isEmpty()) {
            address1.setError(getResources().getString(R.string.address_error));
            return false;
        }

        if (state.getText().toString().isEmpty()) {
            state.setError(getResources().getString(R.string.state_error));
            return false;
        }

        if (city.getText().toString().isEmpty()) {
            city.setError(getResources().getString(R.string.city_error));
            return false;
        }

        return true;
    }

    public class checkCountry extends AsyncTask<String, String, String> {

        String z = null;
        Boolean isSuccess = false;

        @SuppressLint("LongLogTag")
        @Override
        protected String doInBackground(String... strings) {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            connect = connectionHelper.conclass();
            if (connect == null) {
                Toast.makeText(getContext(), "Check Internet Connection!", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    String query = "select * from CountryMaster where ID = '" + "101" +"'";
                    stmt = connect.prepareStatement(query);
                    rs = stmt.executeQuery();
                    Log.v(query,"query");
                    ArrayList<String> data = new ArrayList<>();

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                while (rs.next()) {
                                    String id = rs.getString("Name");
                                    data.add(id);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            String[] array = data.toArray(new String[0]);
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.spinner_item_selected, array);
                            adapter.setDropDownViewResource(R.layout.spinner_dropdown);
                            country.setAdapter(adapter);
                            country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    country_selectedValue = adapterView.getItemAtPosition(i).toString();
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {

                                }
                            });

                        }
                    });
                    z="Success";

                }catch (Exception e){
                    isSuccess = false;
                    Log.e("SQL Error : ", e.getMessage());
                }

            }
            return z;
        }
    }

    public class checkState extends AsyncTask<String, String, String> {

        String z = null;
        Boolean isSuccess = false;

        @SuppressLint("LongLogTag")
        @Override
        protected String doInBackground(String... strings) {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            connect = connectionHelper.conclass();
            if (connect == null) {
                Toast.makeText(getContext(), "Check Internet Connection!", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    String query = "select * from StateMaster where Name = '" + state.getText() +"'";
                    stmt = connect.prepareStatement(query);
                    rs = stmt.executeQuery();
                    Log.v(query,"query");
                    ArrayList<String> data = new ArrayList<>();

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                while (rs.next()) {
                                    state_id = rs.getString("ID");
                                    data.add(state_id);
                                    Log.v(state_id,"stateid");
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            String[] array = data.toArray(new String[0]);
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.spinner_item_selected, array);
                            adapter.setDropDownViewResource(R.layout.spinner_dropdown);
                            country.setAdapter(adapter);
                            country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    country_selectedValue = adapterView.getItemAtPosition(i).toString();
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {

                                }
                            });

                        }
                    });
                    z="Success";

                }catch (Exception e){
                    isSuccess = false;
                    Log.e("SQL Error : ", e.getMessage());
                }

            }
            return z;
        }
    }

    public class checkCity extends AsyncTask<String, String, String> {

        String z = null;
        Boolean isSuccess = false;

        @SuppressLint("LongLogTag")
        @Override
        protected String doInBackground(String... strings) {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            connect = connectionHelper.conclass();
            if (connect == null) {
                Toast.makeText(getContext(), "Check Internet Connection!", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    String query = "select * from CityMaster where Name = '" + city.getText() +"'";
                    stmt = connect.prepareStatement(query);
                    rs = stmt.executeQuery();
                    Log.v(query,"query");
                    ArrayList<String> data = new ArrayList<>();

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                while (rs.next()) {
                                    city_id = rs.getString("ID");
                                    data.add(city_id);
                                    Log.v(city_id,"stateid");
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            String[] array = data.toArray(new String[0]);
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.spinner_item_selected, array);
                            adapter.setDropDownViewResource(R.layout.spinner_dropdown);
                            country.setAdapter(adapter);
                            country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    country_selectedValue = adapterView.getItemAtPosition(i).toString();
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {

                                }
                            });

                        }
                    });
                    z="Success";

                }catch (Exception e){
                    isSuccess = false;
                    Log.e("SQL Error : ", e.getMessage());
                }

            }
            return z;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(getActivity()!=null){
            ((MainActivity)getActivity()).getSupportActionBar().setTitle("Shipping");
        }
    }


}