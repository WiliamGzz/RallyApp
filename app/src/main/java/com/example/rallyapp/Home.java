package com.example.rallyapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rallyapp.Common.Common;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class Home extends AppCompatActivity {
    private RecyclerView recyclerMovies;
    private DatabaseReference fDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        fDatabase = FirebaseDatabase.getInstance().getReference().child("Movies");
        fDatabase.keepSynced(true);

        recyclerMovies = (RecyclerView) findViewById(R.id.recyclerMovies);
        recyclerMovies.setHasFixedSize(true);
        recyclerMovies.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<ListMovie, ListMovieViewHolder>firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<ListMovie, ListMovieViewHolder>(ListMovie.class, R.layout.item_movie, ListMovieViewHolder.class, fDatabase) {
            @Override
            protected void populateViewHolder(final ListMovieViewHolder listMovieViewHolder, final ListMovie listMovie, final int i) {
                listMovieViewHolder.setTitle(listMovie.getTitle());
                listMovieViewHolder.setDescription(listMovie.getDescription());
                listMovieViewHolder.setImage(getApplicationContext(), listMovie.getImage());

                listMovieViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (connection_internet()) {
                            // System.out.println("DATA: ");
                            // System.out.println();

                            String id = getRef(i).getKey().toString();
                            //data_id = obtenerTickets().get(recyclerViewHelpDesk.getChildLayoutPosition(v)).getId();
                            Intent detailMovie = new Intent(Home.this, DetailMovie.class);
                            detailMovie.putExtra("movie_id", id);
                            startActivity(detailMovie);

                            //Toast.makeText(getApplicationContext(), "Selección: " + obtenerTickets().get(recyclerViewHelpDesk.getChildLayoutPosition(v)).getId(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }
        };
        recyclerMovies.setAdapter(firebaseRecyclerAdapter);
    }

    public static class ListMovieViewHolder extends RecyclerView.ViewHolder {
        View view;
        public ListMovieViewHolder (View itemView) {
            super(itemView);
            view = itemView;
        }

        public void setTitle (String title) {
            TextView input_title = (TextView) view.findViewById(R.id.movieTitle);
            input_title.setText(title);
        }

        public void setDescription (String description) {
            TextView input_description = (TextView) view.findViewById(R.id.movieDescription);
            input_description.setText(description);
        }
        public void setImage (Context ctx, String image) {
            ImageView input_image = (ImageView) view.findViewById(R.id.imgMovie);
            // Picasso.with(ctx).load(image).into(input_image);
            Picasso.get().load(image).into(input_image);

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
                    Home.this.onBackPressed();
                    connectionDialog.dismiss();
                }
            });
        }
        return res;
    }

}