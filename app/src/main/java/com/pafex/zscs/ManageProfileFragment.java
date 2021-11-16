package com.pafex.zscs;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ManageProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ManageProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextInputEditText name, mobile_no, dob, pan_no, passport_no, total_exp, email_id, designation, address,
    doj, aadhar_no, primary_skill, reporting_manager;
    private Spinner head_company, invoice, acc_type;
    private String head_company_selectedValue, invoice_selectedValue, acc_type_selectedValue;
    TextView role_tv, branch_company;
    private String reg_name,reg_email_id,reg_company_name,reg_mobile;
    boolean[] selectedRole, selectedBranch;
    ArrayList<Integer> langList, langList1;
    String[] langArray = {"Admin", "Manager", "Supervisor", "Customer Executive", "Data Entry", "Delivery Executive",
            "Pickup Executive", "Sales Executive", "Account Executive"};

    String[] langArray1 = {"PRG", "ZSCS"};
    private Button submit_btn;
    private Boolean isValid;
    private Calendar myCalendar,myCalendar1;
    private Connection connect;

    public ManageProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ManageProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ManageProfileFragment newInstance(String param1, String param2) {
        ManageProfileFragment fragment = new ManageProfileFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_manage_profile, container, false);
        name = (TextInputEditText)rootView.findViewById(R.id.name);
        mobile_no = (TextInputEditText)rootView.findViewById(R.id.mobile_no);
        dob = (TextInputEditText)rootView.findViewById(R.id.dob);
        pan_no = (TextInputEditText)rootView.findViewById(R.id.pan_no);
        passport_no = (TextInputEditText)rootView.findViewById(R.id.passport_no);
        total_exp = (TextInputEditText)rootView.findViewById(R.id.total_exp);
        email_id = (TextInputEditText)rootView.findViewById(R.id.email_id);
        designation = (TextInputEditText)rootView.findViewById(R.id.designation);
        address = (TextInputEditText)rootView.findViewById(R.id.address);
        doj = (TextInputEditText)rootView.findViewById(R.id.doj);
        aadhar_no = (TextInputEditText)rootView.findViewById(R.id.aadhar_no);
        primary_skill = (TextInputEditText)rootView.findViewById(R.id.skill);
        reporting_manager = (TextInputEditText)rootView.findViewById(R.id.reporting_manager);
        head_company = (Spinner)rootView.findViewById(R.id.head_company);
        role_tv = (TextView)rootView.findViewById(R.id.role_tv);
        invoice = (Spinner)rootView.findViewById(R.id.invoice);
        branch_company = (TextView)rootView.findViewById(R.id.branch_company);
        acc_type = (Spinner)rootView.findViewById(R.id.account_type);
        submit_btn = (Button)rootView.findViewById(R.id.submit_btn);
        langList = new ArrayList<>();
        langList1 = new ArrayList<>();
        myCalendar = Calendar.getInstance();
        myCalendar1 = Calendar.getInstance();

        SharedPreferences prefs = getContext().getSharedPreferences("Save Register Data", MODE_PRIVATE);
        reg_name = prefs.getString("reg_name", null);
        reg_email_id = prefs.getString("reg_email", null);
        reg_company_name = prefs.getString("reg_company_name", null);
        reg_mobile = prefs.getString("reg_mobile_no", null);
        name.setText(reg_name);
        email_id.setText(reg_email_id);
        mobile_no.setText(reg_mobile);

        String[] items = new String[]{"Select Head Company", "ZSCS", "PRG"};
        String[] items1 = new String[]{"Select Invoice Type", "Invoice1", "Invoice2", "Invoice3"};
        String[] items2 = new String[]{"Select Account Type", "Demo", "ZSCS"};
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
        head_company.setAdapter(adapter);
        head_company.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                head_company_selectedValue = adapterView.getItemAtPosition(i).toString();

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
        invoice.setAdapter(adapter1);
        invoice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                /*invoice_selectedValue = adapterView.getItemAtPosition(i).toString();*/
                if (invoice.getSelectedItem().toString().trim().equals("Invoice1")){
                    invoice_selectedValue = "1";
                }
                else if (invoice.getSelectedItem().toString().trim().equals("Invoice2")){
                    invoice_selectedValue = "2";
                }
                else if (invoice.getSelectedItem().toString().trim().equals("Invoice3")){
                    invoice_selectedValue = "3";
                }
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
        acc_type.setAdapter(adapter2);
        acc_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                acc_type_selectedValue = adapterView.getItemAtPosition(i).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        final DatePickerDialog.OnDateSetListener date_dob = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                myCalendar.set(Calendar.YEAR,y);
                myCalendar.set(Calendar.MONTH,m);
                myCalendar.set(Calendar.DAY_OF_MONTH,d);
                updateDate();
            }
        };

        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getContext(), date_dob, myCalendar.
                        get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        final DatePickerDialog.OnDateSetListener date_doj = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                myCalendar1.set(Calendar.YEAR,y);
                myCalendar1.set(Calendar.MONTH,m);
                myCalendar1.set(Calendar.DAY_OF_MONTH,d);
                updateDate1();
            }
        };


        doj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getContext(), date_doj, myCalendar1.
                        get(Calendar.YEAR), myCalendar1.get(Calendar.MONTH),
                        myCalendar1.get(Calendar.DAY_OF_MONTH)).show();
            }
        });



        SelectedRole();
        SelectedBranchCompany();
        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isValid = SetValidation();
                if(isValid){
                    new ManageProfileFragment.submitDetails().execute("");
                }
            }
        });

        return rootView;
    }

    private void updateDate() {
        String myFormat = "yyyy-MM-dd"; //put your date format in which you need to display
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);

        dob.setText(sdf.format(myCalendar.getTime()));
    }

    private void updateDate1() {
        String myFormat = "yyyy-MM-dd"; //put your date format in which you need to display
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);

        doj.setText(sdf.format(myCalendar1.getTime()));
    }



    private boolean SetValidation() {

        if (head_company.getSelectedItem().toString().trim().equals("Select Head Company")) {
            Toast.makeText(getContext(), "Choose Head Company!", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Check for a valid company name
/*
        if (role_tv.getText().toString().isEmpty()) {
            role_tv.setError(getResources().getString(R.string.role_error));
            return false;
        }
*/

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

        if (dob.getText().toString().isEmpty()) {
            dob.setError(getResources().getString(R.string.dob));
            return false;
        }

        if (pan_no.getText().toString().isEmpty()) {
            pan_no.setError(getResources().getString(R.string.number));
            return false;
        }

        if (passport_no.getText().toString().isEmpty()) {
            passport_no.setError(getResources().getString(R.string.number));
            return false;
        }

        if (total_exp.getText().toString().isEmpty()) {
            total_exp.setError(getResources().getString(R.string.exp_error));
            return false;
        }

        if (invoice.getSelectedItem().toString().trim().equals("Select Invoice Type")) {
            Toast.makeText(getContext(), "Choose one of Invoices!", Toast.LENGTH_SHORT).show();
            return false;
        }

/*
        if (branch_company.getText().toString().isEmpty()) {
            branch_company.setError(getResources().getString(R.string.branch_error));
            return false;
        }
*/

        if (email_id.getText().toString().isEmpty()) {
            email_id.setError(getResources().getString(R.string.email_error));
            return false;
        }

        if (designation.getText().toString().isEmpty()) {
            designation.setError(getResources().getString(R.string.designtaion_error));
            return false;
        }

        if (address.getText().toString().isEmpty()) {
            address.setError(getResources().getString(R.string.address_error));
            return false;
        }

        if (doj.getText().toString().isEmpty()) {
            doj.setError(getResources().getString(R.string.dob));
            return false;
        }

        if (aadhar_no.getText().toString().isEmpty()) {
            aadhar_no.setError(getResources().getString(R.string.number));
            return false;
        }

        if (primary_skill.getText().toString().isEmpty()) {
            primary_skill.setError(getResources().getString(R.string.skill_error));
            return false;
        }

        if (acc_type.getSelectedItem().toString().trim().equals("Select Account Type")) {
            Toast.makeText(getContext(), "Choose one of account type!", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
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

                    String sql = "UPDATE Users SET UserName = '" + name.getText() +"',EmailId = '" + email_id.getText() +"'," +
                            "RoleCode = '" + langList +"',FullAddress = '" + address.getText() +"'," +
                            "ContactNo = '" + mobile_no.getText() +"',Alt_ContactNo = '" + mobile_no.getText() +"'," +
                            "DateOfBirth = '" + dob.getText() +"',DateOfJoin = '" + doj.getText() +"'," +
                            "AadharNumber = '" + aadhar_no.getText() +"',PanNumber = '" + pan_no.getText() + "'," +
                            "PassportNumber = '" + passport_no.getText() + "',Role = '" + role_tv.getText() +"'," +
                            "Designation = '" + designation.getText() +"',TotalExperience = '" + total_exp.getText() +"'," +
                            "PrimarySkill = '" + primary_skill.getText() +"',HeadCompany = '" + head_company_selectedValue +"'," +
                            "BranchCompany = '" + branch_company.getText() +"',ReportingManager = '" + reporting_manager.getText() +"'," +
                            "InvoiceNumber = '" + invoice_selectedValue +"',AccountType = '" + acc_type_selectedValue + "' " +
                            "WHERE EmailId = '" + reg_email_id + "'";

                    PreparedStatement statement = connect.prepareStatement(sql);
                    statement.executeUpdate();
                    statement.close();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(), "Your profile is updated successfully!",Toast.LENGTH_SHORT).show();
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



    private void SelectedBranchCompany() {
        selectedBranch = new boolean[langArray1.length];
        branch_company.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Select Branch Company");
                builder.setCancelable(false);
                builder.setMultiChoiceItems(langArray1, selectedBranch, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                        if(b){
                            langList1.add(i);
                            Collections.sort(langList1);
                        }
                        else if(langList1.contains(i)){
                            langList1.remove(langList1.indexOf(i));
                        }
                    }
                });
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        StringBuilder stringBuilder = new StringBuilder();
                        for(int j=0; j<langList1.size(); j++){
                            stringBuilder.append(langArray1[langList1.get(j)]);
                            if(j!=langList1.size() - 1){
                                stringBuilder.append(", ");
                            }
                        }
                        branch_company.setText(stringBuilder.toString());
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // dismiss dialog
                        dialogInterface.dismiss();
                    }
                });
                builder.setNeutralButton("Clear All", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // use for loop
                        for (int j = 0; j < selectedBranch.length; j++) {
                            // remove all selection
                            selectedBranch[j] = false;
                            // clear language list
                            langList1.clear();
                            // clear text view value
                            branch_company.setText("");
                        }
                    }
                });
                builder.show();
            }
        });
    }

    public void SelectedRole() {
        selectedRole = new boolean[langArray.length];
        role_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Select Role");
                builder.setCancelable(false);
                builder.setMultiChoiceItems(langArray, selectedRole, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                        if(b){
                            langList.add(i);
                            Collections.sort(langList);
                        }
                        else if(langList.contains(i)){
                            langList.remove(langList.indexOf(i));
                        }
                    }
                });
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        StringBuilder stringBuilder = new StringBuilder();
                        for(int j=0; j<langList.size(); j++){
                            stringBuilder.append(langArray[langList.get(j)]);
                            if(j!=langList.size() - 1){
                                stringBuilder.append(", ");
                            }
                        }
                        role_tv.setText(stringBuilder.toString());
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // dismiss dialog
                        dialogInterface.dismiss();
                    }
                });
                builder.setNeutralButton("Clear All", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // use for loop
                        for (int j = 0; j < selectedRole.length; j++) {
                            // remove all selection
                            selectedRole[j] = false;
                            // clear language list
                            langList.clear();
                            // clear text view value
                            role_tv.setText("");
                        }
                    }
                });
                builder.show();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if(getActivity()!=null){
            ((MainActivity)getActivity()).getSupportActionBar().setTitle("Manage Profile");
        }
    }
}