package com.gitproject.zombiesurvivor;

import java.util.ArrayList;

import exceptions.*;
import lombok.Data;

@Data
public class Game {

    private ArrayList<Survivor> gameOrder;

    public void start() {
        if (gameOrder == null)
            this.gameOrder = new ArrayList<>();
        else
            gameOrder.clear();
    }

    public void addSurvivor(Survivor survivor) {
        verifyNameAlreadyTaken(survivor);
        gameOrder.add(survivor);
    }

    public void addWound(Survivor s) {
        gameOrder.forEach(t -> {
            if (t.equals(s)) {
                t.addWound();
                if (t.isDead() && verifyEndGame())
                    throw new EndGameException("The game is over. No survivor left.");
            }
        });
    }

    private boolean verifyEndGame() {
        return gameOrder.stream().filter(t -> !t.isDead()).count() < 1;
    }

    private void verifyNameAlreadyTaken(Survivor survivor) {
        for (Survivor s : gameOrder) {
            if (s.getName().equals(survivor.getName()))
                throw new NameAlreadyExistException("There is a survivor with the same name. Choose another name.");
        }
    }
}