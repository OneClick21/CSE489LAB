package edu.ewubd.cse489lab;

public class Item {
    String id;
    String itemName;
    double cost;
    long date;

    public Item(String id, String itemName, double cost, long date){
        this.id = id;
        this.itemName = itemName;
        this.cost = cost;
        this.date = date;
    };
}
