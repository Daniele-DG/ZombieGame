package com.gitproject.zombiesurvivor;

import java.util.LinkedList;

import lombok.Data;

@Data
public class Game {

    private LinkedList<Turn> gameOrder;
    
    public Game(){
        this.gameOrder= new LinkedList<>();
    }
}
