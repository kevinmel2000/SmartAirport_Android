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
import android.widget.ImageView;
import android.widget.TextView;

import com.laurensius_dede_suhardiman.smartairport.DirectionMap;
import com.laurensius_dede_suhardiman.smartairport.QRBookingGenerator;
import com.laurensius_dede_suhardiman.smartairport.R;
import com.laurensius_dede_suhardiman.smartairport.SmartAirport;
import com.laurensius_dede_suhardiman.smartairport.model.ParkingArea;

import java.util.List;

public class ParkingAreaAdapter extends RecyclerView.Adapter<ParkingAreaAdapter.HolderParkingArea> {
    List<ParkingArea> listParkingArea;
    Context ctx;

    public ParkingAreaAdapter(List<ParkingArea> listParkingArea, Context ctx){
        this.listParkingArea = listParkingArea;
        this.ctx = ctx;
    }

    @Override
    public HolderParkingArea onCreateViewHolder(ViewGroup viewGroup, int i){
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_parking,viewGroup,false);
        HolderParkingArea holderParkingArea = new HolderParkingArea(v);
        return holderParkingArea;
    }

    @Override
    public void onBindViewHolder(HolderParkingArea holderParkingArea,final int i){
        holderParkingArea.tvName.setText(listParkingArea.get(i).getName());
        holderParkingArea.tvStatusCar.setText(listParkingArea.get(i).getStatus_car());
        holderParkingArea.tvStatusMotorcycle.setText(listParkingArea.get(i).getStatus_motorcycle());
        holderParkingArea.btnDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ctx,DirectionMap.class);
                intent.putExtra("object_type", "parking");
                intent.putExtra("parkingObject", listParkingArea.get(i));
                ctx.startActivity(intent);
            }
        });
        holderParkingArea.btnBookingParking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(SmartAirport.user_name != null){
                    Intent intent = new Intent(ctx,QRBookingGenerator.class);
                    intent.putExtra("object_type", "parking");
                    intent.putExtra("parkingObject", listParkingArea.get(i));
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
        Button btnDirection, btnBookingParking;

        HolderParkingArea(View itemView){
            super(itemView);
            cvParkingArea = (CardView) itemView.findViewById(R.id.cv_facility);
            ivImage = (ImageView)itemView.findViewById(R.id.iv_image);
            tvName = (TextView)itemView.findViewById(R.id.tv_name);
            tvStatusCar = (TextView)itemView.findViewById(R.id.tv_status_car);
            tvStatusMotorcycle = (TextView)itemView.findViewById(R.id.tv_status_motorcycle);
            btnDirection = (Button)itemView.findViewById(R.id.btn_direction);
            btnBookingParking = (Button)itemView.findViewById(R.id.btn_booking_parking);
        }
    }
}