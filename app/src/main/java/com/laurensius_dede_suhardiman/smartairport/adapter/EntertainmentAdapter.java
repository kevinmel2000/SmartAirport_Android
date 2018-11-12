package com.laurensius_dede_suhardiman.smartairport.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.laurensius_dede_suhardiman.smartairport.R;
import com.laurensius_dede_suhardiman.smartairport.model.Entertainment;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayerView;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.YouTubePlayerInitListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class EntertainmentAdapter extends RecyclerView.Adapter<EntertainmentAdapter.HolderEntertainment> {
    List<Entertainment> listEntertainment;

    public EntertainmentAdapter(List<Entertainment> listEntertainment){
        this.listEntertainment = listEntertainment;
    }

    @Override
    public HolderEntertainment onCreateViewHolder(ViewGroup viewGroup, int i){
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_entertainment_video,viewGroup,false);
        HolderEntertainment holderEntertainment = new HolderEntertainment(v);
        return holderEntertainment;
    }

    @Override
    public void onBindViewHolder(HolderEntertainment holderEntertainment,final int i){
        holderEntertainment.ytVideoPlayer.initialize(
                new YouTubePlayerInitListener() {

                    @Override
                    public void onInitSuccess(
                            final YouTubePlayer initializedYouTubePlayer) {

                        initializedYouTubePlayer.addListener(
                                new AbstractYouTubePlayerListener() {
                                    @Override
                                    public void onReady() {
                                        initializedYouTubePlayer.loadVideo(listEntertainment.get(i).getYt_id(), 0);
                                    }
                                });
                    }
                }, true);
        holderEntertainment.tvVideoTitle.setText(listEntertainment.get(i).getVideo_title());
        holderEntertainment.tvDescription.setText(listEntertainment.get(i).getDescription());
    }

    @Override
    public int getItemCount(){
        return listEntertainment.size();
    }

    public Entertainment getItem(int position){
        return listEntertainment.get(position);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView){
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class HolderEntertainment extends  RecyclerView.ViewHolder{
        CardView cvEntertainment;
        TextView tvVideoTitle,tvDescription;
        YouTubePlayerView ytVideoPlayer;

        HolderEntertainment(View itemView){
            super(itemView);
            cvEntertainment = (CardView) itemView.findViewById(R.id.cv_entertainment);
            ytVideoPlayer = (YouTubePlayerView) itemView.findViewById(R.id.yt_video_player);
            tvVideoTitle = (TextView)itemView.findViewById(R.id.tv_video_title);
            tvDescription = (TextView)itemView.findViewById(R.id.tv_description);
        }
    }
}