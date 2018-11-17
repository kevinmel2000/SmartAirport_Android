package com.laurensius_dede_suhardiman.smartairport;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.laurensius_dede_suhardiman.smartairport.appcontroller.AppController;

import java.io.IOException;

public final class SmartTranslate extends AppCompatActivity {

    private SurfaceView svTranslate;
    private TextView tvPreview;
    private CameraSource cameraSource;
    private Button btnGetText,btnManual;
    private Dialog dialBox;

    TextRecognizer textRecognizer;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_smart_translate);

        getSupportActionBar().setTitle(getResources().getString(R.string.title_activity_translate));

        svTranslate = (SurfaceView) findViewById(R.id.sv_translate);
        tvPreview = (TextView) findViewById(R.id.tv_preview);
        btnGetText = (Button)findViewById(R.id.btn_get_text);
        btnGetText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String raw_text = tvPreview.getText().toString();
                requestTranslate("auto","en",raw_text);
            }
        });
        btnManual = (Button)findViewById(R.id.btn_manual);
        btnManual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SmartTranslate.this, ManualTranslation.class);
                startActivity(intent);
            }
        });
        startReadText();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cameraSource.release();
    }

    void startReadText(){
        textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();
        if (!textRecognizer.isOperational()) {
            Log.w("MainActivity", "Detector dependencies are not yet available.");
        }
        cameraSource = new CameraSource.Builder(getApplicationContext(), textRecognizer)
                .setFacing(CameraSource.CAMERA_FACING_BACK)
                .setRequestedPreviewSize(1280, 1024)
                .setRequestedFps(2.0f)
                .setAutoFocusEnabled(true)
                .build();
        svTranslate.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                            Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(SmartTranslate.this,
                                new String[]{Manifest.permission.CAMERA},100);
                        return;
                    }
                    cameraSource.start(svTranslate.getHolder());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });

        textRecognizer.setProcessor(new Detector.Processor<TextBlock>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<TextBlock> detections) {
                Log.d("Main", "receiveDetections");
                final SparseArray<TextBlock> items = detections.getDetectedItems();
                if (items.size() != 0) {
                    tvPreview.post(new Runnable() {
                        @Override
                        public void run() {
                            StringBuilder value = new StringBuilder();
                            for (int i = 0; i < items.size(); ++i) {
                                TextBlock item = items.valueAt(i);
                                value.append(item.getValue());
                                value.append("\n");
                            }
                            tvPreview.setText(value.toString());
                        }
                    });
                }

            }
        });
    }

    public void requestTranslate(String o,String d,final String s){
        String tag_req_translate = getResources().getString(R.string.tag_req_translate);
        String url = getResources().getString(R.string.api)
                .concat(getResources().getString(R.string.endpoint_translate))
                .concat(o)
                .concat(getResources().getString(R.string.slash))
                .concat(d)
                .concat(getResources().getString(R.string.slash))
                .concat(s)
                .concat(getResources().getString(R.string.slash));
        final ProgressDialog pDialog = new ProgressDialog(SmartTranslate.this);
        pDialog.setMessage(getResources().getString(R.string.progress_loading));
        pDialog.show();
//        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,url, null,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        pDialog.dismiss();
//                        Log.d(getResources().getString(R.string.debug_tag),response.toString());
//
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        pDialog.dismiss();
//                    }
//                });
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pDialog.dismiss();
                        Log.d("Respon String ",response);
                        Toast.makeText(SmartTranslate.this,response,Toast.LENGTH_LONG).show();
                        dialBox = translateResult(s,response);
                        dialBox.show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.dismiss();
                    }
                });
        AppController.getInstance().addToRequestQueue(stringRequest, tag_req_translate);
    }

    private Dialog translateResult(String s,String r){
        dialBox = new AlertDialog.Builder(this)
                .setTitle("Translation result")
                .setMessage(s + " = " + r)
                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialBox.dismiss();
                    }
                })
                .create();
        return dialBox;
    }


}