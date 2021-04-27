package com.example.rallyapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.rallyapp.Common.Common;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Registro extends AppCompatActivity {

    // Declarar variables
    private EditText edtName, edtAge, edtEmail, edtPassword;
    private Button btnRegistrar;
    // Variables a registrar
    private String name = "", age = "", email = "", password = "";

    // Paquete para autentificacion en Firebase
    FirebaseAuth fAuth;
    DatabaseReference fDatabase;
    SweetAlertDialog authDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        // Relacionar variables con objetos
        edtName = (EditText) findViewById(R.id.EdtName);
        edtAge = (EditText) findViewById(R.id.EdtAge);
        edtEmail = (EditText) findViewById(R.id.EdtEmail);
        edtPassword = (EditText) findViewById(R.id.EdtPassword);
        btnRegistrar = (Button) findViewById(R.id.btnRegistrar);

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (connection_internet()) {

                    name = edtName.getText().toString();
                    age = edtAge.getText().toString();
                    email = edtEmail.getText().toString();
                    password = edtPassword.getText().toString();

                    if (validarCampos(name, age, email, password)){
                        fAuth = FirebaseAuth.getInstance();
                        // Hacer referencia al nodo principal de BD en Firebase
                        fDatabase = FirebaseDatabase.getInstance().getReference();
                        // Metodo para realizar el registro
                        registerUser();
                    }
                }
            }
        });

    }

    private void registerUser() {
        authDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        authDialog.setTitleText(getString(R.string.alert_loading_process));
        authDialog.setCancelable(false);
        authDialog.show();


        final SweetAlertDialog task_Dialog = new SweetAlertDialog(Registro.this, SweetAlertDialog.SUCCESS_TYPE);
        task_Dialog.setConfirmText(getString(R.string.alert_accept));
        task_Dialog.setCancelable(false);

        final SweetAlertDialog task_fail_Dialog = new SweetAlertDialog(Registro.this, SweetAlertDialog.ERROR_TYPE);
        task_fail_Dialog.setConfirmText(getString(R.string.alert_accept));
        task_fail_Dialog.setCancelable(false);

        fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                // Si el registro es exitoso
                if (task.isSuccessful()) {
                    // Mapa de valores a guardar
                    Map<String, Object> values = new HashMap<>();
                    values.put("name", name);
                    values.put("age", age);
                    values.put("email", email);
                    values.put("password", password);

                    // Obtener id del usuario en Firebase
                    String id = fAuth.getCurrentUser().getUid();

                    // Se almacenan los datos en BD
                    // en nodo hijo
                    fDatabase.child("Users").child(id).setValue(values).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task_save) {

                            // Si se guardó en la BD correctamente
                            if (task_save.isSuccessful()) {
                                // System.out.println("TASK: ");
                                // System.out.println(task_save.isSuccessful());

                                authDialog.dismiss();

                                task_Dialog.setContentText(getString(R.string.user_successful_registration));
                                task_Dialog.show();
                                task_Dialog.getButton(SweetAlertDialog.BUTTON_CONFIRM).setBackground(getResources().getDrawable(R.drawable.gray_button_background));
                                task_Dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        onBackPressed();
                                        task_Dialog.dismiss();
                                    }
                                });
                                // Toast.makeText(Registro.this, "¡Registro exitoso!", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                authDialog.dismiss();

                                task_fail_Dialog.setContentText(getString(R.string.user_sign_up_failed_data));
                                task_fail_Dialog.show();
                                task_fail_Dialog.getButton(SweetAlertDialog.BUTTON_CONFIRM).setBackground(getResources().getDrawable(R.drawable.gray_button_background));
                                task_fail_Dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        // onBackPressed();
                                        task_fail_Dialog.dismiss();
                                    }
                                });
                                // Toast.makeText(Registro.this, "No se pudo registrar los datos", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
                else{
                    authDialog.dismiss();

                    task_fail_Dialog.setContentText(getString(R.string.user_sign_up_failed));
                    task_fail_Dialog.show();
                    task_fail_Dialog.getButton(SweetAlertDialog.BUTTON_CONFIRM).setBackground(getResources().getDrawable(R.drawable.gray_button_background));
                    task_fail_Dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            // onBackPressed();
                            task_fail_Dialog.dismiss();
                        }
                    });
                    // Toast.makeText(Registro.this, "No se pudo registrar este usuario", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private boolean validarCampos(String name, String age, String email, String password){
        String input_name = name.trim();
        String input_age = age.trim();
        String input_email = email.trim();
        String input_password = password.trim();

        SweetAlertDialog validarnDialog = new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE);
        validarnDialog.setTitleText(getString(R.string.alert_validate_field_title));
        validarnDialog.setConfirmText(getString(R.string.alert_accept));
        validarnDialog.setCancelable(false);

        if (input_name.isEmpty() || input_age.isEmpty() || input_email.isEmpty() || input_password.isEmpty()){
            validarnDialog.setContentText(getString(R.string.alert_validate_field_empty));
            validarnDialog.show();
            validarnDialog.getButton(SweetAlertDialog.BUTTON_CONFIRM).setBackground(getResources().getDrawable(R.drawable.gray_button_background));
            return false;
        }
        else if (input_password.length() < 6) {
            validarnDialog.setContentText(getString(R.string.alert_validate_password_pattern));
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
                    Registro.this.onBackPressed();
                    connectionDialog.dismiss();
                }
            });
        }
        return res;
    }
}