package com.gitproject.zombiesurvivor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
public class Survivor {
    
    private final String name;
    private int wound;

    public void addWound(){
        this.wound++;
    }

    public boolean isDead(){
        return this.wound > 1;
    }
}
