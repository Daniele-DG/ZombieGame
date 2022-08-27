package com.digregorio.zombiesurvivor;

import static com.digregorio.zombiesurvivor.Level.BLUE;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.digregorio.zombiesurvivor.exceptions.*;

import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Data
@ToString
@Slf4j
public class Survivor {

    private final String name;
    private int wound;
    private List<Turn> turns;
    private Equipment equipment;
    private int currentExperience;
    private Level currentLevel;

    public Survivor(String name) {
        this.name = name;
        this.wound = 0;
        this.turns = new LinkedList<>();
        this.equipment = new Equipment();
        this.currentExperience = 0;
        this.currentLevel = BLUE;
    }

    public void addWound() {
        if (this.wound > 2)
            return;
        this.wound++;
        log.info("Added wound to survivor: " + this);
        if (this.wound == 2)
            return;
        ArrayList<Tool> inHand = this.equipment.getInHand();
        if (!inHand.isEmpty()) {
            Tool toolRemoved = this.removeInHandTool(getLastElement(inHand));
            try {

                addInReserve(toolRemoved);
            } catch (FullEquipmentException exception) {
                ArrayList<Tool> inReserve = this.equipment.getInReserve();
                Tool removeInReserveTool = this.removeInReserveTool(getLastElement(inReserve));
                log.info("Removed tool " + removeInReserveTool);
                addInReserve(toolRemoved);
            }
        }
    }

    public boolean isDead() {
        return this.wound > 1;
    }

    public void incrementExperience(int expEarned) {
        this.setCurrentExperience(currentExperience + expEarned);
        this.updateLevel();
    }

    public void addInHand(Tool toolToAdd) {
        ArrayList<Tool> inHand = this.equipment.getInHand();
        if (inHand.size() >= Equipment.MAX_IN_HAND - wound) {
            throw new FullEquipmentException("In hand equipment is full. Please move some item in your reserve.");
        }
        this.equipment.addToInHand(toolToAdd);
    }

    public void addInReserve(Tool toolToAdd) {
        ArrayList<Tool> inReserve = this.equipment.getInReserve();
        if (inReserve.size() >= Equipment.MAX_IN_RESERVE) {
            throw new FullEquipmentException("In reserve equipment is full. Please remove some item to add a new one.");
        }
        this.equipment.addToInReserve(toolToAdd);
    }

    public Tool removeInHandTool(Tool toolToRemove) {
        return this.equipment.removeInHandTool(toolToRemove);
    }

    public Tool removeInReserveTool(Tool toolToRemove) {
        return this.equipment.removeInReserveTool(toolToRemove);
    }

    private Tool getLastElement(ArrayList<Tool> tools) {
        if (tools.isEmpty()) {
            return null;
        }
        return tools.get(tools.size() - 1);
    }

    private void updateLevel() {
        Arrays.stream(Level.values()).forEach(level -> {
            if (level.getMinExperience() > this.currentLevel.getMinExperience()
                    && level.getMinExperience() <= this.currentExperience) {
                this.currentLevel = level;
            }
        });
    }
}
