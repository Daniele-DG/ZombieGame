package com.digregorio.zombiesurvivor;

import static com.digregorio.zombiesurvivor.Level.BLUE;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import com.digregorio.zombiesurvivor.exceptions.*;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class Game {

    private ArrayList<Survivor> survivors;
    private Level level;
    private LinkedList<String> history = new LinkedList<>();

    public void start() {
        if (survivors == null)
            this.survivors = new ArrayList<>();
        else
            survivors.clear();
        this.level = BLUE;
        this.history = new LinkedList<>();
        saveHistory("Game start.");
    }

    public void addSurvivor(Survivor survivor) {
        verifyNameAlreadyTaken(survivor);
        survivors.add(survivor);
        String message = String.format("Added survivor '%s'.", survivor);
        saveHistory(message);
        if (this.level.getMinExperience() < survivor.getCurrentLevel().getMinExperience())
            this.level = survivor.getCurrentLevel();
    }

    public void addExperienceToSurvivor(Survivor survivor) {
        List<Survivor> survivorsAlive = this.survivors.stream()
                .filter(t -> !t.isDead() && t.getName().equals(survivor.getName())).collect(Collectors.toList());
        if (!survivorsAlive.contains(survivor) || survivorsAlive.size() > 1)
            throw new SurvivorNotFoundException("Survivor not found.");
        survivorsAlive.get(0).incrementExperience(1);
        String message = String.format("Survivor '%s' earned 1 exp point.", survivor);
        saveHistory(message);
        if (survivorsAlive.get(0).getCurrentLevel().getMinExperience() > this.level.getMinExperience())
            this.setLevel(survivorsAlive.get(0).getCurrentLevel());
    }

    public void addWound(Survivor survivor) {
        if (survivors.isEmpty())
            throw new SurvivorNotFoundException("No survivor has been found.");
        survivors.forEach(t -> {
            if (t.equals(survivor)) {
                t.addWound();
                String message = String.format("Added wound to survivor '%s'.", survivor);
                saveHistory(message);
                if (t.isDead()) {
                    message = String.format("Survivor '%s' is dead.", survivor);
                    saveHistory(message);
                    if (verifyEndGame())
                        throw new EndGameException("The game is over. No survivor left.");
                }

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

    private void saveHistory(String message) {
        history.push(message);
        log.info(message);
    }
}