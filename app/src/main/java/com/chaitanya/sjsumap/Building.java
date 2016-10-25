package com.chaitanya.sjsumap;

/**
 * Created by 33843 on 10/22/2016.
 */
public class Building {

    private Coordinate start;
    private Coordinate end;
    public String name;
    public String address;
    public int id;
    //Declare all variable related to building here.

    public Building(int id, String name,String address, Coordinate start, Coordinate end){
        this.start = start;
        this.end = end;
        this.name = name;
        this.address = address;
        this.id = id;
        //Initilaize all variables here.
    }

    public boolean checkTouch(int x, int y){
        return this.start.x < x && this.end.x > x && this.start.y < y && this.end.y >y;
    }

}
