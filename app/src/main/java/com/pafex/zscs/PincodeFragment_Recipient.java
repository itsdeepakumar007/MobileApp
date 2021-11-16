package com.pafex.zscs;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PincodeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PincodeFragment_Recipient extends Fragment implements SearchView.OnQueryTextListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View rootView;
    private TextView recyclableTextView;
    private ImageView imageView;
    int scrollX = 0;
    private TextView txtBranchName, txtTDD, txtPincode, txtCity, txtState, txtDistrictName, txtCountry;

    RecyclerView rvPincode;

    HorizontalScrollView headerScroll;

    SearchView searchView;

    PincodeAdapter pincodeAdapter;
    private String pincode;
    private Connection connect;
    private boolean isSuccess;
    private String branch_name, pincode1,city,state,country,district,tdd ;
    private ArrayList<Pincode> pincodeList;
    private RecyclerView recyclerView;
    private PincodeAdapter_Recipient adapter;
    private TextView textView;

    public PincodeFragment_Recipient() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PincodeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PincodeFragment newInstance(String param1, String param2) {
        PincodeFragment fragment = new PincodeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_pincode__recipient, container, false);

        Bundle bundle = this.getArguments();
        pincode = bundle.getString("pincode","");
        textView = rootView.findViewById(R.id.textViewTitle);
        recyclerView = rootView.findViewById(R.id.recyclerViewDeliveryProductList);
        adapter = new PincodeAdapter_Recipient(getPincodeList());

        getActivity().getOnBackPressedDispatcher().addCallback(getActivity(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (!searchView.isIconified()) {
                    searchView.onActionViewCollapsed();

                } else {
                    String backStateName = this.getClass().getName();
                    getActivity().getSupportFragmentManager().popBackStack(backStateName, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                }
            }
        });


        return rootView;
    }

    private List<Pincode> getPincodeList() {
        pincodeList = new ArrayList<>();

        new PincodeFragment_Recipient.pincodeGo().execute("");

        return pincodeList;
    }



    public class pincodeGo extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings) {
            String z = null;
            isSuccess = false;

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

                    String sql = "SELECT a.BranchName,a.BranchPincode,a.BranchTaluk,a.BranchStateName," +
                            "a.BranchCountry,a.BranchDistrictname,b.TDD FROM PostCodeList a INNER JOIN PincodeMaster b ON " +
                            "a.BranchPincode = b.PINCODE WHERE a.BranchPincode = '" + pincode +"' ";

                    Statement statement = connect.createStatement();
                    ResultSet rs = statement.executeQuery(sql);
                    if(rs!=null){
                        while (rs.next()){
                            try{
                                pincodeList.add(new Pincode(rs.getString("BranchName"),rs.getString("BranchPincode"),
                                        rs.getString("BranchTaluk"),rs.getString("BranchStateName"),
                                        rs.getString("BranchCountry"),rs.getString("BranchDistrictname"),
                                        rs.getString("TDD")));
                                Log.v(district,"rs");

                            }
                            catch (Exception e){
                                e.printStackTrace();
                            }
                        }

                        z = "Found";
                        isSuccess = true;
                    }
                    else {
                        z = "Not Found";
                        isSuccess = false;
                    }

                }



            } catch (Exception e) {
                e.printStackTrace();
                Writer writer = new StringWriter();
                e.printStackTrace(new PrintWriter(writer));
                z = writer.toString();
                isSuccess = false;
            }
            return z;
        }

        @Override
        protected void onPostExecute(String s) {
            if(!isSuccess)
            {}
            else{
                try {


                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                    recyclerView.setLayoutManager(linearLayoutManager);

                    recyclerView.setAdapter(adapter);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);


        item.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                // Do something when collapsed
                adapter.setFilter(pincodeList);
                return true; // Return true to collapse action view
            }

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                // Do something when expanded
                return true; // Return true to expand action view
            }
        });
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        final List<Pincode> filteredModelList = filter(pincodeList, newText);
        adapter.setFilter(filteredModelList);
        return true;
    }

    private List<Pincode> filter(List<Pincode> models, String query) {
        query = query.toLowerCase();

        final List<Pincode> filteredModelList = new ArrayList<>();
        for (Pincode model : models) {
            final String text = model.getBranch_name().toLowerCase();
            if (text.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }

}