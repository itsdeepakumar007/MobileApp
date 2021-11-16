package com.pafex.zscs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<
        RecyclerAdapter.RecyclerViewHolder> {

    /**
     * Let's start by defining our instance fields
     */
    private int selectedStarPosition = -1;
    private List<ServiceDetail> galaxies;
    private Context c;
    private AdapterView.OnItemClickListener onItemClickListener;

    /**
     * Let's create our constructor
     * @param context
     * @param mGalaxies
     */
    public RecyclerAdapter(Context context, List<ServiceDetail> mGalaxies) {
        this.c = context;
        this.galaxies = mGalaxies;
    }

    /**
     * OnCreateViewHolder - here is where we inflate our model layout
     * @param viewGroup
     * @param i
     * @return
     */
    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        final View v = LayoutInflater.from(c).inflate(R.layout.model, viewGroup, false);
        return new RecyclerViewHolder(v, this);
    }

    /**
     * OnBindViewHolder - Here's where we bind our data
     * @param viewHolder
     * @param position
     */
    @Override
    public void onBindViewHolder(RecyclerViewHolder viewHolder, final int position) {
        ServiceDetail galaxy = galaxies.get(position);
        try {
            viewHolder.bindData(galaxy, position);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * Let's return the number of items to be bound to our adapter.
     * @return
     */
    @Override
    public int getItemCount() {
        return galaxies.size();
    }

    /**
     * Let's receive our onItemClickListener and assign it to our local one.
     * @param onItemClickListener
     */
    public void setOnItemClickListener(AdapterView.OnItemClickListener
                                               onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    /**
     * When user clicks our itemView, we still invoke the onItemClick
     * @param holder
     */
    public void onItemHolderClick(RecyclerViewHolder holder) {
        if (onItemClickListener != null)
            onItemClickListener.onItemClick(null, holder.itemView,
                    holder.getAdapterPosition(), holder.getItemId());
    }

    /**
     * Let's come create our ViewHolder class.
     */
    class RecyclerViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {

        private RecyclerAdapter mAdapter;
        private RadioButton mRadioButton;
        private TextView tw_mMinWeigTxt,tv_rto;
        private ImageView mGalaxyImg;

        /**
         * In our constructor, we reference our itemView widgets.
         * @param itemView
         * @param mAdapter
         */
        public RecyclerViewHolder(View itemView, final RecyclerAdapter mAdapter) {
            super(itemView);
            this.mAdapter = mAdapter;

            tw_mMinWeigTxt = itemView.findViewById(R.id.tw_mMinWeigTxt);
            tv_rto = itemView.findViewById(R.id.tv_rto);
            mRadioButton = itemView.findViewById(R.id.mRadioButton);
            mGalaxyImg=itemView.findViewById(R.id.mGalaxyImageView);
            itemView.setOnClickListener(this);
            mRadioButton.setOnClickListener(this);
        }

        /**
         * Let's create a method that allows us bind our data.
         * @param galaxy
         * @param position
         */
        public void bindData(ServiceDetail galaxy, int position) {
            mRadioButton.setChecked(position == selectedStarPosition);
            tw_mMinWeigTxt.setText(galaxy.getMin_weight());
            tv_rto.setText(String.valueOf(galaxy.getRto_charge()));
            mGalaxyImg.setImageResource(galaxy.getImage());
        }

        /**
         * Let's override our OnClick method.
         * @param v
         */
        @Override
        public void onClick(View v) {
            selectedStarPosition = getAdapterPosition();
            notifyItemRangeChanged(0, galaxies.size());
            mAdapter.onItemHolderClick(RecyclerViewHolder.this);
        }
    }
}
