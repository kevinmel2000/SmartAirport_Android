package com.laurensius_dede_suhardiman.smartairport.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.laurensius_dede_suhardiman.smartairport.R;
import com.laurensius_dede_suhardiman.smartairport.model.BaggageTracking;

import java.util.List;

public class BaggageTrackingAdapter extends RecyclerView.Adapter<BaggageTrackingAdapter.HolderBaggageTracking> {
    List<BaggageTracking> listBaggageTracking;

    public BaggageTrackingAdapter(List<BaggageTracking> listBaggageTracking){
        this.listBaggageTracking = listBaggageTracking;
    }

    @Override
    public HolderBaggageTracking onCreateViewHolder(ViewGroup viewGroup, int i){
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_baggage_tracking,viewGroup,false);
        HolderBaggageTracking holderBaggageTracking = new HolderBaggageTracking(v);
        return holderBaggageTracking;
    }

    @Override
    public void onBindViewHolder(HolderBaggageTracking holderBaggageTracking,final int i){
        holderBaggageTracking.tvDatetime.setText(listBaggageTracking.get(i).getDatetime());
        holderBaggageTracking.tvStatus.setText(listBaggageTracking.get(i).getStatus());
    }

    @Override
    public int getItemCount(){
        return listBaggageTracking.size();
    }

    public BaggageTracking getItem(int position){
        return listBaggageTracking.get(position);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView){
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class HolderBaggageTracking extends  RecyclerView.ViewHolder{
        CardView cvBaggageTracking;
        TextView tvDatetime,tvStatus;

        HolderBaggageTracking(View itemView){
            super(itemView);
            cvBaggageTracking = (CardView) itemView.findViewById(R.id.cv_baggage_tracking);
            tvDatetime = (TextView)itemView.findViewById(R.id.tv_datetime);
            tvStatus = (TextView)itemView.findViewById(R.id.tv_status);
        }
    }
}