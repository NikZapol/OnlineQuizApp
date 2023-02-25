package com.example.onlinequizapp;

import java.util.ArrayList;

public class TicketList {
    private ArrayList<Ticket> tickets;

    public TicketList() {
        this.tickets = new ArrayList<>();
    }

    public void add(Ticket ticket) {
        tickets.add(ticket);
    }


    public ArrayList<Ticket> getTickets() {
        return tickets;
    }
}
