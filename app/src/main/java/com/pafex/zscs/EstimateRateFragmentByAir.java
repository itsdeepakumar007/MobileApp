package com.pafex.zscs;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EstimateRateFragmentByAir#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EstimateRateFragmentByAir extends Fragment implements AdapterView.OnItemClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View rootView;
    RecyclerView mRecyclerView;
    private RecyclerAdapter mAdapter;
    private float vol_weig, total_weight;
    private String service_type;

    public EstimateRateFragmentByAir() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EstimateRateFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EstimateRateFragmentByAir newInstance(String param1, String param2) {
        EstimateRateFragmentByAir fragment = new EstimateRateFragmentByAir();
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
        rootView = inflater.inflate(R.layout.fragment_estimate_rate_by_air, container, false);
        mRecyclerView=rootView.findViewById(R.id.mRecylcerview);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            vol_weig = bundle.getFloat("Volumetric Weight1",0);
            total_weight = bundle.getFloat("Total Weight1",0);
            service_type = bundle.getString("ServiceType1","");

        }
        mAdapter = new RecyclerAdapter(getContext(), getData());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
        return rootView;
    }

    private ArrayList<ServiceDetail> getData()
    {
        ArrayList<ServiceDetail> galaxies=new ArrayList<>();
        ServiceDetail g=new ServiceDetail("0.5 Kg", 297.2, R.drawable.aircon);
        galaxies.add(g);

        g=new ServiceDetail("1 Kg", 397.2, R.drawable.aircon);
        galaxies.add(g);

        g=new ServiceDetail("1.5 Kg", 497.2, R.drawable.aircon);
        galaxies.add(g);

        return galaxies;
    }

    /**
     * Let's create a method to open our detail activity and pass our object.
     * @param ServiceDetail
     */
    private void openDetailActivity(ServiceDetail ServiceDetail)
    {
        float rto_charge = Float.parseFloat(String.valueOf(ServiceDetail.getRto_charge()));
        String backStateName = this.getClass().getName();
        AppCompatActivity activity = (AppCompatActivity) rootView.getContext();
        Fragment fragment = new TotalCostFragment();
        Bundle bundle = new Bundle();
        bundle.putFloat("rto_charge", rto_charge);
        bundle.putBoolean("Boolean",false);
        bundle.putFloat("Volumetric Weight2", vol_weig);
        bundle.putFloat("Total Weight2", total_weight);
        bundle.putString("ServiceType2",service_type);
        fragment.setArguments(bundle);
        Log.v(String.valueOf(vol_weig),"volwe");
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        boolean fragmentPopped = fragmentManager.popBackStackImmediate (backStateName, 0);
        if(!fragmentPopped) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame, fragment);
            fragmentTransaction.addToBackStack(backStateName);
            fragmentTransaction.commit();
        }
    }

    /**
     * Let's implemenent our onItemClick method
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ServiceDetail g=getData().get(position);
        openDetailActivity(g);
    }

}