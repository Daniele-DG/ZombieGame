package com.digregorio.zombiesurvivor;

import static com.digregorio.zombiesurvivor.Level.BLUE;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import exceptions.*;
import lombok.Data;

@Data
public class Game {

    private ArrayList<Survivor> survivors;
    private Level level;

    public void start() {
        if (survivors == null)
            this.survivors = new ArrayList<>();
        else
            survivors.clear();
        this.level = BLUE;
    }

    public void addSurvivor(Survivor survivor) {
        verifyNameAlreadyTaken(survivor);
        survivors.add(survivor);
        if (this.level.getMinExperience() < survivor.getCurrentLevel().getMinExperience())
            this.level = survivor.getCurrentLevel();
    }

    public void addExperienceToSurvivor(Survivor survivor) {
        List<Survivor> survivorsAlive = this.survivors.stream()
                .filter(t -> !t.isDead() && t.getName().equals(survivor.getName())).collect(Collectors.toList());
        if (!survivorsAlive.contains(survivor) || survivorsAlive.size() > 1)
            throw new SurvivorNotFoundException("Survivor not found.");
        survivorsAlive.get(0).incrementExperience(1);
        if (survivorsAlive.get(0).getCurrentLevel().getMinExperience() > this.level.getMinExperience())
            this.setLevel(survivorsAlive.get(0).getCurrentLevel());
    }

    public void addWound(Survivor s) {
        survivors.forEach(t -> {
            if (t.equals(s)) {
                t.addWound();
                if (t.isDead() && verifyEndGame())
                    throw new EndGameException("The game is over. No survivor left.");
            }
        });
    }

    private boolean verifyEndGame() {
        return survivors.stream().filter(t -> !t.isDead()).count() < 1;
    }

    private void verifyNameAlreadyTaken(Survivor survivor) {
        for (Survivor s : survivors) {
            if (s.getName().equals(survivor.getName()))
                throw new NameAlreadyExistException("There is a survivor with the same name. Choose another name.");
        }
    }
}