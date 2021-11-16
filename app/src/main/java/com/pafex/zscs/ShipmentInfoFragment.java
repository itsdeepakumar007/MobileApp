package com.pafex.zscs;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.textfield.TextInputEditText;
import com.shuhart.stepview.StepView;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ShipmentInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShipmentInfoFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    View rootView;
    private Spinner packaging_type,service_type;
    private String city_str, state_str,pincode_str;
    private boolean isSuccess;
    private Connection connect;
    private EditText length, width, height, weight, code, desc, madeIn;
    private TextInputEditText total_weight;
    private CheckBox tdd, sd, pdso, hpl;
    private String packaging_type_selectedValue, service_type_selectedValue;
    private float vol_wei;
    private int tot_wei;
    private boolean isValid;

    public ShipmentInfoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ShipmentInfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ShipmentInfoFragment newInstance(String param1, String param2) {
        ShipmentInfoFragment fragment = new ShipmentInfoFragment();
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
        rootView = inflater.inflate(R.layout.fragment_shipment_info, container, false);
        packaging_type = (Spinner)rootView.findViewById(R.id.packaging_type);
        service_type = (Spinner)rootView.findViewById(R.id.service_type);
        length = (EditText)rootView.findViewById(R.id.et_length);
        width = (EditText)rootView.findViewById(R.id.et_width);
        height = (EditText)rootView.findViewById(R.id.et_height);
        weight = (EditText)rootView.findViewById(R.id.et_weight);
        code = (EditText)rootView.findViewById(R.id.et_code);
        desc = (EditText)rootView.findViewById(R.id.et_desc);
        madeIn = (EditText)rootView.findViewById(R.id.et_madein);
        tdd = (CheckBox)rootView.findViewById(R.id.time_definite_delivery);
        sd = (CheckBox)rootView.findViewById(R.id.saturday_delivery);
        pdso = (CheckBox)rootView.findViewById(R.id.perfic_delivery);
        hpl = (CheckBox)rootView.findViewById(R.id.hold_at_perfic);
        total_weight = (TextInputEditText)rootView.findViewById(R.id.total_weight);


        height.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int l = Integer.parseInt(length.getText().toString());
                int w = Integer.parseInt(width.getText().toString());
                int h = Integer.parseInt(height.getText().toString());
                if(height.getText().toString().trim().length()>0){
                    tot_wei = l*w*h;
                    weight.setText(String.valueOf(tot_wei));
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        showSetpView0();
        String[] items = new String[]{"Service Type", "By Air", "By Road"};
        String[] items1 = new String[]{"Packaging Type", "Paperboard boxes", "Corrugated boxes","Plastic boxes",
        "Rigid boxes", "Chipboard packaging"};
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
        service_type.setAdapter(adapter);
        service_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                service_type_selectedValue = adapterView.getItemAtPosition(i).toString();
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
        packaging_type.setAdapter(adapter1);
        packaging_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                packaging_type_selectedValue = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        return rootView;
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
        stepView.go(2, true);
        rootView.findViewById(R.id.next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> sp_handlingList = new ArrayList<String>();
                if(tdd.isChecked()){
                    sp_handlingList.add("category_id=1");
                }
                if(sd.isChecked()){
                    sp_handlingList.add("category_id=2");
                }
                if(pdso.isChecked()){
                    sp_handlingList.add("category_id=3");
                }
                if(hpl.isChecked()){
                    sp_handlingList.add("category_id=4");
                }
                String[] sp_handling = sp_handlingList.toArray(new String[sp_handlingList.size()]);

                if(service_type_selectedValue.equals("By Air")) {
                    vol_wei = Float.parseFloat(String.valueOf(tot_wei / 5000.0));
                }
                else if(service_type_selectedValue.equals("By Road")){
                    vol_wei = Float.parseFloat(String.valueOf(tot_wei / 4000.0));
                }
                    Log.v(String.valueOf(vol_wei), "vw");
                    isValid = SetValidation();
                    if(isValid) {
                        SharedPreferences preferences = getActivity().getSharedPreferences("Shipping_Prefs", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("Packaging Type",packaging_type_selectedValue);
                        editor.putString("Service Type",service_type_selectedValue);
                        editor.putString("Total Weight",total_weight.getText().toString());
                        editor.putString("Length",length.getText().toString());
                        editor.putString("Width",width.getText().toString());
                        editor.putString("Height",height.getText().toString());
                        editor.putString("Weight",weight.getText().toString());
                        editor.putString("Code",code.getText().toString());
                        editor.putString("Desc",desc.getText().toString());
                        editor.putString("MadeIn",madeIn.getText().toString());
                        editor.putString("Special Handling", Arrays.toString(sp_handling));
                        editor.apply();
                        String backStateName = this.getClass().getName();
                        AppCompatActivity activity = (AppCompatActivity) rootView.getContext();
                        Fragment fragment = new TotalCostFragment();
                        Bundle bundle = new Bundle();
                        bundle.putBoolean("Boolean", true);
                        bundle.putFloat("Volumetric Weight", vol_wei);
                        bundle.putFloat("Total Weight", Float.parseFloat(total_weight.getText().toString()));
                        bundle.putString("ServiceType", service_type_selectedValue);
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
        rootView.findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String backStateName = this.getClass().getName();
                AppCompatActivity activity = (AppCompatActivity) rootView.getContext();
                Fragment fragment = new RecipientInfoFragment();
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
        });
    }

    private boolean SetValidation() {

        if (packaging_type.getSelectedItem().toString().trim().equals("Packaging Type")) {
            Toast.makeText(getContext(), "Choose one of Packaging type!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (service_type.getSelectedItem().toString().trim().equals("Service Type")) {
            Toast.makeText(getContext(), "Choose one of Service type!", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Check for a valid company name
        if (total_weight.getText().toString().isEmpty()) {
            total_weight.setError(getResources().getString(R.string.totalweight_error));
            return false;
        }

        // Check for a valid name
        if (length.getText().toString().isEmpty()) {
            length.setError(getResources().getString(R.string.length));
            return false;
        }

        if (width.getText().toString().isEmpty()) {
            width.setError(getResources().getString(R.string.width));
            return false;
        }

        if (height.getText().toString().isEmpty()) {
            height.setError(getResources().getString(R.string.height));
            return false;
        }

        if (weight.getText().toString().isEmpty()) {
            weight.setError(getResources().getString(R.string.weight));
            return false;
        }

        if (code.getText().toString().isEmpty()) {
            code.setError(getResources().getString(R.string.code));
            return false;
        }

        if (desc.getText().toString().isEmpty()) {
            desc.setError(getResources().getString(R.string.desc));
            return false;
        }

        if (madeIn.getText().toString().isEmpty()) {
            madeIn.setError(getResources().getString(R.string.madeIn));
            return false;
        }

        if(!(tdd.isChecked() || sd.isChecked() || pdso.isChecked() || hpl.isChecked())){
            Toast.makeText(getContext(),"Select one of the options!",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

}