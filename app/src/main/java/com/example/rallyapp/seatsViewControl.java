package com.example.rallyapp;

public interface seatsViewControl {
    void ReceiveSeatsReserved (String seat);
    void DeleteSeatsReserved (String seat);
    void CheckIfThereAreSeats ();
}
