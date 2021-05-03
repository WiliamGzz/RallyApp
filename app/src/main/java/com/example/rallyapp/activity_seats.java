package com.example.rallyapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class activity_seats extends AppCompatActivity implements seatsViewControl {

    List<ModelSeat> seats;
    List<String> ReservedSeats;
    String idRow;

    Button ReservarAsiento;
    LinearLayout container;
    Button Cancelar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seats);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        idRow = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        seats = AddSeats(15);
        ReservedSeats = new ArrayList<>();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.listSeat);
        recyclerView.setAdapter(new AdapterSeats(seats, this, this));
        recyclerView.setLayoutManager(new GridLayoutManager(this, 5));

        container = (LinearLayout) findViewById(R.id.ContainerButtonsSeats);
        ReservarAsiento = (Button) findViewById(R.id.button5);
        Cancelar = (Button) findViewById(R.id.button4);

        Cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), activity_rowsSala.class);
                startActivity(intent);
            }
        });




    }

    private List<ModelSeat> AddSeats(int no) {
        List<ModelSeat> seats = new ArrayList<>();
        for(int i=0; i<no; i++ ){
            seats.add(new ModelSeat(""+i+""+idRow));
        }
        return seats;
    }

    @Override
    public void ReceiveSeatsReserved(String seat) {
        ReservedSeats.add(seat);
        Toast.makeText(this, ReservedSeats.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void DeleteSeatsReserved(String seat) {
        ReservedSeats.remove(seat);
        Toast.makeText(this, ReservedSeats.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void CheckIfThereAreSeats() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            TransitionManager.beginDelayedTransition(container);
        }
        if(ReservedSeats.size() < 1){
            ReservarAsiento.setVisibility(View.GONE);
        }else{
            ReservarAsiento.setVisibility(View.VISIBLE);
        }
    }
}