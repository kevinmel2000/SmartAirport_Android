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

public class FragmentAccount extends Fragment {

    private EditText etEmail, etName,etPhone;
    private Button btnSignOut;

    public FragmentAccount() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflaterSignIn = inflater.inflate(R.layout.fragment_account, container, false);
        etEmail = (EditText) inflaterSignIn.findViewById(R.id.et_email);
        etName = (EditText) inflaterSignIn.findViewById(R.id.et_name);
        etPhone = (EditText) inflaterSignIn.findViewById(R.id.et_phone);
        btnSignOut = (Button) inflaterSignIn.findViewById(R.id.btn_signout);
        return inflaterSignIn;
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        etEmail.setText(SmartAirport.user_email);
        etName.setText(SmartAirport.user_name);
        etPhone.setText(SmartAirport.user_phone);
        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getResources().getString(R.string.sharedpreferences), 0);
                SharedPreferences.Editor editorPreferences = sharedPreferences.edit();
                editorPreferences.clear();
                editorPreferences.commit();
                SmartAirport.user_email = null;
                SmartAirport.user_name = null;
                SmartAirport.user_phone = null;
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

}
