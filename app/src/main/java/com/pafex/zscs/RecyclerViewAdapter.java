package com.pafex.zscs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder> {

    private ArrayList<RecyclerData> DataArrayList;
    private Context mcontext;

    public RecyclerViewAdapter(ArrayList<RecyclerData> recyclerDataArrayList, Context mcontext) {
        this.DataArrayList = recyclerDataArrayList;
        this.mcontext = mcontext;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate Layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, @SuppressLint("RecyclerView") int position) {
        // Set the data to textview and imageview.
        RecyclerData recyclerData = DataArrayList.get(position);
        holder.gridTV.setText(recyclerData.getTitle());
        holder.gridIV.setImageResource(recyclerData.getImgid());
        holder.gridCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(position == 0)
                {
                    String backStateName = this.getClass().getName();
                    AppCompatActivity activity = (AppCompatActivity) view.getContext();
                    Fragment fragment = new PickUpFragment();
                    FragmentManager fragmentManager = activity.getSupportFragmentManager();
                    boolean fragmentPopped = fragmentManager.popBackStackImmediate (backStateName, 0);
                    if(!fragmentPopped) {
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.frame, fragment);
                        fragmentTransaction.addToBackStack(backStateName);
                        fragmentTransaction.commit();
                    }
                }
                else if(position == 1){
                    String backStateName = this.getClass().getName();
                    AppCompatActivity activity = (AppCompatActivity) view.getContext();
                    Fragment fragment = new ShipperInfoFragment();
                    Bundle args = new Bundle();
                    args.putBoolean("Boolean_Value",true);
                    fragment.setArguments(args);
                    FragmentManager fragmentManager = activity.getSupportFragmentManager();
                    boolean fragmentPopped = fragmentManager.popBackStackImmediate (backStateName, 0);
                    if(!fragmentPopped) {
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.frame, fragment);
                        fragmentTransaction.addToBackStack("shipperInfoFragment");
                        fragmentTransaction.commit();
                    }
                }
                else if(position == 5){
                    String backStateName = this.getClass().getName();
                    AppCompatActivity activity = (AppCompatActivity) view.getContext();
                    Fragment fragment = new ManageProfileFragment();
                    FragmentManager fragmentManager = activity.getSupportFragmentManager();
                    boolean fragmentPopped = fragmentManager.popBackStackImmediate (backStateName, 0);
                    if(!fragmentPopped) {
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.frame, fragment);
                        fragmentTransaction.addToBackStack(backStateName);
                        fragmentTransaction.commit();
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        // this method returns the size of recyclerview
        return DataArrayList.size();
    }

    // View Holder Class to handle Recycler View.
    public class RecyclerViewHolder extends RecyclerView.ViewHolder {

        private TextView gridTV;
        private ImageView gridIV;
        private LinearLayout gridCard;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            gridTV = itemView.findViewById(R.id.idgrid);
            gridIV = itemView.findViewById(R.id.idgridIV);
            gridCard = itemView.findViewById(R.id.grid_card);
        }
    }
}

