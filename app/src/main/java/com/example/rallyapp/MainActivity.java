package com.example.rallyapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void Registrar (View view)
    {
        Intent registrar = new Intent (this, Registro.class);
        // Comentario para git
        startActivity(registrar);
    }
    public void Books (View view)
    {
        Intent books = new Intent (this, PantallaPrinci.class);
        startActivity(books);
    }
}