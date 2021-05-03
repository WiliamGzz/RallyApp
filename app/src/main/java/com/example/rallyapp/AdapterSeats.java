package com.example.rallyapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdapterSeats extends RecyclerView.Adapter<AdapterSeats.ViewHolder>  {
    List<ModelSeat> seats;
    seatsViewControl SC;
    Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtNumberSeat;
        CheckBox checkbox;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNumberSeat = (TextView) itemView.findViewById(R.id.NoAsiento);
            checkbox = (CheckBox) itemView.findViewById(R.id.CheckSeatBox);
        }

        public TextView getTextNoSeat(){
            return txtNumberSeat;
        }

        public CheckBox getCheckbox(){
            return checkbox;
        }
    }

    public AdapterSeats(List<ModelSeat> seat, seatsViewControl SC, Context context){
        seats = seat;
        this.SC = SC;
        this.context = context;
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
    public void onBindViewHolder(final AdapterSeats.ViewHolder holder, int position) {
        final ModelSeat seat = seats.get(position);
        holder.getTextNoSeat().setText(seat.getIdSeat());
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
    }

    @Override
    public int getItemCount() {
        return seats.size();
    }
}
