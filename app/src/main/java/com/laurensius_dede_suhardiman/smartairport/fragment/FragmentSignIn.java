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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.laurensius_dede_suhardiman.smartairport.BookingProcess;
import com.laurensius_dede_suhardiman.smartairport.R;
import com.laurensius_dede_suhardiman.smartairport.SmartAirport;
import com.laurensius_dede_suhardiman.smartairport.appcontroller.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class FragmentSignIn extends Fragment {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editorPreferences;


    private EditText etEmail, etPassword;
    private Button btnSignIn;
    private TextView tvRegister;
    
    public FragmentSignIn() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflaterSignIn = inflater.inflate(R.layout.fragment_signin, container, false);
        etEmail = (EditText) inflaterSignIn.findViewById(R.id.et_email);
        etPassword = (EditText) inflaterSignIn.findViewById(R.id.et_password);
        btnSignIn = (Button) inflaterSignIn.findViewById(R.id.btn_signin);
        tvRegister = (TextView) inflaterSignIn.findViewById(R.id.tv_register);
        return inflaterSignIn;
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateInput();
            }
        });

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentSignUp signup = new FragmentSignUp();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fl_master,signup,null)
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
        if(etEmail.getText().toString().equals("") || etPassword.getText().toString().equals("")){
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
            requestSignIn(etEmail.getText().toString(),etPassword.getText().toString());
        }
    }

    public void requestSignIn(String e,String p){
        Random random = new Random();
        int rnd = random.nextInt(999999 - 99) + 99;
        String tag_req_signin = getResources().getString(R.string.tag_req_signin);
        String url = getResources().getString(R.string.api)
                .concat(getResources().getString(R.string.endpoint_signin))
                .concat(String.valueOf(rnd))
                .concat(getResources().getString(R.string.slash));
        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage(getResources().getString(R.string.progress_loading));
        pDialog.show();
        final Map<String, String> params = new HashMap<String, String>();
        params.put("email", e);
        params.put("password", p);
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
                        pDialog.dismiss(); new AlertDialog.Builder(getActivity())
                                .setTitle("Whooops . . .")
                                .setMessage("Something went wrong. Please try again!")
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        requestSignIn(etEmail.getText().toString(),etPassword.getText().toString());
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
                        .setTitle("Login Success")
                        .setMessage(responseJsonObj.getString("message"))
                        .setIcon(android.R.drawable.ic_menu_info_details)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                            }}).show().
                        getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#3d9b2d"));
                JSONArray jsonArrayUser = content.getJSONArray("user");
                JSONObject objUser = jsonArrayUser.getJSONObject(0);
                sharedPreferences = getActivity().getSharedPreferences(getResources().getString(R.string.sharedpreferences), 0);
                editorPreferences = sharedPreferences.edit();
                editorPreferences.putString(getResources().getString(R.string.sharedpref_user_id),objUser.getString(getResources().getString(R.string.sharedpref_user_id)));
                editorPreferences.putString(getResources().getString(R.string.sharedpref_user_email),objUser.getString(getResources().getString(R.string.sharedpref_user_email)));
                editorPreferences.putString(getResources().getString(R.string.sharedpref_user_name),objUser.getString(getResources().getString(R.string.sharedpref_user_name)));
                editorPreferences.putString(getResources().getString(R.string.sharedpref_user_phone),objUser.getString(getResources().getString(R.string.sharedpref_user_phone)));
                editorPreferences.commit();
                SmartAirport.user_id = objUser.getString(getResources().getString(R.string.sharedpref_user_id));
                SmartAirport.user_email = objUser.getString(getResources().getString(R.string.sharedpref_user_email));
                SmartAirport.user_name = objUser.getString(getResources().getString(R.string.sharedpref_user_name));
                SmartAirport.user_phone = objUser.getString(getResources().getString(R.string.sharedpref_user_phone));
                FragmentAccount account = new FragmentAccount();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fl_master,account,null)
                        .addToBackStack(null)
                        .commit();
            }else{
                new AlertDialog.Builder(getActivity())
                        .setTitle("Login Failed")
                        .setMessage(responseJsonObj.getString("message"))
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                            }}).show().
                        getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#3d9b2d"));
            }
        }catch (JSONException e){
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
