package com.example.rallyapp;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.rallyapp.Common.Common;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Start extends AppCompatActivity {
    // TextView txtLinkForgotten;
    Button btnSignIn;
    Button btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        connection_internet();

        btnSignIn = findViewById(R.id.btnSignIn);
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Start.this.connection_internet()) {
                    Intent login = new Intent(Start.this, Login.class);
                    Start.this.startActivity(login);
                }
            }
        });

        btnSignUp = findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Start.this.connection_internet()) {
                    Intent registro = new Intent(Start.this, Registro.class);
                    Start.this.startActivity(registro);
                }
            }
        });

        /*txtLinkForgotten = findViewById(R.id.txtLinkForgotten);
        txtLinkForgotten.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/
    }

    private boolean connection_internet(){
        // Si funciona
        boolean res;
        if (Common.isConnectedToInternet(getBaseContext())) {
            res = true;
        }
        else{
            res = false;
            final SweetAlertDialog connectionDialog = new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE);
            connectionDialog.setTitleText(getString(R.string.alert_connection_title));
            connectionDialog.setContentText(getString(R.string.alert_connection_content));
            connectionDialog.setConfirmText(getString(R.string.alert_accept));
            connectionDialog.setCancelable(false);
            connectionDialog.show();
            connectionDialog.getButton(SweetAlertDialog.BUTTON_CONFIRM).setBackground(getResources().getDrawable(R.drawable.gray_button_background));
            connectionDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    Start.this.onBackPressed();
                    connectionDialog.dismiss();
                }
            });
        }
        return res;
    }
}