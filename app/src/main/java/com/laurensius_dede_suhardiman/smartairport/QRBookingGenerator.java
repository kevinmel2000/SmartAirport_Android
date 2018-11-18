package com.laurensius_dede_suhardiman.smartairport;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.laurensius_dede_suhardiman.smartairport.model.ParkingArea;
import com.laurensius_dede_suhardiman.smartairport.model.Transportation;

import java.util.Calendar;

public class QRBookingGenerator extends AppCompatActivity {

    String QR_source = "";
    ImageView ivQR;

    private ParkingArea parking;
    private Transportation transportation;
    private TextView tvBookingCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrbooking_view);
        int y = Calendar.getInstance().get(Calendar.YEAR);
        int m = Calendar.getInstance().get(Calendar.MONTH);
        int d = Calendar.getInstance().get(Calendar.DATE);
        int h = Calendar.getInstance().get(Calendar.HOUR);
        int i = Calendar.getInstance().get(Calendar.MINUTE);
        int s = Calendar.getInstance().get(Calendar.SECOND);

        Intent intent = getIntent();
        String type = intent.getStringExtra("object_type");
        String regex = "";
        if(type.equals("transportation")){
            regex = "TR";
        }else{
            regex = "PR";
        }
        String bookcode = regex
                .concat(SmartAirport.user_id)
                .concat(String.valueOf(y))
                .concat(String.valueOf(m))
                .concat(String.valueOf(d))
                .concat(String.valueOf(h))
                .concat(String.valueOf(i))
                .concat(String.valueOf(s));
        if(type.equals("transportation") ==  true){
            transportation = (Transportation) intent.getSerializableExtra("transportationObject");
            QR_source = SmartAirport.user_email.concat("#").concat(type).concat(transportation.getId()).concat(bookcode);
        }else
        if(type.equals("parking") ==  true){
            parking = (ParkingArea) intent.getSerializableExtra("parkingObject");
            QR_source = SmartAirport.user_email.concat("#").concat(type).concat(parking.getId()).concat(bookcode);
        }
        ivQR = (ImageView)findViewById(R.id.iv_qr);
        tvBookingCode = (TextView)findViewById(R.id.tv_booking_code);
        tvBookingCode.setText(bookcode);
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(QR_source,BarcodeFormat.QR_CODE,200,200);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            ivQR.setImageBitmap(bitmap);
        }catch (WriterException e){
            e.printStackTrace();
        }
    }
}
