package com.digregorio.zombiesurvivor;

import static com.digregorio.zombiesurvivor.Level.BLUE;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import exceptions.*;
import lombok.Data;
import lombok.Singular;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Data
@ToString
@Slf4j
public class Survivor {

    private final String name;
    private int wound;
    @Singular
    private List<Turn> turns;
    private Equipment equipment;
    private int currentExperience;
    private Level currentLevel;

    public Survivor(String name, int wound) {
        this.name = name;
        this.wound = wound;
        this.turns = new LinkedList<>();
        this.equipment = new Equipment();
        this.currentExperience = 0;
        this.currentLevel = BLUE;
    }

    public void addWound() {
        this.wound++;
        log.info("Added wound to survivor: " + this);
        Tool[] inHand = this.equipment.getInHand();
        Tool toolRemoved = this.removeInHandTool(getLastElement(inHand));
        try {
            addInReserve(toolRemoved);
        } catch (FullEquipmentException exception) {
            Tool[] inReserve = this.equipment.getInReserve();
            Tool removeInReserveTool = this.removeInReserveTool(getLastElement(inReserve));
            log.info("Removed tool " + removeInReserveTool);
            addInReserve(toolRemoved);
        }
    }

    public boolean isDead() {
        return this.wound > 1;
    }

    public void incrementExperience(int expEarned) {
        int oldExperience = currentExperience;
        this.setCurrentExperience(currentExperience + expEarned);
        Stream<Level> filteredLevels = Arrays.asList(Level.values()).stream().filter(t -> !t.equals(currentLevel)
                && t.getMinExperience() < currentExperience && t.getMinExperience() > oldExperience);
        if (filteredLevels.count() > 0)
            this.setCurrentLevel(this.incrementLevel(filteredLevels));
    }

    public void addInHand(Tool toolToAdd) {
        Tool[] inHand = this.equipment.getInHand();
        if (getNumberOfTools(inHand) > 1) {
            throw new FullEquipmentException("In hand equipment is full. Please move some item in your reserve.");
        }
        this.equipment.addToEmptyPosition(toolToAdd, inHand, inHand.length, wound);
    }

    public void addInReserve(Tool toolToAdd) {
        Tool[] inReserve = this.equipment.getInReserve();
        if (getNumberOfTools(inReserve) > 3) {
            throw new FullEquipmentException("In reserve equipment is full. Please move some item in your reserve.");
        }
        this.equipment.addToEmptyPosition(toolToAdd, inReserve, inReserve.length, wound);
    }

    public Tool removeInHandTool(Tool toolToRemove) {
        Tool[] inHand = this.equipment.getInHand();
        return this.equipment.remove(toolToRemove, inHand, inHand.length);
    }

    public Tool removeInReserveTool(Tool toolToRemove) {
        Tool[] inReserve = this.equipment.getInReserve();
        return this.equipment.remove(toolToRemove, inReserve, inReserve.length);
    }

    private Tool getLastElement(Tool[] tools) {
        return tools[tools.length - 1];
    }

    private int getNumberOfTools(Tool[] tools) {
        int size = 0;
        for (int i = 0; i < tools.length; i++) {
            if (tools[i] != null)
                size++;
        }
        return size;
    }

    private Level incrementLevel(Stream<Level> filteredLevels) {
        List<Level> availablesLevels = filteredLevels.collect(Collectors.toList());
        if (availablesLevels.size() == 1)
            return availablesLevels.get(0);
        throw new LevelNotFoundException("Error during level update.");
    }
}
