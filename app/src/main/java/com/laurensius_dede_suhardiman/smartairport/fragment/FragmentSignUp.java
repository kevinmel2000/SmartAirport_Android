package com.laurensius_dede_suhardiman.smartairport.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.laurensius_dede_suhardiman.smartairport.R;
import com.laurensius_dede_suhardiman.smartairport.SmartAirport;
import com.laurensius_dede_suhardiman.smartairport.appcontroller.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class FragmentSignUp extends Fragment {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editorPreferences;


    private EditText etEmail, etPassword, etName, etPhone;
    private Button btnSignUp;
    private TextView tvSignIn;

    public FragmentSignUp() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflaterSignUp = inflater.inflate(R.layout.fragment_signup, container, false);
        etEmail = (EditText) inflaterSignUp.findViewById(R.id.et_email);
        etPassword = (EditText) inflaterSignUp.findViewById(R.id.et_password);
        etName = (EditText) inflaterSignUp.findViewById(R.id.et_name);
        etPhone = (EditText) inflaterSignUp.findViewById(R.id.et_phone);
        btnSignUp = (Button) inflaterSignUp.findViewById(R.id.btn_signup);
        tvSignIn = (TextView) inflaterSignUp.findViewById(R.id.tv_signin);
        
        return inflaterSignUp;
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateInput();
            }
        });

        tvSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentSignIn signin = new FragmentSignIn();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fl_master,signin,null)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void validateInput(){
        if(etEmail.getText().toString().equals("") || etPassword.getText().toString().equals("") ||
                etName.getText().toString().equals("") || etPhone.getText().toString().equals("")){
            new AlertDialog.Builder(getActivity())
                    .setTitle("Whooops . . .")
                    .setMessage("Please fill the blank field")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.dismiss();
                        }}).show().
                    getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#3d9b2d"));

        }else{
            requestSignUp(etEmail.getText().toString(),
                    etPassword.getText().toString(),
                    etName.getText().toString(),
                    etPhone.getText().toString());
        }
    }

    public void requestSignUp(String e,String p,String n, String ph){
        Random random = new Random();
        int rnd = random.nextInt(999999 - 99) + 99;
        String tag_req_signin = getResources().getString(R.string.tag_req_signin);
        String url = getResources().getString(R.string.api)
                .concat(getResources().getString(R.string.endpoint_signup))
                .concat(String.valueOf(rnd))
                .concat(getResources().getString(R.string.slash));
        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage(getResources().getString(R.string.progress_loading));
        pDialog.show();
        final Map<String, String> params = new HashMap<String, String>();
        params.put("email", e);
        params.put("password", p);
        params.put("name", n);
        params.put("phone", ph);
        JSONObject parameter = new JSONObject(params);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,url, parameter,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        pDialog.dismiss();
                        Log.d(getResources().getString(R.string.debug_tag),response.toString());
                        parseData(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.dismiss();
                        new AlertDialog.Builder(getActivity())
                                .setTitle("Whooops . . .")
                                .setMessage("Something went wrong. Please try again!")
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        requestSignUp(etEmail.getText().toString(),
                                                etPassword.getText().toString(),
                                                etName.getText().toString(),
                                                etPhone.getText().toString());
                                    }}).show().
                                        getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#3d9b2d"));
                    }
                });
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_req_signin);
    }


    public void parseData(JSONObject responseJsonObj){
        Log.d(getResources().getString(R.string.debug_tag),responseJsonObj.toString());
        try{
            String severity = responseJsonObj.getString(getResources().getString(R.string.json_key_severity));
            JSONObject content = responseJsonObj.getJSONObject(getResources().getString(R.string.json_key_content));
            if(severity.equals(getResources().getString(R.string.success))){
                new AlertDialog.Builder(getActivity())
                        .setTitle("Signup Success")
                        .setMessage(responseJsonObj.getString("message"))
                        .setIcon(android.R.drawable.ic_menu_info_details)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                            }}).show().
                        getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#3d9b2d"));
            }else{
                new AlertDialog.Builder(getActivity())
                        .setTitle("Signup Failed")
                        .setMessage(responseJsonObj.getString("message"))
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                            }}).show().
                        getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#3d9b2d"));
            }
        }catch (JSONException e){
            Log.d("DBUG BIJB ", e.getMessage());
            Toast.makeText(getActivity(), e.getMessage(),Toast.LENGTH_LONG).show();
            new AlertDialog.Builder(getActivity())
                    .setTitle("Whooops . . .")
                    .setMessage("Something went wrong. Please try again!")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {

                        }}).show().
                    getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#3d9b2d"));
        }
    }
}
