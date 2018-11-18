package com.laurensius_dede_suhardiman.smartairport;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.database.DataSetObserver;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.laurensius_dede_suhardiman.smartairport.appcontroller.AppController;

public class ManualTranslation extends AppCompatActivity {

    String[] array_language;
    String[] array_language_code;
    private EditText etResource, etResult;
    private Spinner spLangOrig, spLangDest;
    private Button btnTranslate;
    private Dialog dialBox;


    String selected_orig;
    String selected_dest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_translation);

        getSupportActionBar().setTitle(getResources().getString(R.string.title_activity_manual_translate));

        array_language = getResources().getStringArray(R.array.language);
        array_language_code  = getResources().getStringArray(R.array.language_code);
        etResource = (EditText)findViewById(R.id.et_resource);
        etResult= (EditText)findViewById(R.id.et_result);
        spLangOrig = (Spinner)findViewById(R.id.sp_language_origin);
        spLangDest = (Spinner)findViewById(R.id.sp_language_destination);
        btnTranslate = (Button)findViewById(R.id.btn_translate);



        ArrayAdapter<String> langAdapter = new ArrayAdapter(
                ManualTranslation.this,
                R.layout.custom_spinner_item,array_language);
        spLangOrig.setAdapter(langAdapter);
        spLangDest.setAdapter(langAdapter);

        spLangOrig.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_orig = array_language_code[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spLangDest.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_dest = array_language_code[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnTranslate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etResource.getText().toString().equals("")){
                    Toast.makeText(ManualTranslation.this,"Pleas input text", Toast.LENGTH_LONG).show();
                }else{
                    requestTranslate(selected_orig,selected_dest,etResource.getText().toString());
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
        final ProgressDialog pDialog = new ProgressDialog(ManualTranslation.this);
        pDialog.setMessage(getResources().getString(R.string.progress_loading));
        pDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pDialog.dismiss();
                        Log.d("Respon String ",response);
                        etResult.setText(response);
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


}
