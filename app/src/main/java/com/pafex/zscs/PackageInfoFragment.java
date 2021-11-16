package com.pafex.zscs;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PackageInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PackageInfoFragment extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Spinner shipment_type, measurement, ready_time, latest_time;
    private TextInputEditText pickup_date, total_package, weight, pickup_noti, sp_inst;
    private Calendar myCalendar;
    private String company, contact_name, address1, address2, address3, postal_code, city, phone_no, acc_no, country_ter, is_residence, new_pickup_loc, save_address;
    private Button submit_btn;
    boolean isValid;
    private String shipment_type_selectedValue, measurement_selectedValue, ready_time_selectedValue, latest_time_selectedValue;
    private Connection connect;
    private CheckBox express_pickup;
    private TextView check_tv;

    public PackageInfoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PackageInfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PackageInfoFragment newInstance(String param1, String param2) {
        PackageInfoFragment fragment = new PackageInfoFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_package_info, container, false);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            acc_no = bundle.getString("acc_no", "");
            country_ter = bundle.getString("country_ter", "");
            company = bundle.getString("company", "");
            contact_name = bundle.getString("contact_name", "");
            address1 = bundle.getString("address1", "");
            address2 = bundle.getString("address2", "");
            address3 = bundle.getString("address3", "");
            postal_code = bundle.getString("postal_code", "");
            city = bundle.getString("city", "");
            phone_no = bundle.getString("phone_no", "");
            is_residence = bundle.getString("is_residence", "");
            new_pickup_loc = bundle.getString("new_pickup_loc", "");
            save_address = bundle.getString("save_address", "");
        }
        shipment_type = (Spinner) rootView.findViewById(R.id.shipment_type);
        measurement = (Spinner) rootView.findViewById(R.id.measurement);
        ready_time = (Spinner) rootView.findViewById(R.id.ready_time);
        latest_time = (Spinner) rootView.findViewById(R.id.latest_time);
        pickup_date = (TextInputEditText) rootView.findViewById(R.id.pickup_date);
        total_package = (TextInputEditText)rootView.findViewById(R.id.total_pkg);
        weight = (TextInputEditText)rootView.findViewById(R.id.weight);
        pickup_noti = (TextInputEditText)rootView.findViewById(R.id.pickup_noti);
        sp_inst = (TextInputEditText)rootView.findViewById(R.id.sp_inst);
        express_pickup = (CheckBox)rootView.findViewById(R.id.experss_pickup);
        check_tv = (TextView)rootView.findViewById(R.id.check_tv1) ;
        submit_btn = (Button)rootView.findViewById(R.id.submit_btn);
        myCalendar = Calendar.getInstance();
        String[] items = new String[]{"Shipment Type", "Domestic", "International"};
        String[] items1 = new String[]{"Measurement", "Kg", "Lbs"};
        String[] items2= new String[]{"Ready Time", "1:00 AM", "2:00 AM", "1:00 PM", "2:00 PM"};
        String[] items3= new String[]{"Latest Time Available", "1:00 AM", "2:00 AM", "1:00 PM", "2:00 PM"};
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
        shipment_type.setAdapter(adapter);
        shipment_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                shipment_type_selectedValue = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getContext(), R.layout.spinner_item_selected, items1){

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
        adapter1.setDropDownViewResource(R.layout.spinner_dropdown);
//set the spinners adapter to the previously created one.
        measurement.setAdapter(adapter1);
        measurement.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                measurement_selectedValue = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getContext(), R.layout.spinner_item_selected, items2){

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
        adapter2.setDropDownViewResource(R.layout.spinner_dropdown);
//set the spinners adapter to the previously created one.
        ready_time.setAdapter(adapter2);
        ready_time.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ready_time_selectedValue = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(getContext(), R.layout.spinner_item_selected, items3){

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
        adapter3.setDropDownViewResource(R.layout.spinner_dropdown);
//set the spinners adapter to the previously created one.
        latest_time.setAdapter(adapter3);
        latest_time.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                latest_time_selectedValue = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                myCalendar.set(Calendar.YEAR,y);
                myCalendar.set(Calendar.MONTH,m);
                myCalendar.set(Calendar.DAY_OF_MONTH,d);
                updateDate();
            }
        };

        pickup_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        submit_btn.setOnClickListener(this);

        return rootView;
    }

    private void updateDate() {
        String myFormat = "dd-MM-yyyy"; //put your date format in which you need to display
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);

        pickup_date.setText(sdf.format(myCalendar.getTime()));
    }

    private boolean SetValidation() {

        if (shipment_type.getSelectedItem().toString().trim().equals("Shipment Type")) {
            Toast.makeText(getContext(), "Choose one of Shipment type!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (measurement.getSelectedItem().toString().trim().equals("Measurement")) {
            Toast.makeText(getContext(), "Choose one of Measurement!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (ready_time.getSelectedItem().toString().trim().equals("Ready Time")) {
            Toast.makeText(getContext(), "Choose one of time!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (latest_time.getSelectedItem().toString().trim().equals("Latest Time Available")) {
            Toast.makeText(getContext(), "Choose one of time!", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Check for a valid company name
        if (total_package.getText().toString().isEmpty()) {
            total_package.setError(getResources().getString(R.string.totalpkg_error));
            return false;
        }

        // Check for a valid name
        if (weight.getText().toString().isEmpty()) {
            weight.setError(getResources().getString(R.string.totalweight_error));
            return false;
        }

        return true;
    }


    @Override
    public void onResume() {
        super.onResume();
        if(getActivity()!=null){
            ((MainActivity)getActivity()).getSupportActionBar().setTitle("Package Information");
        }

    }

    @Override
    public void onClick(View view) {
        if(express_pickup.isChecked()){
            check_tv.setText("1");
        }
        else {
            check_tv.setText("0");
        }
        isValid = SetValidation();
        if(isValid){
            new PackageInfoFragment.submitDetails().execute("");

        }

    }

    public class submitDetails extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings) {
            String z = null;
            Boolean isSuccess = false;

            try {
                ConnectionHelper connectionHelper = new ConnectionHelper();
                connect = connectionHelper.conclass();
                if(connect==null){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(),"Check Internet Connection!", Toast.LENGTH_LONG).show();
                        }
                    });
                    z = "On Internet Connection";
                } else{

                    String sql = "INSERT INTO PickupDetails (AccountNumber,CountryTerritory,Company,ContactName,Address1," +
                            "Address2,PostalCode,City,PhoneNumber,IsResidence,IsAddnewpickuplocation,IssaveChangestoexistingaddress," +
                            "IsScheduleexpresspickup,TotalPackages,ShipmentType,TotalWeight,WeightType,PickupTime,PickupDate,ReadyTime," +
                            "LatestTimeavailable,Specialinstruction,PickupNotification) " +
                            "VALUES ('" + acc_no +"', '" + "101" +"', '" + company +"', '" + contact_name +"'," +
                            "'" + address1 +"', '" + address2 + "', '" + postal_code +"', '" + city +"', '" + phone_no +"'," +
                            "'" + is_residence +"', '" + new_pickup_loc + "', '" + save_address + "', '" + check_tv.getText().toString() +"'," +
                            "'" + total_package.getText() +"', '" + shipment_type_selectedValue +"', '" + weight.getText() +"'," +
                            "'" + measurement_selectedValue +"', '" + ready_time_selectedValue +"', '" + pickup_date.getText() +"'," +
                            "'" + ready_time_selectedValue +"', '" + latest_time_selectedValue + "', '" + sp_inst.getText() + "'," +
                            "'" + pickup_noti.getText() +"')";

                    PreparedStatement statement = connect.prepareStatement(sql);
                    statement.executeUpdate();
                    statement.close();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(), "Entered details are saved successfully!",Toast.LENGTH_SHORT).show();
                        }
                    });
                    String backStateName = this.getClass().getName();
                    AppCompatActivity activity = (AppCompatActivity) getContext();
                    Fragment fragment = new HomeFragment();
                    FragmentManager fragmentManager = activity.getSupportFragmentManager();
                    boolean fragmentPopped = fragmentManager.popBackStackImmediate(backStateName, 0);
                    if (!fragmentPopped) {
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.frame, fragment);
                        fragmentTransaction.addToBackStack(backStateName);
                        fragmentTransaction.commit();
                    }
                }

            } catch (Exception e) {
                isSuccess = false;
                Log.e("SQL Error : ", e.getMessage());
            }
            return z;
        }
    }
}