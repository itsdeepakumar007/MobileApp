package com.pafex.zscs;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class PincodeAdapter extends RecyclerView.Adapter
{
    List<Pincode> pincodeList;

    public PincodeAdapter(List<Pincode> pincodeList)
    {
        this.pincodeList = pincodeList;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType)
    {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.pincode_rvitem, viewGroup, false);

        return new RowViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        RowViewHolder rowViewHolder = (RowViewHolder) holder;

        int rowPos = rowViewHolder.getAdapterPosition();

        if (rowPos == 0) {
            // Header Cells. Main Headings appear here
            rowViewHolder.txtBranchName.setBackgroundResource(R.drawable.table_header_cell_bg);
            rowViewHolder.txtPincode.setBackgroundResource(R.drawable.table_header_cell_bg);
            rowViewHolder.txtCity.setBackgroundResource(R.drawable.table_header_cell_bg);
            rowViewHolder.txtState.setBackgroundResource(R.drawable.table_header_cell_bg);
            rowViewHolder.txtCountry.setBackgroundResource(R.drawable.table_header_cell_bg);
            rowViewHolder.txtDistrictName.setBackgroundResource(R.drawable.table_header_cell_bg);
            rowViewHolder.txtTDD.setBackgroundResource(R.drawable.table_header_cell_bg);

            rowViewHolder.txtBranchName.setText("Branch Name");
            rowViewHolder.txtPincode.setText("Pincode");
            rowViewHolder.txtCity.setText("City");
            rowViewHolder.txtState.setText("State");
            rowViewHolder.txtCountry.setText("Country");
            rowViewHolder.txtDistrictName.setText("District Name");
            rowViewHolder.txtTDD.setText("TDD");

            rowViewHolder.hand_submit.setVisibility(View.GONE);
        } else {
            Pincode pincode = pincodeList.get(rowPos-1);

            // Content Cells. Content appear here

            rowViewHolder.txtBranchName.setBackgroundResource(R.drawable.border_table);
            rowViewHolder.txtPincode.setBackgroundResource(R.drawable.border_table);
            rowViewHolder.txtCity.setBackgroundResource(R.drawable.border_table);
            rowViewHolder.txtState.setBackgroundResource(R.drawable.border_table);
            rowViewHolder.txtCountry.setBackgroundResource(R.drawable.border_table);
            rowViewHolder.txtDistrictName.setBackgroundResource(R.drawable.border_table);
            rowViewHolder.txtTDD.setBackgroundResource(R.drawable.border_table);

            rowViewHolder.txtBranchName.setTextColor(Color.parseColor("#000000"));
            rowViewHolder.txtPincode.setTextColor(Color.parseColor("#000000"));
            rowViewHolder.txtCity.setTextColor(Color.parseColor("#000000"));
            rowViewHolder.txtState.setTextColor(Color.parseColor("#000000"));
            rowViewHolder.txtCountry.setTextColor(Color.parseColor("#000000"));
            rowViewHolder.txtDistrictName.setTextColor(Color.parseColor("#000000"));
            rowViewHolder.txtTDD.setTextColor(Color.parseColor("#000000"));

            rowViewHolder.txtBranchName.setText(pincode.getBranch_name());
            rowViewHolder.txtPincode.setText(pincode.getPincode());
            rowViewHolder.txtCity.setText(pincode.getCity());
            rowViewHolder.txtState.setText(pincode.getState());
            rowViewHolder.txtCountry.setText(pincode.getCountry());
            rowViewHolder.txtDistrictName.setText(pincode.getDistrict_name());
            rowViewHolder.txtTDD.setText(pincode.getTdd());
        }
    }

    @Override
    public int getItemCount()
    {
        return pincodeList.size()+1;
    }


    public void setFilter(List<Pincode> pincodefilteredList){
        pincodeList = new ArrayList<>();
        pincodeList.addAll(pincodefilteredList);
        notifyDataSetChanged();
    }

    public class RowViewHolder extends RecyclerView.ViewHolder
    {
        protected TextView txtBranchName, txtTDD, txtPincode, txtCity, txtState, txtDistrictName, txtCountry;
        public ImageView hand_submit;

        public RowViewHolder(View view)
        {
            super(view);
            txtBranchName = view.findViewById(R.id.txtBranchName);
            txtPincode = view.findViewById(R.id.txtPincode);
            txtCity = view.findViewById(R.id.txtCity);
            txtState = view.findViewById(R.id.txtState);
            txtCountry = view.findViewById(R.id.txtCountry);
            txtDistrictName = view.findViewById(R.id.txtDistrictName);
            txtTDD = view.findViewById(R.id.txtTDD);
            hand_submit = view.findViewById(R.id.hand_submit);
            hand_submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String city = txtCity.getText().toString();
                    String state = txtState.getText().toString();
                    String pincode = txtPincode.getText().toString();
                    String backStateName = this.getClass().getName();
                    AppCompatActivity activity = (AppCompatActivity) view.getContext();
                    Fragment fragment = new ShipperInfoFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("city",city);
                    bundle.putString("state",state);
                    bundle.putString("pincode",pincode);
                    bundle.putBoolean("Boolean_Value", false);
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
            });
        }
    }

}