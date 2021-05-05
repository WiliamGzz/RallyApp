package com.example.rallyapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class activity_rowsSala extends AppCompatActivity implements activityRowSeatsControl{

    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    private DatabaseReference mDatabase;

    List<String> filas;

    Context context;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rows_sala);
        filas = new ArrayList<>();
        context = this;

        AñadirFilas();

        //initialize seat view
        recyclerView = (RecyclerView) findViewById(R.id.ListContainerRowsOfSeats);
        recyclerView.setAdapter(new AdapterRows(filas, activity_rowsSala.this, this));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    void AñadirFilas(){
        filas.add("A");
        filas.add("B");
        filas.add("C");
        filas.add("D");
        filas.add("E");
    }

    void StartActivity(){

    }


    @Override
    public void gotoSeats(String id) {
        Intent intent = new Intent(getApplicationContext(), activity_seats.class);
        startActivity(intent);
        intent.putExtra(EXTRA_MESSAGE, id);
        startActivity(intent);
    }
}