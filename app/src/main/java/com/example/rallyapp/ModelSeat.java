package com.example.rallyapp;

public class ModelSeat {
    private String Id;

    public void setId(String id) {
        Id = id;
    }

    public void setEstado(Boolean estado) {
        Estado = estado;
    }

    private Boolean Estado;

    public ModelSeat(String idSeat, Boolean State){
        Id = idSeat;
        Estado = State;
    }

    public String getIdSeat() {
        return Id;
    }
    public Boolean getState() {
        return Estado;
    }
}
