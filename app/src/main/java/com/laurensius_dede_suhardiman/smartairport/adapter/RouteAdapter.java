package com.laurensius_dede_suhardiman.smartairport.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.laurensius_dede_suhardiman.smartairport.BookingProcess;
import com.laurensius_dede_suhardiman.smartairport.R;
import com.laurensius_dede_suhardiman.smartairport.SmartAirport;
import com.laurensius_dede_suhardiman.smartairport.model.Route;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Random;

public class RouteAdapter extends RecyclerView.Adapter<RouteAdapter.HolderRoute> {
    List<Route> listRoute;
    Context ctx;

    public RouteAdapter(List<Route> listRoute, Context ctx){
        this.listRoute = listRoute;
        this.ctx = ctx;
    }

    @Override
    public HolderRoute onCreateViewHolder(ViewGroup viewGroup, int i){
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_route,viewGroup,false);
        HolderRoute holderRoute = new HolderRoute(v);
        return holderRoute;
    }

    @Override
    public void onBindViewHolder(HolderRoute holderRoute,final int i){
        holderRoute.tvOriginIataIcao.setText(listRoute.get(i).getOrigin_iata() + "\n(" + listRoute.get(i).getOrigin_icao() + ")");
        holderRoute.tvDestinationIataIcao.setText(listRoute.get(i).getDestination_iata() + "\n(" + listRoute.get(i).getDestination_icao() + ")");
        holderRoute.tvSchedule.setText("Flight Shedule : " + listRoute.get(i).getFlight_schedule());
        holderRoute.tvDuration.setText("Duration : " + listRoute.get(i).getFlight_duration_minutes() + " minutes");
        holderRoute.tvOrigin.setText("Departure : " + listRoute.get(i).getOrigin_name());
        holderRoute.tvDestination.setText("Arrival : " + listRoute.get(i).getDestination_name());
        holderRoute.tvAirline.setText(listRoute.get(i).getAirlines());

        Random random = new Random();
        double rnd = 625500.00 + random.nextFloat() * (999000 - 625000);
        final double harga = rnd / 1000;

        final DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        holderRoute.tvPrice.setText("IDR " + df.format(harga) + "K");

        holderRoute.btnBookNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(SmartAirport.user_name != null){
                    Intent intent = new Intent(ctx,BookingProcess.class);
                    intent.putExtra("routeObject",listRoute.get(i));
                    intent.putExtra("price",String.valueOf(df.format(harga) ));
                    ctx.startActivity(intent);
                }else{
                    new AlertDialog.Builder(ctx)
                            .setTitle("Whooops . . .")
                            .setMessage("Please sigin before booking!")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    dialog.dismiss();
                                }}).show().
                            getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#3d9b2d"));
                }

            }
        });

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
        TextView tvSchedule,tvDuration,tvOrigin,tvDestination,tvAirline,tvPrice;
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
            tvPrice = (TextView)itemView.findViewById(R.id.tv_price);
            btnBookNow = (Button)itemView.findViewById(R.id.btn_book_now);
        }
    }
}