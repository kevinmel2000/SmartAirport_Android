package com.laurensius_dede_suhardiman.smartairport.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.laurensius_dede_suhardiman.smartairport.R;
import com.laurensius_dede_suhardiman.smartairport.model.Facility;
import com.laurensius_dede_suhardiman.smartairport.model.Route;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RouteAdapter extends RecyclerView.Adapter<RouteAdapter.HolderRoute> {
    List<Route> listRoute;

    public RouteAdapter(List<Route> listRoute){
        this.listRoute = listRoute;
    }

    @Override
    public HolderRoute onCreateViewHolder(ViewGroup viewGroup, int i){
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_route,viewGroup,false);
        HolderRoute holderRoute = new HolderRoute(v);
        return holderRoute;
    }

    @Override
    public void onBindViewHolder(HolderRoute holderRoute,int i){
        holderRoute.tvOriginIataIcao.setText(listRoute.get(i).getOrigin_iata());
        holderRoute.tvDestinationIataIcao.setText(listRoute.get(i).getDestination_iata());
        holderRoute.tvSchedule.setText(listRoute.get(i).getFlight_schedule());
        holderRoute.tvDuration.setText(listRoute.get(i).getFlight_duration_minutes());
        holderRoute.tvOrigin.setText(listRoute.get(i).getOrigin_name());
        holderRoute.tvDestination.setText(listRoute.get(i).getDestination_name());
        holderRoute.tvAirline.setText(listRoute.get(i).getAirlines());
    }

    @Override
    public int getItemCount(){
        return listRoute.size();
    }

    public Route getItem(int position){
        return listRoute.get(position);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView){
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class HolderRoute extends  RecyclerView.ViewHolder{
        CardView cvRoute;
        TextView tvOriginIataIcao,tvDestinationIataIcao;
        TextView tvSchedule,tvDuration,tvOrigin,tvDestination,tvAirline;
        Button btnBookNow;

        HolderRoute(View itemView){
            super(itemView);
            cvRoute = (CardView) itemView.findViewById(R.id.cv_route);
            tvOriginIataIcao = (TextView) itemView.findViewById(R.id.tv_origin_iata_icao);
            tvDestinationIataIcao = (TextView) itemView.findViewById(R.id.tv_destination_iata_icao);
            tvSchedule = (TextView)itemView.findViewById(R.id.tv_schedule);
            tvDuration = (TextView) itemView.findViewById(R.id.tv_duration);
            tvOrigin = (TextView) itemView.findViewById(R.id.tv_origin);
            tvDestination = (TextView) itemView.findViewById(R.id.tv_destination);
            tvAirline = (TextView) itemView.findViewById(R.id.tv_airline);
            btnBookNow = (Button)itemView.findViewById(R.id.btn_book_now);
        }
    }
}