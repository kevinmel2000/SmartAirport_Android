package com.laurensius_dede_suhardiman.smartairport.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.laurensius_dede_suhardiman.smartairport.R;
import com.laurensius_dede_suhardiman.smartairport.model.Facility;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FacilityAdapter extends RecyclerView.Adapter<FacilityAdapter.HolderFacility> {
    List<Facility> listFacility;
    int origin_destination;

    public FacilityAdapter(List<Facility> listFacility, int origin_destination){
        this.listFacility = listFacility;
        this.origin_destination = origin_destination;
    }

    @Override
    public HolderFacility onCreateViewHolder(ViewGroup viewGroup, int i){
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_facility,viewGroup,false);
        HolderFacility holderFacility = new HolderFacility(v);
        return holderFacility;
    }

    @Override
    public void onBindViewHolder(HolderFacility holderFacility,int i){
        Picasso.get().load(listFacility.get(i).getImage()).into(holderFacility.ivImage);
        holderFacility.tvName.setText(listFacility.get(i).getName());
        holderFacility.tvDescription.setText(listFacility.get(i).getDescription());
        holderFacility.tvLocation.setText(listFacility.get(i).getLocation());
    }

    @Override
    public int getItemCount(){
        return listFacility.size();
    }

    public Facility getItem(int position){
        return listFacility.get(position);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView){
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class HolderFacility extends  RecyclerView.ViewHolder{
        CardView cvFacility;
        ImageView ivImage;
        TextView tvName,tvDescription,tvLocation;

        HolderFacility(View itemView){
            super(itemView);
            cvFacility = (CardView) itemView.findViewById(R.id.cv_facility);
            ivImage = (ImageView)itemView.findViewById(R.id.iv_image);
            tvName = (TextView)itemView.findViewById(R.id.tv_name);
            tvDescription = (TextView)itemView.findViewById(R.id.tv_description);
            tvLocation = (TextView)itemView.findViewById(R.id.tv_location);
        }
    }
}