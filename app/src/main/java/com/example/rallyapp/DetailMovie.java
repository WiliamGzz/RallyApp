package com.example.rallyapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.rallyapp.Common.Common;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class DetailMovie extends AppCompatActivity {
    private FirebaseDatabase db;
    private DatabaseReference moviesRef;
    // Variables para detalles de pelicula
    private TextView txtName, txtDescription, txtAge, txtCategory, txtClasification;
    private ImageView imgMovie;
    private Button btnFunctions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_movie);

        txtName = (TextView) findViewById(R.id.txtName);
        txtDescription = (TextView) findViewById(R.id.txtDescription);
        txtAge = (TextView) findViewById(R.id.txtAge);
        txtCategory = (TextView) findViewById(R.id.txtCategory);
        txtClasification = (TextView) findViewById(R.id.txtClasification);
        imgMovie = (ImageView) findViewById(R.id.imgMovie);
        btnFunctions = (Button) findViewById(R.id.btnFunctions);

        db = FirebaseDatabase.getInstance();

        String movie_id;
        movie_id = getIntent().getExtras().get("movie_id").toString();
        moviesRef = db.getReference("Movies"  ).child(movie_id);
        moviesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                ListMovie movie = dataSnapshot.getValue(ListMovie.class);
                //Obtenemos los valores de una pelicula en especifico
                String title = movie.getTitle();
                String age = movie.getAge();
                String description = movie.getDescription();
                String image = movie.getImage();
                String clasification = movie.getClasification();
                String category = movie.getCategory();
                //Obteniendo el movie id
                String uid = dataSnapshot.getKey();
                System.out.println("DATA: ");
                System.out.println(title);
                System.out.println(description);
                System.out.println(age);
                System.out.println(image);
                System.out.println(category);
                System.out.println(clasification);
                System.out.println(uid);
                //puedes hacer lo que quieras con los datos
                txtName.setText(title);
                txtAge.setText(age);
                txtCategory.setText(category);
                txtClasification.setText(clasification);
                txtDescription.setText(description);
                Picasso.with(getApplicationContext()).load(image).into(imgMovie);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        btnFunctions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DetailMovie.this.connection_internet()) {
                    final SweetAlertDialog fail = new SweetAlertDialog(DetailMovie.this, SweetAlertDialog.ERROR_TYPE);
                    fail.setTitleText("¿Quieres reservar?");
                    fail.setContentText("Primero codifícalo");
                    fail.setConfirmText(getString(R.string.alert_accept));
                    fail.setCancelable(false);
                    fail.show();
                    fail.getButton(SweetAlertDialog.BUTTON_CONFIRM).setBackground(getResources().getDrawable(R.drawable.gray_button_background));
                    fail.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            // DetailMovie.this.onBackPressed();
                            fail.dismiss();
                        }
                    });
                }
            }
        });

        Toast.makeText(getApplicationContext(), "Selección: " + movie_id, Toast.LENGTH_SHORT).show();

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
                    DetailMovie.this.onBackPressed();
                    connectionDialog.dismiss();
                }
            });
        }
        return res;
    }

}