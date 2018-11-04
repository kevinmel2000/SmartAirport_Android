package com.laurensius_dede_suhardiman.smartairport.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.laurensius_dede_suhardiman.smartairport.R;
import com.laurensius_dede_suhardiman.smartairport.model.PhoneDirectory;

import java.util.List;

public class PhoneDirectoryAdapter extends RecyclerView.Adapter<PhoneDirectoryAdapter.HolderSchedule> {
    List<PhoneDirectory> listPhoneDirectory;

    public PhoneDirectoryAdapter(List<PhoneDirectory> listPhoneDirectory){
        this.listPhoneDirectory = listPhoneDirectory;
    }

    @Override
    public HolderSchedule onCreateViewHolder(ViewGroup viewGroup, int i){
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_phone_dir,viewGroup,false);
        HolderSchedule holderSchedule = new HolderSchedule(v);
        return holderSchedule;
    }

    @Override
    public void onBindViewHolder(final HolderSchedule holderSchedule,int i){
        holderSchedule.tvName.setText(listPhoneDirectory.get(i).getName());
        holderSchedule.tvPhone.setText(listPhoneDirectory.get(i).getPhone());
    }

    @Override
    public int getItemCount(){
        return listPhoneDirectory.size();
    }

    public PhoneDirectory getItem(int position){
        return listPhoneDirectory.get(position);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView){
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class HolderSchedule extends  RecyclerView.ViewHolder{
        CardView cvPhoneDir;
        ImageView ivImage;
        TextView tvName,tvPhone;

        HolderSchedule(View itemView){
            super(itemView);
            cvPhoneDir = (CardView) itemView.findViewById(R.id.cv_phone_dir);
            ivImage = (ImageView)itemView.findViewById(R.id.iv_image);
            tvName = (TextView)itemView.findViewById(R.id.tv_name);
            tvPhone= (TextView)itemView.findViewById(R.id.tv_phone);
        }
    }
}