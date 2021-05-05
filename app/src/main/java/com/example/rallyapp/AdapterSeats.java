package com.example.rallyapp;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdapterSeats extends RecyclerView.Adapter<AdapterSeats.ViewHolder>  {


    List<ModelSeat> seats;
    seatsViewControl SC;
    Context context;
    String currentRow;


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtNumberSeat;
        CheckBox checkbox;
        CardView card;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNumberSeat = (TextView) itemView.findViewById(R.id.NoAsiento);
            checkbox = (CheckBox) itemView.findViewById(R.id.CheckSeatBox);
            card = (CardView) itemView.findViewById(R.id.card_view);
        }

        public TextView getTextNoSeat(){
            return txtNumberSeat;
        }

        public CheckBox getCheckbox(){
            return checkbox;
        }

        public CardView getCard() {
            return card;
        }
    }

    public AdapterSeats(List<ModelSeat> seat, seatsViewControl SC, Context context, String row){
        seats = seat;
        this.SC = SC;
        this.context = context;
        currentRow = row;
    }

    @Override
    public AdapterSeats.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.recycleview_list_seats_view,
                parent,
                false
        );



        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull List<Object> payloads) {
        if(payloads.isEmpty()){
            super.onBindViewHolder(holder,position, payloads);
        }else{
            if(payloads.get(0) instanceof Boolean){
                holder.getCheckbox().setEnabled((Boolean) payloads.get(0));
                holder.card.setCardBackgroundColor(Color.WHITE);
                //Toast.makeText(context,"Ocurrio un cambio en: "+seats.get(position).getIdSeat(), Toast.LENGTH_SHORT).show();
            }

            if((Boolean) payloads.get(0)){}else{
                holder.getCheckbox().setChecked(false);
                holder.card.setCardBackgroundColor(Color.GRAY);
                SC.DeleteSeatsReserved(seats.get(position).getIdSeat());
            }
        }

    }

    @Override
    public void onBindViewHolder(final AdapterSeats.ViewHolder holder, int position) {


        final ModelSeat seat = seats.get(position);
        holder.getTextNoSeat().setText(seat.getIdSeat()+currentRow);
        holder.getCheckbox().setChecked(false);
        holder.getCheckbox().setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                if(holder.getCheckbox().isChecked()){
                    SC.ReceiveSeatsReserved(seat.getIdSeat().toString());

                }else{
                    SC.DeleteSeatsReserved(seat.getIdSeat());
                }
                SC.CheckIfThereAreSeats();
            }
        });

        if(seat.getState() == false){

        }else{
            holder.checkbox.setEnabled(false);
            holder.card.setCardBackgroundColor(Color.GRAY);
        }

    }

    @Override
    public int getItemCount() {
        return seats.size();
    }







}
