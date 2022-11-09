package com.example.model;

import java.util.List;

public class Zoo {
    public String name;
    public int area;
    public boolean isOpenForVisit;
    public List<String> animals;
    public Ticket ticket;

    public static class Ticket {
        public String age;
        public int price;
    }
}
