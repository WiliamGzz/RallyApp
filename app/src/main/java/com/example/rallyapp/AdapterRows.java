package com.example.rallyapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdapterRows extends RecyclerView.Adapter<AdapterRows.ViewHolder> {
    List<String> idRows;
    Context context;
    activityRowSeatsControl control;

    public class ViewHolder extends RecyclerView.ViewHolder {
        Button btn_Row;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            btn_Row = (Button) itemView.findViewById(R.id.RowButton);
        }

        public Button getButton(){
            return btn_Row;
        }
    }

    public AdapterRows(List<String> idRows, Context context, activityRowSeatsControl control){
        this.idRows =   idRows;
        this.context = context;
        this.control = control;
    }

    @NonNull
    @Override
    public AdapterRows.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.recycleview_list_rowsala_activity,
                parent,
                false
        );
        return new AdapterRows.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterRows.ViewHolder holder, int position) {
        final String row = idRows.get(position);
        holder.getButton().setText("Fila: "+ row);
        holder.getButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               control.gotoSeats(row);
            }
        });

    }

    @Override
    public int getItemCount() {
        return idRows.size();
    }



}