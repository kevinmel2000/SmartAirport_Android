package com.laurensius_dede_suhardiman.smartairport.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.laurensius_dede_suhardiman.smartairport.R;
import com.laurensius_dede_suhardiman.smartairport.model.ParkingArea;

import java.util.List;

public class ParkingAreaAdapter extends RecyclerView.Adapter<ParkingAreaAdapter.HolderParkingArea> {
    List<ParkingArea> listParkingArea;

    public ParkingAreaAdapter(List<ParkingArea> listParkingArea){
        this.listParkingArea = listParkingArea;
    }

    @Override
    public HolderParkingArea onCreateViewHolder(ViewGroup viewGroup, int i){
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_parking,viewGroup,false);
        HolderParkingArea holderParkingArea = new HolderParkingArea(v);
        return holderParkingArea;
    }

    @Override
    public void onBindViewHolder(HolderParkingArea holderParkingArea,int i){
        holderParkingArea.tvName.setText(listParkingArea.get(i).getName());
        holderParkingArea.tvStatusCar.setText(listParkingArea.get(i).getStatus_car());
        holderParkingArea.tvStatusMotorcycle.setText(listParkingArea.get(i).getStatus_motorcycle());
    }

    @Override
    public int getItemCount(){
        return listParkingArea.size();
    }

    public ParkingArea getItem(int position){
        return listParkingArea.get(position);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView){
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class HolderParkingArea extends  RecyclerView.ViewHolder{
        CardView cvParkingArea;
        ImageView ivImage;
        TextView tvName,tvStatusCar,tvStatusMotorcycle;

        HolderParkingArea(View itemView){
            super(itemView);
            cvParkingArea = (CardView) itemView.findViewById(R.id.cv_facility);
            ivImage = (ImageView)itemView.findViewById(R.id.iv_image);
            tvName = (TextView)itemView.findViewById(R.id.tv_name);
            tvStatusCar = (TextView)itemView.findViewById(R.id.tv_status_car);
            tvStatusMotorcycle = (TextView)itemView.findViewById(R.id.tv_status_motorcycle);
        }
    }
}