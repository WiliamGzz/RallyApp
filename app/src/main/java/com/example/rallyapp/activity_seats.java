package com.example.rallyapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class activity_seats extends AppCompatActivity implements seatsViewControl {


    ///DatabaseSeats
    private FirebaseDatabase DtBase = FirebaseDatabase.getInstance();
    DatabaseReference  DtRefSeat;
    ValueEventListener postListener;
    ChildEventListener childListener;
    List<ModelSeat> seats;
    int ctr;


    List<String> PruebaseatsStrings;
    List<String> ReservedSeats;
    String idRow;
    AdapterSeats adapterS;


            ////////




    Button ReservarAsiento;
    LinearLayout container;
    Button Cancelar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seats);

        seats = new ArrayList<>();
        PruebaseatsStrings = new ArrayList<>();
        ctr =1;


        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        idRow = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        CheckSeatsOfFilesFromDatabase();

        //seats = AddSeats(15);
        ReservedSeats = new ArrayList<>();




        container = (LinearLayout) findViewById(R.id.ContainerButtonsSeats);
        ReservarAsiento = (Button) findViewById(R.id.button5);
        Cancelar = (Button) findViewById(R.id.button4);

        Cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DtRefSeat.removeEventListener(postListener);
                DtRefSeat.removeEventListener(childListener);
                Intent intent = new Intent(getApplicationContext(), activity_rowsSala.class);
                startActivity(intent);
            }
        });




    }

    private void CheckSeatsOfFilesFromDatabase(){
        /**DtRefSeat = DtBase.getReference("Salas");
        for(int i=0; i<5; i++){
            final ModelSeat seat = new ModelSeat(""+(i+1), false);
            DtRefSeat.child("1").child("Filas").child("E").child(seat.getIdSeat()).setValue(seat);
        }**/

        DtRefSeat = DtBase.getReference("Salas/1/Filas/"+idRow);

        postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // Get Post object and use the values to update the UI
                if(ctr ==1){
                    seats.clear();
                    for(DataSnapshot data :dataSnapshot.getChildren()){

                            seats.add(new ModelSeat(data.child("idSeat").getValue().toString(), (Boolean) data.child("state").getValue()));




                    }
                }

                PruebaseatsStrings.clear();
                for(ModelSeat model : seats){
                    PruebaseatsStrings.add(model.getIdSeat());
                }
                Toast.makeText(getApplicationContext(), PruebaseatsStrings.toString(), Toast.LENGTH_SHORT).show();

                if(ctr ==1){
                    RecyclerView recyclerView = (RecyclerView) findViewById(R.id.listSeat);
                    adapterS = new AdapterSeats(seats, activity_seats.this, getApplicationContext(), idRow);
                    recyclerView.setAdapter(adapterS);
                    recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 5));
                    ctr = 0;
                }


                // ..
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                //Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        };

        childListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                int position;
                if(snapshot.child("state").getValue().equals(true)){
                    String Noseat = snapshot.child("idSeat").getValue().toString();
                    Toast.makeText(getApplicationContext(), "Se ocupo asiento: "+Noseat, Toast.LENGTH_SHORT).show();
                    //deleteSeat(Noseat, true)
                    position = Integer.parseInt(snapshot.child("idSeat").getValue().toString()) -1;
                    adapterS.notifyItemChanged(position,false);
                }
                if(snapshot.child("state").getValue().equals(false)){
                    String Noseat = snapshot.child("idSeat").getValue().toString();
                    Toast.makeText(getApplicationContext(), "Se desocupo asiento: "+ Noseat, Toast.LENGTH_SHORT).show();
                    position = Integer.parseInt(snapshot.child("idSeat").getValue().toString()) -1;
                    adapterS.notifyItemChanged(position,true);
                    //AddSeat(Integer.parseInt(Noseat));
                    //deleteSeat(Noseat, false);
                }




            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        DtRefSeat.addValueEventListener(postListener);
        DtRefSeat.addChildEventListener(childListener);

    }

    private void deleteSeat(String seat, Boolean state){
        int i=0;
        for(ModelSeat idseat : seats){
            if(idseat.getIdSeat().equals(seat)){
                //seats.remove(i);

                adapterS.notifyItemRemoved(i);

                if(ReservedSeats.contains(idseat.getIdSeat())){
                    Toast.makeText(getApplicationContext(),"Se cancela reservacion", Toast.LENGTH_SHORT).show();
                    ReservedSeats.remove(idseat.getIdSeat());
                    CheckIfThereAreSeats();
                }

                break;
            }
            i=i+1;
        }

        seats.remove(i);

        AddSeat(Integer.parseInt(seat), state);


    }

    private void AddSeat(Integer seat, Boolean state){
        int i=0;
        for(ModelSeat Idseat: seats){
            int NoId = Integer.parseInt(Idseat.getIdSeat());
            if(NoId>seat){
                seats.add(i,new ModelSeat(""+seat, state));
                adapterS.notifyItemInserted(i);
                Toast.makeText(getApplicationContext(), ""+i, Toast.LENGTH_SHORT).show();
                break;
            }
            i=i+1;
        }

        if(i == seats.size()){
            seats.add(i,new ModelSeat(""+seat, state));
            adapterS.notifyItemInserted(i);
        }
    }

    private List<ModelSeat> AddSeats(int no) {
        List<ModelSeat> seats = new ArrayList<>();
        for(int i=0; i<no; i++ ){
            seats.add(new ModelSeat(""+i+""+idRow, false));
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
        CheckIfThereAreSeats();
        Toast.makeText(this, ReservedSeats.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void CheckIfThereAreSeats() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            TransitionManager.beginDelayedTransition(container);
        }
        if(ReservedSeats.size() < 1){
            ReservarAsiento.setVisibility(View.GONE);
            Toast.makeText(this, ReservedSeats.toString(), Toast.LENGTH_SHORT).show();
        }else{
            ReservarAsiento.setVisibility(View.VISIBLE);
        }
    }
}