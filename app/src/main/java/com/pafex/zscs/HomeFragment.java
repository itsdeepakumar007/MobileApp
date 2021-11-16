package com.pafex.zscs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private ArrayList<RecyclerData> recyclerDataArrayList;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BlankFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.idgridRV);

        recyclerDataArrayList = new ArrayList<>();
        recyclerDataArrayList.add(new RecyclerData("PICKUP DETAILS",R.drawable.pickup_icon));
        recyclerDataArrayList.add(new RecyclerData("SHIPPING",R.drawable.shipping_icon));
        recyclerDataArrayList.add(new RecyclerData("INTERNATIONAL SHIPPING",R.drawable.international_shipping_icon));
        recyclerDataArrayList.add(new RecyclerData("SHIPPING RATE AND DELIVERY TIME",R.drawable.deliverytime_icon));
        recyclerDataArrayList.add(new RecyclerData("TRACKING",R.drawable.order_tracking));
        recyclerDataArrayList.add(new RecyclerData("MANAGE PROFILE",R.drawable.manage_profile_icon));

        RecyclerViewAdapter adapter=new RecyclerViewAdapter(recyclerDataArrayList,getContext());

        // setting grid layout manager to implement grid view.
        // in this method '2' represents number of columns to be displayed in grid view.
        GridLayoutManager layoutManager=new GridLayoutManager(getContext(),2);

        // at last set adapter to recycler view.
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);


        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(getActivity()!=null){
            ((MainActivity)getActivity()).getSupportActionBar().setTitle("Home");
        }

    }
}