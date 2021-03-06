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
import com.laurensius_dede_suhardiman.smartairport.model.Tourism;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TourismAdapter extends RecyclerView.Adapter<TourismAdapter.HolderTourism> {
    List<Tourism> listTourism;
    Context ctx;

    public TourismAdapter(List<Tourism> listTourism, Context ctx){
        this.listTourism = listTourism;
        this.ctx = ctx;
    }

    @Override
    public HolderTourism onCreateViewHolder(ViewGroup viewGroup, int i){
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_tourism,viewGroup,false);
        HolderTourism holderTourism = new HolderTourism(v);
        return holderTourism;
    }

    @Override
    public void onBindViewHolder(HolderTourism holderTourism,int i){
        Log.d("Debug gambar" , listTourism.get(i).getImage());
        String url = ctx.getResources().getString(R.string.url_tourism_img)
                .concat(listTourism.get(i).getCity_district().toLowerCase() + "/").concat(listTourism.get(i).getImage());
        Picasso.get().load(url).into(holderTourism.ivImage);
        holderTourism.tvName.setText(listTourism.get(i).getName());
        holderTourism.tvCityDistrict.setText("Location : "+ listTourism.get(i).getCity_district());
        holderTourism.tvCategory.setText("Category : " + listTourism.get(i).getCategory());
    }

    @Override
    public int getItemCount(){
        return listTourism.size();
    }

    public Tourism getItem(int position){
        return listTourism.get(position);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView){
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class HolderTourism extends  RecyclerView.ViewHolder{
        CardView cvTourism;
        ImageView ivImage;
        TextView tvName,tvCityDistrict,tvCategory;

        HolderTourism(View itemView){
            super(itemView);
            cvTourism = (CardView) itemView.findViewById(R.id.cv_tourism);
            ivImage = (ImageView)itemView.findViewById(R.id.iv_image);
            tvName = (TextView)itemView.findViewById(R.id.tv_name);
            tvCityDistrict = (TextView)itemView.findViewById(R.id.tv_city_district);
            tvCategory = (TextView)itemView.findViewById(R.id.tv_category);
        }
    }

}