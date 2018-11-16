package com.laurensius_dede_suhardiman.smartairport.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.laurensius_dede_suhardiman.smartairport.R;
import com.laurensius_dede_suhardiman.smartairport.model.Transportation;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayerView;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.YouTubePlayerInitListener;

import java.util.List;

public class TransportationAdapter extends RecyclerView.Adapter<TransportationAdapter.HolderTransportation> {
    List<Transportation> listTransportation;

    public TransportationAdapter(List<Transportation> listTransportation){
        this.listTransportation = listTransportation;
    }

    @Override
    public HolderTransportation onCreateViewHolder(ViewGroup viewGroup, int i){
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_transportation,viewGroup,false);
        HolderTransportation holderTransportation = new HolderTransportation(v);
        return holderTransportation;
    }

    @Override
    public void onBindViewHolder(HolderTransportation holderTransportation,final int i){
        holderTransportation.tvName.setText(listTransportation.get(i).getName());
        holderTransportation.tvOrigin.setText(listTransportation.get(i).getOrigin());
        holderTransportation.tvDestination.setText(listTransportation.get(i).getDestination());
        holderTransportation.tvSchedule.setText(listTransportation.get(i).getSchedule());
        holderTransportation.tvTicketPrice.setText(listTransportation.get(i).getTicket_price());
    }

    @Override
    public int getItemCount(){
        return listTransportation.size();
    }

    public Transportation getItem(int position){
        return listTransportation.get(position);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView){
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class HolderTransportation extends  RecyclerView.ViewHolder{
        CardView cvTransportation;
        TextView tvName, tvOrigin, tvDestination, tvSchedule, tvTicketPrice;


        HolderTransportation(View itemView){
            super(itemView);
            cvTransportation = (CardView) itemView.findViewById(R.id.cv_entertainment);
            tvName = (TextView)itemView.findViewById(R.id.tv_name);
            tvOrigin = (TextView)itemView.findViewById(R.id.tv_origin);
            tvDestination = (TextView)itemView.findViewById(R.id.tv_destination);
            tvSchedule = (TextView)itemView.findViewById(R.id.tv_schedule);
            tvTicketPrice = (TextView)itemView.findViewById(R.id.tv_ticket_price);
        }
    }
}