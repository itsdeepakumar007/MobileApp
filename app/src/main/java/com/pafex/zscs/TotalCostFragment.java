package com.pafex.zscs;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.shuhart.stepview.StepView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TotalCostFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TotalCostFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View rootView;
    private float vol_weig;
    private float vol_weig2;
    private EditText vol_weight;
    private float total_weight, total_weight2;
    private String service_type, service_type2;
    private EditText et_tw, et_cw, et_iv;
    private TextView tw_st;
    private Button estimate_rate;
    private ServiceDetail service_detail;
    private float rto_charge;
    private String company_name, email, phone_no, address1, address2, address3, pincode, state, city, state_id, city_id;
    private String r_company_name, r_email, r_phone_no, r_address1, r_address2, r_address3, r_pincode, r_state, r_city, r_state_id, r_city_id;
    private String packaging_type, length, width, weight, height, code, desc, madeIn, check_tv1, check_tv2, check_tv3, check_tv4;
    private boolean isValid;
    private String shipper_id, recipient_id;
    private String sp_handling;
    private Connection connect;
    private float invoice;
    private String currentDateandTime;

    public TotalCostFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TotalCostFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TotalCostFragment newInstance(String param1, String param2) {
        TotalCostFragment fragment = new TotalCostFragment();
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
        rootView = inflater.inflate(R.layout.fragment_total_cost, container, false);
        vol_weight = (EditText)rootView.findViewById(R.id.et_vw);
        et_tw = (EditText)rootView.findViewById(R.id.et_tw);
        et_cw = (EditText)rootView.findViewById(R.id.et_cw);
        et_iv = (EditText)rootView.findViewById(R.id.et_iv);
        tw_st = (TextView)rootView.findViewById(R.id.service_type);
        estimate_rate = (Button)rootView.findViewById(R.id.estimate_rate);
        boolean getValue = false;
        if (getArguments() != null) {
            getValue = getArguments().getBoolean("Boolean");
        }
        if(getValue) {

        }else{
            Bundle bundle = this.getArguments();
            if(bundle!=null) {
                rto_charge = bundle.getFloat("rto_charge", 0);
                vol_weig2 = bundle.getFloat("Volumetric Weight2", 0);
                total_weight2 = bundle.getFloat("Total Weight2", 0);
                service_type2 = bundle.getString("ServiceType2", "");
            }
            Log.v(String.valueOf(total_weight2),"volw");

            et_tw.post(new Runnable() {
                @Override
                public void run() {
                    et_tw.setText(String.valueOf(total_weight2));
                }
            });
            et_cw.post(new Runnable() {
                @Override
                public void run() {
                    et_cw.setText(String.valueOf(total_weight2));
                }
            });
            tw_st.post(new Runnable() {
                @Override
                public void run() {
                    tw_st.setText(service_type2);
                }
            });
            vol_weight.post(new Runnable() {
                @Override
                public void run() {
                    String w = String.format("%.3f",vol_weig2);
                    vol_weight.setText(w);
                }
            });
        }
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            vol_weig = bundle.getFloat("Volumetric Weight",0);
            total_weight = bundle.getFloat("Total Weight",0);
            service_type = bundle.getString("ServiceType","");

        }
        et_tw.setText(String.valueOf(total_weight));
        et_cw.setText(String.valueOf(total_weight));
        tw_st.setText(service_type);
        String w = String.format("%.3f",vol_weig);
        vol_weight.setText(w);
        invoice = rto_charge*total_weight2;
        String inv = String.format("%.3f",invoice);
        et_iv.setText(inv);

        estimate_rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(service_type.equals("By Air")) {
                    String backStateName = this.getClass().getName();
                    AppCompatActivity activity = (AppCompatActivity) rootView.getContext();
                    Fragment fragment = new EstimateRateFragmentByAir();
                    Bundle bundle = new Bundle();
                    bundle.putFloat("Volumetric Weight1", vol_weig);
                    bundle.putFloat("Total Weight1", total_weight);
                    bundle.putString("ServiceType1", service_type);
                    fragment.setArguments(bundle);
                    FragmentManager fragmentManager = activity.getSupportFragmentManager();
                    boolean fragmentPopped = fragmentManager.popBackStackImmediate(backStateName, 0);
                    if (!fragmentPopped) {
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.frame, fragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }
                }
                else if(service_type.equals("By Road")){
                    String backStateName = this.getClass().getName();
                    AppCompatActivity activity = (AppCompatActivity) rootView.getContext();
                    Fragment fragment = new EstimateRateFragmentByRoad();
                    Bundle bundle = new Bundle();
                    bundle.putFloat("Volumetric Weight1", vol_weig);
                    bundle.putFloat("Total Weight1", total_weight);
                    bundle.putString("ServiceType1", service_type);
                    fragment.setArguments(bundle);
                    FragmentManager fragmentManager = activity.getSupportFragmentManager();
                    boolean fragmentPopped = fragmentManager.popBackStackImmediate(backStateName, 0);
                    if (!fragmentPopped) {
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.frame, fragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }
                }
            }
        });
        getSharedPreferences();
        showSetpView0();

        return rootView;
    }

    private void getSharedPreferences() {
        SharedPreferences preferences = getActivity().getSharedPreferences("Shipping_Prefs", Context.MODE_PRIVATE);
        company_name = preferences.getString("Company Name","");
        email = preferences.getString("Email","");
        phone_no = preferences.getString("Phone Number","");
        address1 = preferences.getString("Address 1","");
        address2 = preferences.getString("Address 2","");
        address3 = preferences.getString("Address 3","");
        pincode = preferences.getString("Pincode","");
        state = preferences.getString("State","");
        city = preferences.getString("City","");
        state_id = preferences.getString("State ID","");
        city_id = preferences.getString("City ID","");
        r_company_name = preferences.getString("R_Company Name","");
        r_email = preferences.getString("R_Email","");
        r_phone_no = preferences.getString("R_Phone Number","");
        r_address1 = preferences.getString("R_Address 1","");
        r_address2 = preferences.getString("R_Address 2","");
        r_address3 = preferences.getString("R_Address 3","");
        r_pincode = preferences.getString("R_Pincode","");
        r_state = preferences.getString("R_State","");
        r_city = preferences.getString("R_City","");
        r_state_id = preferences.getString("R_State ID","");
        r_city_id = preferences.getString("R_City ID","");
        packaging_type = preferences.getString("Packaging Type","");
        length = preferences.getString("Length","");
        width = preferences.getString("Width","");
        height = preferences.getString("Height","");
        weight = preferences.getString("Weight","");
        code = preferences.getString("Code","");
        desc = preferences.getString("Desc","");
        madeIn = preferences.getString("MadeIn","");
        sp_handling = preferences.getString("Special Handling","");
        shipper_id = preferences.getString("Shipper ID", "");
        recipient_id = preferences.getString("Recipient ID","");

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
        stepView.go(3, true);
        rootView.findViewById(R.id.submit_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isValid = SetValidation();
                if (isValid){
                    new TotalCostFragment.submitDetails().execute("");

                }
            }
        });
        rootView.findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String backStateName = this.getClass().getName();
                AppCompatActivity activity = (AppCompatActivity) rootView.getContext();
                Fragment fragment = new ShipmentInfoFragment();
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


        // Check for a valid company name
        if (vol_weight.getText().toString().isEmpty()) {
            vol_weight.setError(getResources().getString(R.string.totalweight_error));
            return false;
        }

        // Check for a valid name
        if (et_tw.getText().toString().isEmpty()) {
            et_tw.setError(getResources().getString(R.string.totalweight_error));
            return false;
        }

        if (et_cw.getText().toString().isEmpty()) {
            et_cw.setError(getResources().getString(R.string.totalweight_error));
            return false;
        }

        if (tw_st.getText().toString().isEmpty()) {
            tw_st.setError(getResources().getString(R.string.service_type));
            return false;
        }

        if (et_iv.getText().toString().isEmpty()) {
            et_iv.setError(getResources().getString(R.string.invoice));
            return false;
        }

        return true;
    }

    public class submitDetails extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings) {
            String z = null;
            boolean isSuccess = false;

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

                    String volweigh = String.format("%.2f",vol_weig2);
                    String sql1 = "INSERT INTO Shipper (vc_UserCode,vc_UserCompanyCode,vc_UserCompanyName,vc_PreFix,vc_CompanyName," +
                            "vc_Address1,vc_Address2,vc_Address3,vc_CountryID,vc_CountryName,vc_StateID,vc_StateName,vc_CityID,vc_CityName," +
                            "vc_PinCode,vc_Email,vc_PhoneNumber1,b_Active,vc_ShipmentType) " +
                            "VALUES ('" + "ZUSR0000001" +"', '" + "ZSCS" +"', '" + "ZSCS" +"', '" + "ZSS" +"'," +
                            "'" + company_name +"', '" + address1 + "', '" + address2 +"', '" + address3 +"', '" + "101" +"'," +
                            "'" + "India" +"','" + state_id +"', '" + state + "', '" + city_id +"', '" + city + "', '" + pincode +"'," +
                            "'" + email +"', '" + phone_no +"', '" + 1 +"', + '" + "D" +"')";

                    String sql2 = "INSERT INTO Recipient (vc_UserCode,vc_UserCompanyCode,vc_UserCompanyName,vc_PreFix,vc_CompanyName," +
                            "vc_Address1,vc_Address2,vc_Address3,vc_CountryID,vc_CountryName,vc_StateID,vc_StateName,vc_CityID,vc_CityName," +
                            "vc_PinCode,vc_Email,vc_PhoneNumber1,b_Active,vc_ShipmentType) " +
                            "VALUES ('" + "ZUSR0000001" +"', '" + "ZSCS" +"', '" + "ZSCS" +"', '" + "ZSR" +"'," +
                            "'" + r_company_name +"', '" + r_address1 + "', '" + r_address2 +"', '" + r_address3 +"', '" + "101" +"'," +
                            "'" + "India" +"', '" + r_state_id + "' , '" + r_state + "','" + r_city_id + "', '" + r_city + "', '" + r_pincode +"'," +
                            "'" + r_email +"', '" + r_phone_no +"', '" + 1 +"', + '" + "D" +"')";

                    String sql3 = "INSERT INTO Shipment (vc_UserCode,vc_UserCompanyCode,vc_UserCompanyName,PreFix,vc_SpecialHandling," +
                            "ServiceID,ServiceName,vc_ShipperID,vc_RecipientID,vc_ShipmentStatus,vc_ShipmentType,vc_CurrencyID," +
                            "vc_CurrencyName,vc_InvoiceValue,vc_InvoiceValueWhole,vc_ShipmentVolumeWeight," +
                            "vc_ShipmentVolumeWeightWhole,vc_ShipmentTotalWeight,vc_ChargeableWeight,vc_WeightMeasurementUnit,vc_WeightMeasurement," +
                            "vc_Transport) " +
                            "VALUES ('" + "ZUSR0000001" +"', '" + "ZSCS" +"', '" + "ZSCS" +"', '" + "ZSR" +"'," +
                            "'" + sp_handling +"', '" + 1 + "' , '" + 0 + "' ,'" + shipper_id + "','" + recipient_id + "', '" + "New" + "', '" + "D" +"', '" + "INR" +"', '" + "INR" +"'," +
                            "'" + et_iv.getText() +"', '" + invoice + "', '" + vol_weight.getText() + "', '" + volweigh +"'," +
                            "'" + et_tw.getText() +"', '" + et_cw.getText() +"', '" + "Kg" +"', + '" + "Kg" +"', " +
                            "'" + service_type2 +"')";


                    PreparedStatement statement1 = connect.prepareStatement(sql1);
                    statement1.executeUpdate();
                    statement1.close();
                    PreparedStatement statement2 = connect.prepareStatement(sql2);
                    statement2.executeUpdate();
                    statement2.close();
                    PreparedStatement statement3 = connect.prepareStatement(sql3);
                    statement3.executeUpdate();
                    statement3.close();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(), "Shipment details are saved successfully!",Toast.LENGTH_SHORT).show();
                        }
                    });

                }

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

            } catch (Exception e) {
                isSuccess = false;

                Log.e("SQL Error : ", e.getMessage());
            }
            return z;
        }
    }



}