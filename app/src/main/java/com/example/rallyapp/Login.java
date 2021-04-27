package com.example.rallyapp;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.rallyapp.Common.Common;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Login extends AppCompatActivity {
    private Button btnSignIn;
    private EditText edtEmail, edtPassword;
    private String email = "", password = "";
    private FirebaseAuth fAuth;
    SweetAlertDialog authDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        connection_internet();

        fAuth = FirebaseAuth.getInstance();

        edtEmail = (EditText) findViewById(R.id.EdtEmail);
        edtPassword = (EditText) findViewById(R.id.EdtPassword);

        btnSignIn = findViewById(R.id.btnSignIn);
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Login.this.connection_internet()) {
                    email = edtEmail.getText().toString();
                    password = edtPassword.getText().toString();

                    if (Login.this.validarCampos(email, password)) {
                        Login.this.login();
                    }

                }
            }
        });

    }

    public void login ()
    {
        authDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        authDialog.setTitleText(getString(R.string.alert_loading_auth));
        authDialog.setCancelable(false);
        authDialog.show();

        fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    authDialog.dismiss();
                    Intent home = new Intent(Login.this, Home.class);
                    Login.this.startActivity(home);
                    finish();
                    // System.out.println("TASK: ");
                    // System.out.println(task_save.isSuccessful());

                    // Toast.makeText(Registro.this, "¡Registro exitoso!", Toast.LENGTH_SHORT).show();
                }
                else {
                    authDialog.dismiss();

                    final SweetAlertDialog fail_login_Dialog = new SweetAlertDialog(Login.this, SweetAlertDialog.ERROR_TYPE);
                    fail_login_Dialog.setTitleText(getString(R.string.alert_validate_field_title));
                    fail_login_Dialog.setContentText(getString(R.string.alert_fail_login));
                    fail_login_Dialog.setConfirmText(getString(R.string.alert_accept));
                    fail_login_Dialog.setCancelable(false);
                    fail_login_Dialog.show();
                    fail_login_Dialog.getButton(SweetAlertDialog.BUTTON_CONFIRM).setBackground(getResources().getDrawable(R.drawable.gray_button_background));
                    fail_login_Dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            // Login.this.onBackPressed();
                            fail_login_Dialog.dismiss();
                        }
                    });


                }
            }
        });
    }
    /*public void Books (View view)
    {
        Intent books = new Intent (this, PantallaPrinci.class);
        startActivity(books);
    }*/

    private boolean validarCampos(String email, String password){
        String input_email = email.trim();
        String input_password = password.trim();

        SweetAlertDialog validarnDialog = new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE);
        validarnDialog.setTitleText(getString(R.string.alert_validate_field_title));
        validarnDialog.setConfirmText(getString(R.string.alert_accept));
        validarnDialog.setCancelable(false);

        if (input_email.isEmpty() || input_password.isEmpty()){
            validarnDialog.setContentText(getString(R.string.alert_validate_field_empty));
            validarnDialog.show();
            validarnDialog.getButton(SweetAlertDialog.BUTTON_CONFIRM).setBackground(getResources().getDrawable(R.drawable.gray_button_background));
            return false;
        }

        else {
            return true;
        }
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
                    Login.this.onBackPressed();
                    connectionDialog.dismiss();
                }
            });
        }
        return res;
    }
}