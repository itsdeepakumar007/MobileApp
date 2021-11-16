package com.pafex.zscs;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.textfield.TextInputEditText;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PickUpFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PickUpFragment extends Fragment {


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Spinner acc_no, country_ter;
    private TextInputEditText company, contact_name, address1, address2, address3, postal_code, city, phone_no;
    private Button pkg_info_btn;
    private CheckBox is_residence,new_pickup_loc,save_address;
    private Connection connect;
    private PreparedStatement stmt;
    private ResultSet rs;
    boolean isValid;
    private String acc_no_selectedValue;
    private String countryter_selectedValue;
    private TextView check_tv1,check_tv2,check_tv3;

    public PickUpFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EmployeeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PickUpFragment newInstance(String param1, String param2) {
        PickUpFragment fragment = new PickUpFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_pickup, container, false);
        acc_no = (Spinner)rootView.findViewById(R.id.acc_no);
        country_ter = (Spinner) rootView.findViewById(R.id.country_ter);
        pkg_info_btn = (Button)rootView.findViewById(R.id.pkg_info_btn);
        company = (TextInputEditText) rootView.findViewById(R.id.company);
        contact_name = (TextInputEditText) rootView.findViewById(R.id.contact_name);
        address1 = (TextInputEditText)rootView.findViewById(R.id.address_1);
        address2 = (TextInputEditText)rootView.findViewById(R.id.address_2);
        address3 = (TextInputEditText)rootView.findViewById(R.id.address_3);
        postal_code = (TextInputEditText)rootView.findViewById(R.id.postal_code);
        city = (TextInputEditText)rootView.findViewById(R.id.city);
        phone_no = (TextInputEditText)rootView.findViewById(R.id.phone_no);
        is_residence = (CheckBox)rootView.findViewById(R.id.check_is_residence);
        new_pickup_loc = (CheckBox)rootView.findViewById(R.id.check_new_location);
        save_address = (CheckBox)rootView.findViewById(R.id.check_address);
        check_tv1 = (TextView)rootView.findViewById(R.id.check_tv1);
        check_tv2 = (TextView)rootView.findViewById(R.id.check_tv2);
        check_tv3 = (TextView)rootView.findViewById(R.id.check_tv3);
        new PickUpFragment.checkCountry().execute("");
//create a list of items for the spinner.
        String[] items = new String[]{"Account Number", "Account 1", "Account 2"};
//create an adapter to describe how the items are displayed, adapters are used in several places in android.
//There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item_selected, items){
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                }
                else
                {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };

        adapter.setDropDownViewResource(R.layout.spinner_dropdown);
//set the spinners adapter to the previously created one.
        acc_no.setAdapter(adapter);
        acc_no.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                acc_no_selectedValue = adapterView.getItemAtPosition(i).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        pkg_info_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(is_residence.isChecked()){
                    check_tv1.setText("1");
                }
                else {
                    check_tv1.setText("0");
                }
                if (new_pickup_loc.isChecked()){
                    check_tv2.setText("1");
                }
                else{
                    check_tv2.setText("0");
                }
                if(save_address.isChecked()){
                    check_tv3.setText("1");
                }
                else{
                    check_tv3.setText("0");
                }
                isValid = SetValidation();
                if(isValid){
                    String backStateName = this.getClass().getName();
                    AppCompatActivity activity = (AppCompatActivity) view.getContext();
                    Fragment fragment = new PackageInfoFragment();
                    FragmentManager fragmentManager = activity.getSupportFragmentManager();
                    boolean fragmentPopped = fragmentManager.popBackStackImmediate(backStateName, 0);
                    if (!fragmentPopped) {
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.frame, fragment);
                        fragmentTransaction.addToBackStack(backStateName);
                        fragmentTransaction.commit();
                    }
                    Bundle bundle = new Bundle();
                    bundle.putString("acc_no", acc_no_selectedValue);
                    bundle.putString("country_ter", countryter_selectedValue);
                    bundle.putString("company",company.getText().toString());
                    bundle.putString("contact_name",contact_name.getText().toString());
                    bundle.putString("address1",address1.getText().toString());
                    bundle.putString("address2",address2.getText().toString());
                    bundle.putString("address3",address3.getText().toString());
                    bundle.putString("postal_code",postal_code.getText().toString());
                    bundle.putString("city",city.getText().toString());
                    bundle.putString("phone_no",phone_no.getText().toString());
                    bundle.putString("is_residence",check_tv1.getText().toString());
                    bundle.putString("new_pickup_loc",check_tv2.getText().toString());
                    bundle.putString("save_address",check_tv3.getText().toString());
                    fragment.setArguments(bundle);
                }

                acc_no.setSelection(0);
                company.setText("");
                contact_name.setText("");
                address1.setText("");
                address2.setText("");
                address3.setText("");
                postal_code.setText("");
                city.setText("");
                phone_no.setText("");
                is_residence.setChecked(false);
                new_pickup_loc.setChecked(false);
                save_address.setChecked(false);
            }
        });


        return rootView;
    }

    private boolean SetValidation() {

        if (acc_no.getSelectedItem().toString().trim().equals("Account Number")) {
            Toast.makeText(getContext(), "Choose one of Account Number!", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Check for a valid company name
        if (company.getText().toString().isEmpty()) {
            company.setError(getResources().getString(R.string.company_name_error));
            return false;
        }

        // Check for a valid name
        if (contact_name.getText().toString().isEmpty()) {
            contact_name.setError(getResources().getString(R.string.name_error));
            return false;
        }


        if (address1.getText().toString().isEmpty()) {
            address1.setError(getResources().getString(R.string.address_error));
            return false;
        }

       /* if (address2.getText().toString().isEmpty()) {
            address2.setError(getResources().getString(R.string.name_error));
            return false;
        }

        if (address3.getText().toString().isEmpty()) {
            address3.setError(getResources().getString(R.string.name_error));
            return false;
        }*/

        if (postal_code.getText().toString().isEmpty()) {
            postal_code.setError(getResources().getString(R.string.postal_code_error));
            return false;
        }

        if (city.getText().toString().isEmpty()) {
            city.setError(getResources().getString(R.string.city_error));
            return false;
        }

        // Check for a valid mobile number
        if (phone_no.getText().toString().isEmpty() | !Patterns.PHONE.matcher(phone_no.getText().toString()).matches()) {
            phone_no.setError(getResources().getString(R.string.mobile_error));
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
                                    country_ter.setAdapter(adapter);
                                    country_ter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                            countryter_selectedValue = adapterView.getItemAtPosition(i).toString();
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
            ((MainActivity)getActivity()).getSupportActionBar().setTitle("Pickup Details");
        }
    }

}