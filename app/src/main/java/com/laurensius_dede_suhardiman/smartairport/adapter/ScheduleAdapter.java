package com.laurensius_dede_suhardiman.smartairport.adapter;

import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.laurensius_dede_suhardiman.smartairport.R;
import com.laurensius_dede_suhardiman.smartairport.appcontroller.AppController;
import com.laurensius_dede_suhardiman.smartairport.model.Schedule;

import java.text.ParseException;
import java.util.Date;
import java.util.List;


public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.HolderSchedule> {
    List<Schedule> listSchedule;
    int origin_destination;
    Context context;

    public ScheduleAdapter(List<Schedule> listSchedule, int origin_destination, Context ctx){
        this.listSchedule = listSchedule;
        this.origin_destination = origin_destination;
        this.context = ctx;
    }

    @Override
    public HolderSchedule onCreateViewHolder(ViewGroup viewGroup, int i){
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_flight_info,viewGroup,false);
        HolderSchedule holderSchedule = new HolderSchedule(v);
        return holderSchedule;
    }

    @Override
    public void onBindViewHolder(final HolderSchedule holderSchedule,int i){
        ImageLoader imageLoader = AppController.getInstance().getImageLoader();
        imageLoader.get(context.getResources().getString(R.string.url_airlines_logo) + listSchedule.get(i).getAirlines_icon(), new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                holderSchedule.ivImage.setImageBitmap(response.getBitmap());
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Volley image error ","ERROR");
            }
        });
        holderSchedule.tvFlightNo.setText(listSchedule.get(i).getFlight_no());
        holderSchedule.tvSchedule.setText(listSchedule.get(i).getFlight_schedule());
        holderSchedule.tvAirline.setText(listSchedule.get(i).getAirlines());
        if(this.origin_destination == 1){
            holderSchedule.tvLabelOriginDestination.setText("Destination to");
            holderSchedule.tvOriginDestination.setText(listSchedule.get(i).getDestination_name());
            holderSchedule.tvIataIcao.setText(listSchedule.get(i).getDestination_iata().concat(" - ").concat(listSchedule.get(i).getDestination_icao()));
        }else
        if(this.origin_destination == 2){
            holderSchedule.tvLabelOriginDestination.setText("Arrived from");
            holderSchedule.tvOriginDestination.setText(listSchedule.get(i).getOrigin_name());
            holderSchedule.tvIataIcao.setText(listSchedule.get(i).getOrigin_iata().concat(" - ").concat(listSchedule.get(i).getOrigin_icao()));
        }
        holderSchedule.tvStatus.setText(listSchedule.get(i).getFlight_status());
    }

    @Override
    public int getItemCount(){
        return listSchedule.size();
    }

    public Schedule getItem(int position){
        return listSchedule.get(position);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView){
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class HolderSchedule extends  RecyclerView.ViewHolder{
        CardView cvFLightInfo;
        ImageView ivImage;
        TextView tvFlightNo,tvSchedule,tvAirline, tvLabelOriginDestination,tvOriginDestination, tvIataIcao, tvStatus;

        HolderSchedule(View itemView){
            super(itemView);
            cvFLightInfo = (CardView) itemView.findViewById(R.id.cv_flight_info);
            ivImage = (ImageView)itemView.findViewById(R.id.iv_image);
            tvFlightNo = (TextView)itemView.findViewById(R.id.tv_flight_no);
            tvSchedule = (TextView)itemView.findViewById(R.id.tv_schedule);
            tvAirline = (TextView)itemView.findViewById(R.id.tv_airline);
            tvLabelOriginDestination = (TextView)itemView.findViewById(R.id.tv_label_origin_destination);
            tvOriginDestination = (TextView)itemView.findViewById(R.id.tv_origin_destination);
            tvIataIcao = (TextView)itemView.findViewById(R.id.tv_iata_icao);
            tvStatus = (TextView)itemView.findViewById(R.id.tv_status);
        }
    }
}